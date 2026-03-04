package week4.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import week4.utils.MimeUtils;
import week4.utils.PathUtils;
import java.io.*;

public class StaticHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String rawPath = exchange.getRequestURI().getPath();
        String path = PathUtils.sanitizePath(rawPath);
        String resourcePath = "/static" + (path.startsWith("/") ? path : "/" + path);

        InputStream is = getClass().getResourceAsStream(resourcePath);
        if (is == null) {
            sendError(exchange, 404, "Resource Not Found: " + path);
            return;
        }

        byte[] content = is.readAllBytes();
        exchange.getResponseHeaders().set("Content-Type", MimeUtils.getContentType(path));
        exchange.sendResponseHeaders(200, content.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(content);
        }
        is.close();
    }

    private void sendError(HttpExchange exchange, int code, String msg) throws IOException {
        exchange.sendResponseHeaders(code, msg.length());
        exchange.getResponseBody().write(msg.getBytes());
        exchange.getResponseBody().close();
    }
}