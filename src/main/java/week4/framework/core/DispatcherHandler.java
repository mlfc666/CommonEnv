package week4.framework.core;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import week4.framework.models.ApiResponse;
import week4.framework.utils.JwtUtils;
import week4.framework.exception.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DispatcherHandler implements HttpHandler {
    private static final Map<String, RouteInfo> routes = new HashMap<>();
    private static final Gson gson = new Gson();

    public static void registerRoute(String path, RouteInfo route) {
        routes.put(path, route);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            // 路径预处理
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
                if (authHeader == null || !authHeader.startsWith("Bearer ") || !JwtUtils.validate(authHeader.substring(7))) {
                    throw new UnauthorizedException("Invalid or missing token");
                }
            }

            // 参数解析与业务执行
            Object result;
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                if (route.getParameterTypes().length > 0) {
                    Object param = gson.fromJson(body, route.getParameterTypes()[0]);
                    result = route.invoke(param);
                } else {
                    result = route.invoke();
                }
            } else {
                result = route.invoke();
            }

            // 成功返回
            sendWrappedResponse(exchange, 200, "success", result);

        } catch (Exception e) {
            // 6. 统一异常处理
            handleException(exchange, e);
        }
    }

    private void handleException(HttpExchange exchange, Exception e) throws IOException {
        // 反射异常需要解包
        Throwable cause = (e instanceof InvocationTargetException) ? e.getCause() : e;

        int code = 500;
        String message = cause.getMessage();

        // 识别业务自定义异常
        if (cause instanceof BusinessException) {
            code = ((BusinessException) cause).getCode();
        } else if (cause instanceof com.google.gson.JsonSyntaxException) {
            code = 400;
            message = "Invalid JSON format";
        }

        if (message == null) message = "Internal Server Error";

        sendWrappedResponse(exchange, code, message, null);
    }

    private void sendWrappedResponse(HttpExchange exchange, int code, String message, Object data) throws IOException {
        ApiResponse responseContainer = new ApiResponse(code, message, data);
        String jsonResponse = gson.toJson(responseContainer);

        byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");

        // 发送对应的 HTTP 状态码
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}