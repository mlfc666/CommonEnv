package week4.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;

public class StaticHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.startsWith("/web-")) path = path.replaceFirst("^/web-[^/]+", "");
        if (path.equals("/") || path.isEmpty()) path = "/index.html";

        InputStream is = getClass().getResourceAsStream("/static" + path); // 从根路径读取静态资源
        if (is == null) {
            exchange.sendResponseHeaders(404, 0);
            exchange.close();
            return;
        }

        byte[] content = is.readAllBytes();
        exchange.sendResponseHeaders(200, content.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(content); }
        is.close();
    }
}