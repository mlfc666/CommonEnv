package week4.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.file.Files;

public class StaticHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // 获取并剥离路径前缀
        String path = exchange.getRequestURI().getPath();
        if (path.startsWith("/web-")) path = path.replaceFirst("^/web-[^/]+", "");
        if (path.equals("/") || path.isEmpty()) path = "/index.html";

        byte[] content;
        String contentType;

        // 优先处理用户上传的头像
        if (path.startsWith("/uploads/")) {
            File file = new File("/app" + path); // 映射物理路径 /app/uploads/...
            if (!file.exists() || file.isDirectory()) {
                sendError(exchange, 404, "File Not Found");
                return;
            }
            content = Files.readAllBytes(file.toPath());
            contentType = getMimeType(path);
        } else {
            // 处理 JAR 包内的静态资源
            try (InputStream is = getClass().getResourceAsStream("/static" + path)) {
                if (is == null) {
                    sendError(exchange, 404, "Resource Not Found");
                    return;
                }
                content = is.readAllBytes();
                contentType = getMimeType(path);
            }
        }

        // 发送响应头及二进制数据
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(200, content.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(content);
        }
    }

    private void sendError(HttpExchange exchange, int code, String msg) throws IOException {
        exchange.sendResponseHeaders(code, msg.length());
        exchange.getResponseBody().write(msg.getBytes());
        exchange.getResponseBody().close();
    }

    // 根据文件后缀名识别 MIME 类型
    private String getMimeType(String path) {
        String p = path.toLowerCase();
        if (p.endsWith(".html")) return "text/html; charset=utf-8";
        if (p.endsWith(".css")) return "text/css";
        if (p.endsWith(".js")) return "application/javascript";
        if (p.endsWith(".png")) return "image/png";
        if (p.endsWith(".jpg") || p.endsWith(".jpeg")) return "image/jpeg";
        if (p.endsWith(".webp")) return "image/webp";
        if (p.endsWith(".svg")) return "image/svg+xml";
        return "application/octet-stream";
    }
}