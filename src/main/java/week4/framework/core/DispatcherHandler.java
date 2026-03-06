package week4.framework.core;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import week4.framework.utils.JwtUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DispatcherHandler implements HttpHandler {
    private static final Map<String, RouteInfo> routes = new HashMap<>();
    private static final Gson gson = new Gson(); // 引入 Gson 实例

    public static void registerRoute(String path, RouteInfo route) { routes.put(path, route); }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.startsWith("/web-")) path = path.replaceFirst("^/web-[^/]+", "");

        RouteInfo route = routes.get(path);
        if (route == null || !route.getHttpMethod().equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 404, "{\"error\":\"Not Found\"}");
            return;
        }

        // JWT 鉴权校验逻辑
        if (route.isAuthRequired()) {
            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ") || !JwtUtils.validate(authHeader.substring(7))) {
                sendResponse(exchange, 401, "{\"error\":\"Unauthorized\"}"); // 校验失败返回401
                return;
            }
        }

        try {
            Object result;
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                // 如果方法有参数，尝试将 Body 转为该参数类型的对象
                if (route.getParameterTypes().length > 0) {
                    Object param = gson.fromJson(body, route.getParameterTypes()[0]);
                    result = route.invoke(param);
                } else {
                    result = route.invoke();
                }
            } else {
                result = route.invoke();
            }
            sendResponse(exchange, 200, gson.toJson(result)); // 使用 Gson 序列化
        } catch (Exception e) {
            sendResponse(exchange, 500, "{\"error\":\"Internal Server Error\"}");
        }
    }

    private void sendResponse(HttpExchange exchange, int code, String body) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
    }
}