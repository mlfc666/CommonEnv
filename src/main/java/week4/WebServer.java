package week4;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;


import java.io.*;

public class WebServer {
    public static void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // 映射根路径
        server.createContext("/", new StaticResourceHandler());

        server.setExecutor(null);
        System.out.println("容器内 Web 服务已启动: http://localhost:8080");
        server.start();
    }

    static class StaticResourceHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();

            // --- 核心修复 1: 处理空路径或斜杠 ---
            // 如果网关剥离前缀后剩下了 "" 或 "/"，都指向 index.html
            if (path == null || path.trim().isEmpty() || path.equals("/")) {
                path = "/index.html";
            }

            // --- 核心修复 2: 资源路径构造 ---
            // 确保 resourcePath 是以 /static/ 开头的绝对 Classpath 路径
            String resourcePath = "/static" + (path.startsWith("/") ? path : "/" + path);

            // 打印日志：这是排查 404 的关键，在 docker logs 中可以看到
            System.out.println("[DEBUG] 收到请求: " + exchange.getRequestURI().getPath() + " -> 映射到资源: " + resourcePath);

            // 尝试读取资源
            InputStream is = getClass().getResourceAsStream(resourcePath);

            if (is == null) {
                System.err.println("[ERROR] 找不到资源: " + resourcePath);
                String response = "404 Not Found: " + path;
                exchange.sendResponseHeaders(404, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                try {
                    byte[] content = is.readAllBytes();
                    String contentType = getContentType(path);
                    exchange.getResponseHeaders().set("Content-Type", contentType);
                    exchange.sendResponseHeaders(200, content.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(content);
                    }
                } finally {
                    is.close();
                }
            }
        }

        private String getContentType(String path) {
            String p = path.toLowerCase();
            if (p.endsWith(".html")) return "text/html; charset=utf-8";
            if (p.endsWith(".js")) return "application/javascript";
            if (p.endsWith(".css")) return "text/css";
            if (p.endsWith(".webp")) return "image/webp";
            if (p.endsWith(".png")) return "image/png";
            if (p.endsWith(".jpg") || p.endsWith(".jpeg")) return "image/jpeg";
            if (p.endsWith(".svg")) return "image/svg+xml";
            if (p.endsWith(".ico")) return "image/x-icon";
            return "application/octet-stream";
        }
    }
}