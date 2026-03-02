package week4;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;

/**
 * 容器内极简 Web 服务器
 */
public class WebServer {

    public static void start() throws Exception {
        // 监听容器内的 8080 端口
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // 映射根路径处理
        server.createContext("/", new StaticResourceHandler());

        // 使用默认调度器
        server.setExecutor(null);
        System.out.println("[INFO] 容器内 Web 服务已启动，监听端口: 8080");
        server.start();
    }

    static class StaticResourceHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // 获取请求路径
            String path = exchange.getRequestURI().getPath();

            // --- 双重保险：如果网关没剥离前缀，容器自己再剥离一次 ---
            if (path != null && path.startsWith("/web-")) {
                // 正则匹配并替换掉开头的 /web-xxxx 部分
                path = path.replaceFirst("^/web-[^/]+", "");
            }

            // --- 路径映射：如果是根路径或空，映射到 index.html ---
            if (path == null || path.trim().isEmpty() || path.equals("/")) {
                path = "/index.html";
            }

            // --- 构造 Classpath 资源路径 ---
            // 最终效果类似 "/static/index.html" 或 "/static/assets/app.js"
            String resourcePath = "/static" + (path.startsWith("/") ? path : "/" + path);

            // 打印调试日志，方便在 docker logs 中排查
            System.out.println("[DEBUG] 请求路径: " + exchange.getRequestURI().getPath() + " -> 查找资源: " + resourcePath);

            // 从 Classpath (JAR包内部) 读取文件
            InputStream is = getClass().getResourceAsStream(resourcePath);

            if (is == null) {
                System.err.println("[ERROR] 资源不存在: " + resourcePath);
                String response = "404 Not Found (CommonEnv)";
                exchange.sendResponseHeaders(404, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                try {
                    // 读取文件内容
                    byte[] content = is.readAllBytes();

                    // 设置响应头（MIME类型）
                    String contentType = getContentType(path);
                    exchange.getResponseHeaders().set("Content-Type", contentType);

                    // 发送 200 响应
                    exchange.sendResponseHeaders(200, content.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(content);
                    }
                } catch (Exception e) {
                    System.err.println("[ERROR] 读取资源失败: " + e.getMessage());
                } finally {
                    is.close();
                }
            }
        }

        /**
         * 简单的 MIME 类型识别逻辑
         */
        private String getContentType(String path) {
            String p = path.toLowerCase();
            if (p.endsWith(".html")) return "text/html; charset=utf-8";
            if (p.endsWith(".js")) return "application/javascript; charset=utf-8";
            if (p.endsWith(".css")) return "text/css; charset=utf-8";
            if (p.endsWith(".webp")) return "image/webp";
            if (p.endsWith(".png")) return "image/png";
            if (p.endsWith(".jpg") || p.endsWith(".jpeg")) return "image/jpeg";
            if (p.endsWith(".svg")) return "image/svg+xml";
            if (p.endsWith(".ico")) return "image/x-icon";
            if (p.endsWith(".json")) return "application/json; charset=utf-8";
            return "application/octet-stream";
        }
    }
}