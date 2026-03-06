package week4.framework.core;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.*;
import week4.framework.annotations.*;
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
    private static AuthValidator authValidator;
    private static final Map<String, RouteInfo> routes = new HashMap<>();
    private static final Gson gson = new Gson();

    public static void setAuthValidator(AuthValidator validator) {
        authValidator = validator;
    }

    public static void registerRoute(String path, RouteInfo route) {
        routes.put(path, route);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            // 路径剥离
            String path = exchange.getRequestURI().getPath();
            if (path.startsWith("/web-")) path = path.replaceFirst("^/web-[^/]+", "");

            // 路由匹配
            RouteInfo route = routes.get(path);
            if (route == null || !route.getHttpMethod().equals(exchange.getRequestMethod())) {
                throw new NotFoundException("Endpoint not found: " + path);
            }

            // 鉴权与 Payload 提取
            JsonObject payload = null;
            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                JwtUtils.validate(token);
                payload = JwtUtils.getPayload(token);
                if (authValidator != null) authValidator.validate(payload);
            } else if (route.isAuthRequired()) {
                throw new UnauthorizedException("Missing Authorization Header");
            }

            // 统一参数解析
            Object[] methodArgs = resolveAllParameters(route, exchange, payload);

            // 执行并响应
            Object result = route.invoke(methodArgs);
            sendWrappedResponse(exchange, 200, "success", result);

        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

    // 各种参数解析
    private Object[] resolveAllParameters(RouteInfo route, HttpExchange exchange, JsonObject payload) throws IOException {
        Parameter[] params = route.getParameters();
        Object[] args = new Object[params.length];

        // 数据源准备
        String method = exchange.getRequestMethod();
        String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
        byte[] bodyBytes;
        String bodyStr = null;
        JsonObject jsonBody = null;
        Map<String, MultipartFile> fileMap = null;

        if ("POST".equals(method)) {
            bodyBytes = exchange.getRequestBody().readAllBytes();
            if (contentType != null && contentType.contains("multipart/form-data")) {
                String boundary = contentType.split("boundary=")[1];
                fileMap = MultipartUtils.parse(bodyBytes, boundary);
            } else {
                bodyStr = new String(bodyBytes, StandardCharsets.UTF_8);
                try {
                    if (!bodyStr.isBlank()) jsonBody = JsonParser.parseString(bodyStr).getAsJsonObject();
                } catch (Exception ignored) {
                }
            }
        }
        Map<String, String> queryMap = parseQuery(exchange.getRequestURI().getQuery());

        // 遍历参数进行注入
        for (int i = 0; i < params.length; i++) {
            Parameter p = params[i];

            // AuthClaim > RequestBody > RequestPart > RequestParam
            if (p.isAnnotationPresent(AuthClaim.class)) {
                if (payload == null) throw new UnauthorizedException("Session required");
                args[i] = extractFromJson(p, payload, p.getAnnotation(AuthClaim.class).value(), true);
            } else if (p.isAnnotationPresent(RequestBody.class)) {
                if (bodyStr == null || bodyStr.isBlank()) throw new BadRequestException("Body is empty");
                args[i] = gson.fromJson(bodyStr, p.getType());
            } else if (p.isAnnotationPresent(RequestPart.class)) {
                if (fileMap == null) throw new BadRequestException("Not a multipart request");
                args[i] = fileMap.get(p.getAnnotation(RequestPart.class).value());
            } else if (p.isAnnotationPresent(RequestParam.class)) {
                RequestParam ann = p.getAnnotation(RequestParam.class);
                if ("POST".equals(method) && jsonBody != null) {
                    args[i] = extractFromJson(p, jsonBody, ann.value(), ann.required());
                } else {
                    String val = queryMap.get(ann.value());
                    if (val == null && ann.required()) throw new BadRequestException("Missing param: " + ann.value());
                    args[i] = convertType(p.getType(), val);
                }
            }
        }
        return args;
    }

    private Object extractFromJson(Parameter p, JsonObject json, String key, boolean required) {
        if (json == null || !json.has(key)) {
            if (required) throw new BadRequestException("Missing attribute: " + key);
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
        } catch (Exception e) {
            throw new BadRequestException("Type mismatch: " + value);
        }
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
        cause.printStackTrace();
        int status = 500;
        String message = cause.getMessage();
        if (cause instanceof BusinessException) status = ((BusinessException) cause).getCode();
        else if (cause instanceof com.google.gson.JsonSyntaxException) {
            status = 400;
            message = "JSON Syntax Error";
        }
        if (message == null)
            message = "Internal Server Error\n[ERROR] " + cause.getClass().getSimpleName() + ": " + e.getMessage();
        sendWrappedResponse(exchange, status, message, null);
    }

    private void sendWrappedResponse(HttpExchange exchange, int status, String message, Object data) throws IOException {
        ApiResponse apiResponse = new ApiResponse(status, message, data);
        byte[] bytes = gson.toJson(apiResponse).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}