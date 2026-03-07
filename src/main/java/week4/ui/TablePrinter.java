package week4.ui;

import common.ui.ConsoleColors;
import week4.app.models.Memo;
import week4.app.models.User;

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

    // 打印用户表的表头或数据行
    public static void printUser(User u) {
        if (u == null) {
            System.out.printf(ConsoleColors.CYAN + "%-4s | %-12s | %-30s | %-15s | %-15s\n" + ConsoleColors.RESET,
                    "ID", "用户名", "头像路径", "登出时间戳", "创建时间戳");
            return;
        }
        System.out.printf("%-4d | %-12s | %-30s | %-15d | %-15d\n",
                u.getId(),
                u.getUsername(),
                u.getAvatar() == null ? "NULL" : u.getAvatar(),
                u.getLogoutTime(),
                u.getCreateTime());
    }

    // 打印备忘录表的表头或数据行并对长内容进行截断处理
    public static void printMemo(Memo m) {
        if (m == null) {
            System.out.printf(ConsoleColors.CYAN + "%-4s | %-6s | %-15s | %-25s | %-15s | %-15s\n" + ConsoleColors.RESET,
                    "ID", "UID", "标题", "内容摘要", "标签", "创建时间");
            return;
        }
        String tagsStr = m.getTags() != null ? String.join(",", m.getTags()) : "NONE";
        // 截断过长的正文内容以维持表格整齐
        String summary = m.getContent().replace("\n", " ");
        if (summary.length() > 20) summary = summary.substring(0, 17) + "...";

        System.out.printf("%-4d | %-6d | %-15s | %-25s | %-15s | %-15d\n",
                m.getId(),
                m.getUserId(),
                m.getTitle(),
                summary,
                tagsStr,
                m.getCreatTime());
    }
}
