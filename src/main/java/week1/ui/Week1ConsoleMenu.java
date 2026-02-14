package week1.ui;

import common.ui.ConsoleColors;
import common.ui.ConsoleMenu;
import week1.enums.StoryType;

public class Week1ConsoleMenu extends ConsoleMenu {
    public String selectOption() {
        System.out.println("\n1. 创建小怪兽 | 2. 显示列表 | 3. 添加故事 | 4. 查看故事 | 5. 统计 | 0. 退出");
        System.out.print("请选择操作编号: ");
        return scanner.nextLine();
    }

    // 处理输入验证：确保年龄是数字，防止程序崩溃
    public int askForAge() {
        while (true) {
            try {
                System.out.print("年龄: ");
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                showMessage("错误：请输入有效的数字年龄！", ConsoleColors.RED);
            }
        }
    }

    public StoryType askForStoryType() {
        System.out.println("选择故事类型: 1. 搞笑 | 2. 冒险 | 3. 学习 (默认)");
        String choice = scanner.nextLine();
        return switch (choice) {
            case "1" -> StoryType.FUNNY;
            case "2" -> StoryType.ADVENTURE;
            default -> StoryType.LEARNING;
        };
    }
}