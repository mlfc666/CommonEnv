package week2.ui;

import common.utils.ConsoleColors;
import week2.dto.ClassGenderCountDTO;
import week2.dto.ScoreDetailDTO;
import week2.models.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

public class TablePrinter {

    // 统一的表格渲染逻辑
    public static <T> void render(String header, List<T> data, Consumer<T> rowPrinter, String emptyMsg) {
        System.out.println("\n" + ConsoleColors.BOLD + ConsoleColors.BLUE + "=== " + header + " ===" + ConsoleColors.RESET);
        if (data == null || data.isEmpty()) {
            System.out.println(ConsoleColors.YELLOW + emptyMsg + ConsoleColors.RESET);
            return;
        }
        rowPrinter.accept(null); // 打印表头
        data.forEach(rowPrinter);
        System.out.println("------------------------------------------------------------------");
    }

    public static void printStudent(Student s) {
        if (s == null) {
            System.out.printf(ConsoleColors.CYAN + "%-4s | %-8s | %-4s | %-4s | %-12s | %-15s\n" + ConsoleColors.RESET, "ID", "姓名", "性别", "年龄", "班级", "手机号");
            return;
        }
        System.out.printf("%-4d | %-8s | %-4s | %-4d | %-12s | %-15s\n", s.getStudentId(), s.getStudentName(), s.getGender().getGender(), s.getAge(), s.getClassName(), s.getPhone() == null ? "NULL" : s.getPhone());
    }

    public static void printCourse(Course c) {
        if (c == null) {
            System.out.printf(ConsoleColors.CYAN + "%-4s | %-12s | %-10s | %-4s\n" + ConsoleColors.RESET, "ID", "课程名称", "授课老师", "学分");
            return;
        }
        System.out.printf("%-4d | %-12s | %-10s | %-4.1f\n", c.getCourseId(), c.getCourseName(), c.getTeacher(), c.getCredit());
    }

    public static void printRawScore(Score s) {
        if (s == null) {
            System.out.printf(ConsoleColors.CYAN + "%-4s | %-6s | %-6s | %-6s | %-20s | %-10s\n" + ConsoleColors.RESET, "ID", "学号", "课号", "成绩", "考试时间", "备注");
            return;
        }
        System.out.printf("%-4d | %-6d | %-6d | %-6.1f | %-20s | %-10s\n", s.getScoreId(), s.getStudentId(), s.getCourseId(), s.getScore(), s.getExamTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), s.getRemark().getRemark());
    }

    public static void printScoreDetail(ScoreDetailDTO d) {
        if (d == null) {
            System.out.printf(ConsoleColors.CYAN + "%-10s | %-15s | %-8s\n" + ConsoleColors.RESET, "学生姓名", "课程名称", "成绩");
            return;
        }
        System.out.printf("%-10s | %-15s | %-8.1f\n", d.getStudentName(), d.getCourseName(), d.getScore());
    }

    public static void printClassStat(ClassGenderCountDTO d) {
        if (d == null) {
            System.out.printf(ConsoleColors.CYAN + "%-15s | %-10s | %-10s\n" + ConsoleColors.RESET, "班级名称", "男生人数", "女生人数");
            return;
        }
        System.out.printf("%-15s | %-10d | %-10d\n", d.getClassName(), d.getMaleCount(), d.getFemaleCount());
    }
}