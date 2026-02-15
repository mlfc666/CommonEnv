package week3.ui;

import common.ui.ConsoleColors;
import week3.entity.Score;
import week3.entity.Student;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

public class TablePrinter {

    public static <T> void render(String header, List<T> data, Consumer<T> rowPrinter, String emptyMsg) {
        System.out.println("\n" + ConsoleColors.BOLD + ConsoleColors.BLUE + "=== " + header + " ===" + ConsoleColors.RESET);
        if (data == null || data.isEmpty()) {
            System.out.println(ConsoleColors.YELLOW + emptyMsg + ConsoleColors.RESET);
            return;
        }
        rowPrinter.accept(null);
        data.forEach(rowPrinter);
        System.out.println("------------------------------------------------------------------");
    }

    public static void printStudent(Student s) {
        if (s == null) {
            System.out.printf(ConsoleColors.CYAN + "%-4s | %-12s | %-8s | %-4s | %-6s | %-20s\n" + ConsoleColors.RESET,
                    "ID", "学号", "姓名", "性别", "年龄", "创建时间");
            return;
        }
        System.out.printf("%-4d | %-12s | %-8s | %-4s | %-6d | %-20s\n",
                s.getId(), s.getStudentNo(), s.getName(), s.getGender().getGender(), s.getAge(),
                s.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    public static void printScore(Score s) {
        if (s == null) {
            System.out.printf(ConsoleColors.CYAN + "%-4s | %-10s | %-12s | %-8s | %-20s\n" + ConsoleColors.RESET,
                    "ID", "学生ID", "科目", "成绩", "考试时间");
            return;
        }
        System.out.printf("%-4d | %-10d | %-12s | %-8.1f | %-20s\n",
                s.getId(), s.getStudentId(), s.getSubject(), s.getScore(),
                s.getExamTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
}