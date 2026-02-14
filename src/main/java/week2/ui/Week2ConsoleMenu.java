package week2.ui;

import common.ui.ConsoleColors;
import common.ui.ConsoleMenu;
import week2.enums.Gender;
import week2.enums.ScoreRemark;

import java.util.Scanner;

public class Week2ConsoleMenu implements ConsoleMenu {
    private final Scanner scanner = new Scanner(System.in);

    public void printHeader(String title) {
        System.out.println("\n" + ConsoleColors.BOLD + ConsoleColors.BLUE + "========== " + title + " ==========" + ConsoleColors.RESET);
    }

    public void showMessage(String msg, String color) {
        System.out.println(color + msg + ConsoleColors.RESET);
    }

    public String selectOption() {
        System.out.println("\n" + ConsoleColors.CYAN + "------- 选单管理系统 -------");
        System.out.println("S1. 初始化 MySQL 服务 | S2. 插入初始数据");
        System.out.println("S3. 录入学生   | S4. 录入课程   | S5. 录入成绩");
        System.out.println("S6. 查看原始表 | S7. 自定义 SQL | S8. 彻底重置数据库");
        System.out.println("---------------------------");
        System.out.println("1. 更新数据 (年龄/学分/成绩)");
        System.out.println("2. 删除数据 (成绩清理/手机号关联)");
        System.out.println("3. 基础查询 (女生/高学分/特定男生)");
        System.out.println("4. 分组查询 (班级男女比例统计)");
        System.out.println("5. 进阶查询 (老师关联/姓名模糊匹配)");
        System.out.println("0. 退出程序");
        System.out.println("---------------------------" + ConsoleColors.RESET);
        System.out.print("请选择操作编号: ");
        return scanner.nextLine().toUpperCase();
    }

    public String askForString(String label) {
        System.out.print(label + ": ");
        return scanner.nextLine();
    }

    public int askForInt(String label) {
        while (true) {
            try {
                System.out.print(label + ": ");
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                showMessage("错误：请输入有效的整数！", ConsoleColors.RED);
            }
        }
    }

    public Double askForDouble(String label) {
        while (true) {
            try {
                System.out.print(label + ": ");
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                showMessage("错误：请输入有效的数字！", ConsoleColors.RED);
            }
        }
    }

    public Gender askForGender() {
        System.out.print("选择性别 (1. 男 | 2. 女): ");
        String choice = scanner.nextLine();
        return "2".equals(choice) ? Gender.FEMALE : Gender.MALE;
    }

    public ScoreRemark askForRemark() {
        System.out.print("选择考试性质 (1. 正常考试 | 2. 补考): ");
        String choice = scanner.nextLine();
        return "2".equals(choice) ? ScoreRemark.MAKEUP : ScoreRemark.NORMAL;
    }
}