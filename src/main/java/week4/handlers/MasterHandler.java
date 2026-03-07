package week4.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import week4.framework.core.DispatcherHandler;
import java.io.IOException;

public class MasterHandler implements HttpHandler {
    private final DispatcherHandler apiHandler = new DispatcherHandler();
    private final StaticHandler staticHandler = new StaticHandler();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        // 无论原始路径带了什么 web-xxx 前缀，只要剥离后包含 /api/，全部交给 API 处理
        String strippedPath = path.startsWith("/web-") ? path.replaceFirst("^/web-[^/]+", "") : path;

        if (strippedPath.startsWith("/api/")) {
            apiHandler.handle(exchange);
        } else {
            staticHandler.handle(exchange);
        }
    }
}