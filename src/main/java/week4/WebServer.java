package week4;

import com.sun.net.httpserver.HttpServer;
import common.ui.ConsoleColors;
import common.ui.ConsoleMenu;
import week4.framework.core.DispatcherHandler;
import week4.framework.core.RouteScanner;
import week4.handlers.StaticHandler;

import java.net.InetSocketAddress;

public class WebServer {
    // 默认端口 8080
    private static final int PORT = 8080;
    private final ConsoleMenu ui;

    public WebServer(ConsoleMenu ui) {
        this.ui = ui;
    }

    public void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        // 扫描并注册控制器
        RouteScanner.scan("week4.app.controllers");
        // 注册处理器
        server.createContext("/api", new DispatcherHandler()); // 优先匹配 API
        server.createContext("/", new StaticHandler()); // 其他全部走静态资源

        server.setExecutor(null);
        this.ui.showMessage("Web 服务已启动，请访问 http://localhost:" + PORT + " ...", ConsoleColors.GREEN);
        server.start();
    }
}