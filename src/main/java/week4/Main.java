package week4;

import common.ui.ConsoleColors;
import common.utils.JdbcUtils;
import week4.ui.Week4ConsoleMenu;

public class Main {
    private static final Week4ConsoleMenu ui = new Week4ConsoleMenu();
    private static final WebServer webServer = new WebServer(ui);
    public static void main(String[] args) {
        while (true) {
            String choice = ui.selectOption();
            switch (choice) {
                case "S3" -> handleInitService();
                case "0" -> {
                    JdbcUtils.stopEmbeddedDB(ui);
                    ui.showMessage("程序已退出。", ConsoleColors.YELLOW);
                    return;
                }
                default -> ui.showMessage("错误：无效选择。", ConsoleColors.RED);
            }
        }
    }

    private static void handleInitService() {
        try {
            webServer.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}