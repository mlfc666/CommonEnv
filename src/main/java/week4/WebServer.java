package week4;

import com.sun.net.httpserver.HttpServer;
import common.ui.ConsoleColors;
import common.ui.ConsoleMenu;
import week4.app.repository.MemoRepository;
import week4.app.repository.UserRepository;
import week4.app.repository.impl.MemoRepositoryImpl;
import week4.app.repository.impl.UserRepositoryImpl;
import week4.app.security.AppAuthValidator;
import week4.app.services.MemoService;
import week4.app.services.UserService;
import week4.app.services.impl.MemoServiceImpl;
import week4.app.services.impl.UserServiceImpl;
import week4.framework.core.AuthValidator;
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
        // 初始化业务层组件 (Repository & Service)
        UserRepository userRepo = new UserRepositoryImpl();
        MemoRepository memoRepo = new MemoRepositoryImpl();
        UserService userService = new UserServiceImpl(userRepo);
        MemoService memoService = new MemoServiceImpl(memoRepo);

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
        // 优先匹配 /api 路径，框架会根据扫描到的路由表进行分发
        server.createContext("/api", new DispatcherHandler());
        // 处理静态资源逻辑
        server.createContext("/", new StaticHandler());

        // 配置线程调度与启动
        server.setExecutor(null);

        this.ui.showMessage("Web 服务已启动，请访问 http://localhost:" + PORT + " ...", ConsoleColors.GREEN);

        server.start();
    }
}