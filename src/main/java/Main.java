import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("--- 小A后台组寒假训练营总导航 ---");
            System.out.println("1. 进入 week1：小怪兽故事记录系统");
            System.out.println("2. 进入 week2：(待开发)");
            System.out.println("0. 退出程序");
            System.out.print("请输入周次编号进入任务: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.println("跳转中...\n");
                    week1.Main.main(new String[]{});
                }
                case "2" -> System.out.println("提示：第二周任务尚未开启。");
                case "0" -> {
                    System.out.println("程序结束，感谢使用。");
                    return;
                }
                default -> System.out.println("错误：无效编号，请重新输入。");
            }
            System.out.println("\n--- 已返回主导航界面 ---\n");
        }
    }
}