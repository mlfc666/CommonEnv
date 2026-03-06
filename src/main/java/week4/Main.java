package week4;

import common.ui.ConsoleColors;
import common.utils.JdbcUtils;
import week4.ui.Week4ConsoleMenu;
import week4.utils.Database;

public class Main {
    private static final Week4ConsoleMenu ui = new Week4ConsoleMenu();
    private static final WebServer webServer = new WebServer(ui);

    public static void main(String[] args) {
        while (true) {
            String choice = ui.selectOption();
            switch (choice) {
                case "S1" -> handleInitSqlService();
                case "S2" -> handleInitService();
                case "S3" -> handleClearAllData();
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
            webServer.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleClearAllData() {
        ui.printHeader("S3. 彻底重置数据库");
        if (JdbcUtils.resetEmbeddedDB(ui)) {
            Database.init(ui);
        }
    }
}