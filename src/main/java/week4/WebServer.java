package week4;

import com.sun.net.httpserver.HttpServer;
import common.ui.ConsoleColors;
import common.ui.ConsoleMenu;
import week4.app.security.AppAuthValidator;
import week4.app.services.MemoService;
import week4.app.services.UserService;
import week4.framework.core.AuthValidator;
import week4.framework.core.BeanContainer;
import week4.framework.core.DispatcherHandler;
import week4.framework.core.RouteScanner;
import week4.handlers.MasterHandler;

import java.net.InetSocketAddress;

public class WebServer {
    // 默认端口 8080
    private static final int PORT = 8080;
    private final ConsoleMenu ui;

    public WebServer(ConsoleMenu ui) {
        this.ui = ui;
    }

    public void start(UserService userService, MemoService memoService) throws Exception {
        // 注册到 Bean 容器，供框架注入
        BeanContainer.register(UserService.class, userService);
        BeanContainer.register(MemoService.class, memoService);

        // 初始化并配置自定义验证器
        AuthValidator validator = new AppAuthValidator(userService);
        // 必须在服务器启动前注入，否则请求进来时 authValidator 为 null
        DispatcherHandler.setAuthValidator(validator);

        // 执行反射扫描，构建路由表
        // 路由表是静态存储的，必须在注册 Context 之前扫描完成
        RouteScanner.scan("week4.app.controllers");

        // 创建服务器实例并绑定端口
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // 注册路由处理器 (Context Mapping)
        server.createContext("/", new MasterHandler());

        // 配置线程调度与启动
        server.setExecutor(null);

        this.ui.showMessage("Web 服务已启动，请访问 http://java.mlfc.moe/web-容器sid/\n注：容器名称中env-后面就是sid", ConsoleColors.GREEN);

        server.start();
    }
}