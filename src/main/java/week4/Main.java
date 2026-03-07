package week4;

import common.ui.ConsoleColors;
import common.utils.JdbcUtils;
import week4.app.repository.MemoRepository;
import week4.app.repository.UserRepository;
import week4.app.repository.impl.MemoRepositoryImpl;
import week4.app.repository.impl.UserRepositoryImpl;
import week4.app.services.MemoService;
import week4.app.services.UserService;
import week4.app.services.impl.MemoServiceImpl;
import week4.app.services.impl.UserServiceImpl;
import week4.ui.TablePrinter;
import week4.ui.Week4ConsoleMenu;
import week4.utils.Database;

public class Main {
    private static final Week4ConsoleMenu ui = new Week4ConsoleMenu();
    // 初始化业务层组件 (Repository & Service)
    private static final UserRepository userRepo = new UserRepositoryImpl();
    private static final MemoRepository memoRepo = new MemoRepositoryImpl();
    private static final UserService userService = new UserServiceImpl(userRepo, memoRepo);
    private static final MemoService memoService = new MemoServiceImpl(memoRepo);
    private static final WebServer webServer = new WebServer(ui);

    public static void main(String[] args) {
        while (true) {
            String choice = ui.selectOption();
            switch (choice) {
                case "S1" -> handleInitSqlService();
                case "S2" -> handleInitService();
                case "S3" -> handleShowRawTables();
                case "S4" -> handleClearAllData();
                case "0" -> {
                    JdbcUtils.stopEmbeddedDB(ui);
                    ui.showMessage("程序已退出。", ConsoleColors.YELLOW);
                    return;
                }
                default -> ui.showMessage("错误：无效选择。", ConsoleColors.RED);
            }
        }
    }

    private static void handleInitSqlService() {
        ui.printHeader("S1. 初始化数据库");
        JdbcUtils.startEmbeddedDB(ui);
        Database.init(ui);
    }

    private static void handleInitService() {
        ui.printHeader("S2. 初始化服务");
        try {
            webServer.start(userService, memoService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 处理原始系统表数据的可视化输出指令
    private static void handleShowRawTables() {
        ui.printHeader("S3. 查看系统原始表数据");
        System.out.println("1. 用户账户表 | 2. 备忘录数据表");
        String sub = ui.askForString("请选择要查看的表");

        switch (sub) {
            case "1" ->
                // 调用用户服务获取全量持久化记录
                    TablePrinter.render("Users 原始表", userService.findAll(), TablePrinter::printUser, "用户表记录为空");
            case "2" ->
                // 调用备忘录服务获取全量持久化记录
                    TablePrinter.render("Memos 原始表", memoService.findAll(), TablePrinter::printMemo, "备忘录表记录为空");
            default -> System.out.println(ConsoleColors.RED + "非法输入，操作已终止" + ConsoleColors.RESET);
        }
    }

    private static void handleClearAllData() {
        ui.printHeader("S4. 彻底重置数据库");
        if (JdbcUtils.resetEmbeddedDB(ui)) {
            Database.init(ui);
        }
    }
}