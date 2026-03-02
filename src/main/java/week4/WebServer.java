package week4;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;

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
            if (path.equals("/")) path = "/index.html";

            // 关键点：从 resources/static 目录下读取资源
            // 注意这里前面要加 /static
            String resourcePath = "/static" + (path.startsWith("/") ? "" : "/") + path;
            InputStream is = getClass().getResourceAsStream(resourcePath);

            if (is == null) {
                // 如果找不到文件，返回 404
                String response = "404 Not Found";
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // 设置正确的 Content-Type (防止浏览器不识别 CSS/JS)
                String contentType = getContentType(path);
                exchange.getResponseHeaders().set("Content-Type", contentType);

                byte[] content = is.readAllBytes();
                exchange.sendResponseHeaders(200, content.length);
                OutputStream os = exchange.getResponseBody();
                os.write(content);
                os.close();
                is.close();
            }
        }

        // 简单的 MIME 类型识别
        private String getContentType(String path) {
            if (path.endsWith(".html")) return "text/html; charset=utf-8";
            if (path.endsWith(".js")) return "application/javascript";
            if (path.endsWith(".css")) return "text/css";
            if (path.endsWith(".webp")) return "image/webp";
            if (path.endsWith(".png")) return "image/png";
            if (path.endsWith(".svg")) return "image/svg+xml";
            return "application/octet-stream";
        }
    }
}