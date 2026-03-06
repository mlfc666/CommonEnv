package week4.framework.core;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.*;
import week4.framework.annotations.RequestBody;
import week4.framework.annotations.RequestParam;
import week4.framework.annotations.RequestPart;
import week4.framework.models.ApiResponse;
import week4.framework.models.MultipartFile;
import week4.framework.models.RouteInfo;
import week4.framework.utils.JwtUtils;
import week4.framework.utils.MultipartUtils;
import week4.framework.exception.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DispatcherHandler implements HttpHandler {
    private static final Map<String, RouteInfo> routes = new HashMap<>();
    private static final Gson gson = new Gson(); // 默认忽略 null 字段

    public static void registerRoute(String path, RouteInfo route) {
        routes.put(path, route); // 注册路由
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            // 路径预处理 剥离容器环境前缀 /web-xxxx
            String path = exchange.getRequestURI().getPath();
            if (path.startsWith("/web-")) path = path.replaceFirst("^/web-[^/]+", "");

            // 路由匹配
            RouteInfo route = routes.get(path);
            if (route == null || !route.getHttpMethod().equals(exchange.getRequestMethod())) {
                throw new NotFoundException("Endpoint not found: " + path);
            }

            // 鉴权校验
            if (route.isAuthRequired()) {
                String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new UnauthorizedException("Missing Authorization Header");
                }
                JwtUtils.validate(authHeader.substring(7)); // 校验失败抛出 401
            }

            // 参数解析
            Object[] methodArgs;
            String contentType = exchange.getRequestHeaders().getFirst("Content-Type");

            if ("POST".equals(exchange.getRequestMethod())) {
                byte[] bodyBytes = exchange.getRequestBody().readAllBytes();

                if (contentType != null && contentType.contains("multipart/form-data")) {
                    // 解析文件上传请求
                    String boundary = contentType.split("boundary=")[1];
                    Map<String, MultipartFile> fileMap = MultipartUtils.parse(bodyBytes, boundary);
                    methodArgs = resolveMultipartParameters(route, fileMap);
                } else {
                    // 处理普通 JSON 请求
                    String body = new String(bodyBytes, StandardCharsets.UTF_8);
                    methodArgs = resolvePostParameters(route, body);
                }
            } else {
                String query = exchange.getRequestURI().getQuery();
                methodArgs = resolveGetParameters(route, query);
            }

            // 执行业务逻辑并包装响应
            Object result = route.invoke(methodArgs);
            sendWrappedResponse(exchange, 200, "success", result);

        } catch (Exception e) {
            handleException(exchange, e); // 统一异常拦截
        }
    }

    private Object[] resolvePostParameters(RouteInfo route, String body) {
        Parameter[] params = route.getParameters();
        if (params.length == 0) return new Object[0];

        Object[] args = new Object[params.length];
        JsonObject jsonBody = null;
        try {
            if (body != null && !body.trim().isEmpty()) {
                jsonBody = JsonParser.parseString(body).getAsJsonObject();
            }
        } catch (Exception ignored) {}

        for (int i = 0; i < params.length; i++) {
            Parameter p = params[i];
            if (p.isAnnotationPresent(RequestBody.class)) {
                // @RequestBody 解析整个 JSON 对象
                if (body == null || body.isBlank()) throw new BadRequestException("Request body is empty");
                args[i] = gson.fromJson(body, p.getType());
            } else if (p.isAnnotationPresent(RequestParam.class)) {
                // @RequestParam 解析 JSON 字段
                args[i] = extractValueFromJson(p, jsonBody);
            }
        }
        return args;
    }

    private Object[] resolveMultipartParameters(RouteInfo route, Map<String, MultipartFile> fileMap) {
        Parameter[] params = route.getParameters();
        Object[] args = new Object[params.length];

        for (int i = 0; i < params.length; i++) {
            Parameter p = params[i];
            if (p.isAnnotationPresent(RequestPart.class)) {
                String key = p.getAnnotation(RequestPart.class).value();
                args[i] = fileMap.get(key); // 从解析好的 Map 中注入文件对象
            }
        }
        return args;
    }

    private Object[] resolveGetParameters(RouteInfo route, String query) {
        Parameter[] params = route.getParameters();
        if (params.length == 0) return new Object[0];

        Map<String, String> queryMap = parseQuery(query);
        Object[] args = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            Parameter p = params[i];
            RequestParam ann = p.getAnnotation(RequestParam.class);
            if (ann != null) {
                String val = queryMap.get(ann.value());
                if (val == null && ann.required()) throw new BadRequestException("Missing param: " + ann.value());
                args[i] = convertType(p.getType(), val);
            }
        }
        return args;
    }

    private Object extractValueFromJson(Parameter p, JsonObject json) {
        RequestParam ann = p.getAnnotation(RequestParam.class);
        String key = ann.value();
        if (json == null || !json.has(key)) {
            if (ann.required()) throw new BadRequestException("Missing param: " + key);
            return null;
        }
        String raw = json.get(key).isJsonNull() ? null : json.get(key).getAsString();
        return convertType(p.getType(), raw);
    }

    private Object convertType(Class<?> type, String value) {
        if (value == null) return null;
        try {
            if (type == String.class) return value;
            if (type == Integer.class || type == int.class) return Integer.parseInt(value);
            if (type == Long.class || type == long.class) return Long.parseLong(value);
            if (type == Boolean.class || type == boolean.class) return Boolean.parseBoolean(value);
            if (type == Double.class || type == double.class) return Double.parseDouble(value);
            return value;
        } catch (Exception e) { throw new BadRequestException("Type conversion failed: " + value); }
    }

    private Map<String, String> parseQuery(String query) {
        if (query == null) return Collections.emptyMap();
        Map<String, String> map = new HashMap<>();
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length > 1) map.put(kv[0], URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
        }
        return map;
    }

    private void handleException(HttpExchange exchange, Exception e) throws IOException {
        Throwable cause = (e instanceof InvocationTargetException) ? e.getCause() : e;
        int status = 500;
        String message = cause.getMessage();

        if (cause instanceof BusinessException) {
            status = ((BusinessException) cause).getCode(); // 获取自定义状态码 (400, 401, 403, 409)
        } else if (cause instanceof com.google.gson.JsonSyntaxException) {
            status = 400;
            message = "Invalid JSON structure";
        }

        if (message == null) message = "Internal Server Error";
        sendWrappedResponse(exchange, status, message, null);
    }

    private void sendWrappedResponse(HttpExchange exchange, int status, String message, Object data) throws IOException {
        ApiResponse apiResponse = new ApiResponse(status, message, data);
        String json = gson.toJson(apiResponse);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length); // HTTP 状态码与业务 code 同步
        try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
    }
}