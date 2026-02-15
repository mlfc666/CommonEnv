package week3.ui;

import common.ui.ConsoleColors;
import common.ui.ConsoleMenu;
import week3.enums.Gender;

public class Week3ConsoleMenu extends ConsoleMenu {
    public String selectOption() {
        System.out.println("\n" + ConsoleColors.CYAN + "------- 学生成绩管理系统 -------");
        System.out.println("S1. 初始化 MySQL 服务    | S2. 彻底重置数据库");
        System.out.println("S3. 插入初始数据        | S4. 批量新增学生");
        System.out.println("S5. 新增单条学生数据    | S6. 新增单条学生成绩");
        System.out.println("S7. 查看原始表");
        System.out.println("---------------------------");
        System.out.println("1. 根据id删除学生数据及其所有成绩");
        System.out.println("2. 条件分页查询学生");
        System.out.println("3. 根据id修改学生的姓名和年龄");
        System.out.println("4. 根据id查询单个学生数据");
        System.out.println("5. 根据姓名模糊查询学生数据");
        System.out.println("6. 根据学号查询指定学生的所有科目成绩");
        System.out.println("0. 退出程序");
        System.out.println("---------------------------" + ConsoleColors.RESET);
        System.out.print("请选择操作编号: ");
        return scanner.nextLine().toUpperCase();
    }

    public Gender askForGender() {
        System.out.print("选择性别 (1. 男 | 2. 女 ): ");
        String choice = scanner.nextLine();
        if ("1".equals(choice)) return Gender.MALE;
        if ("2".equals(choice)) return Gender.FEMALE;
        return null;
    }
}