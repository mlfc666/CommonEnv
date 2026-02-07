import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static final String MENU_TEXT = """
            --- 小A后台组寒假训练营总导航 ---
            当前Jar开源地址：https://github.com/mlfc666/CommonEnv
            容器平台：https://github.com/mlfc666/CommonDockerEnv
            1. 进入 week1：小怪兽故事记录系统
            2. 进入 week2：SQL练习（开发中）
            3. 进入 week3：(待开发)
            0. 退出程序
            请输入周次编号进入任务:\s""";

    // 使用 Map 映射编号和具体的任务逻辑
    private static final Map<String, Runnable> TASK_MAP = Map.of(
            "1", () -> week1.Main.main(new String[]{})
//            "2", () -> week2.Main.main(new String[]{})
    );

    public static void main(String[] args) {

        while (true) {
            System.out.print(MENU_TEXT);
            String choice = scanner.nextLine();
            // 退出
            if ("0".equals(choice)) {
                System.out.println("程序结束，感谢使用。");
                break;
            }
            // 匹配并执行跳转
            Runnable task = TASK_MAP.get(choice);
            if (task != null) {
                System.out.println("跳转中...\n");
                try {
                    task.run();
                } catch (Exception e) {
                    System.err.println("运行时发生错误: " + e.getMessage());
                }
                System.out.println("\n--- 已返回主导航界面 ---\n");
            } else {
                System.out.println("错误：无效编号，请重新输入。");
            }
        }
        scanner.close();
    }
}