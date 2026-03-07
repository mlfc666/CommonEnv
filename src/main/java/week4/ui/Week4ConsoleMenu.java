package week4.ui;

import common.ui.ConsoleColors;
import common.ui.ConsoleMenu;

public class Week4ConsoleMenu extends ConsoleMenu {
    public String selectOption() {
        System.out.println("\n" + ConsoleColors.CYAN + "------- 备忘录管理系统 -------");
        System.out.println("S1. 初始化 MySQL 服务    | S2. 初始化 Web 服务");
        System.out.println("S3. 彻底重置数据库");
        System.out.println("0. 退出程序");
        System.out.println("---------------------------" + ConsoleColors.RESET);
        System.out.print("请选择操作编号: ");
        return scanner.nextLine().toUpperCase();
    }
}