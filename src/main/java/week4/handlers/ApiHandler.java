package week4.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

public class ApiHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // 这里可以根据 path 分发给不同的 Service
        String response = "{\"status\":\"ok\", \"data\":\"Hello from Container API\"}";
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}