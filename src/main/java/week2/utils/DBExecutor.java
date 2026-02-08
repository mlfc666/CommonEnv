package week2.utils;

import common.utils.ConsoleColors;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBExecutor {
    private static String extractSql(PreparedStatement stmt) {
        if (stmt == null) return "";
        String s = stmt.toString();
        int index = s.indexOf(": ");
        return index > -1 ? s.substring(index + 2) : s;
    }
    public static int executeSql(String sql) throws SQLException {
        try (Connection conn = JdbcUtils.getConnection();
             Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }

    public static int executeUpdate(String description, String sql, Object... params) {
        long start = System.currentTimeMillis();
        int count = 0;
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 循环设置参数
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
            }
            System.out.println(ConsoleColors.CYAN + "[描述]:" + ConsoleColors.RESET + ConsoleColors.BOLD + description + ConsoleColors.RESET);
            System.out.println(ConsoleColors.BLUE + "[SQL]:" + ConsoleColors.RESET + extractSql(stmt));

            count = stmt.executeUpdate();
            long end = System.currentTimeMillis();
            long duration = end - start;

            System.out.println(ConsoleColors.GREEN + "[执行成功]耗时:" + duration + "ms|影响行数:" + count + ConsoleColors.RESET);
            System.out.println(ConsoleColors.CYAN + "---------------------------------------------------------" + ConsoleColors.RESET);

        } catch (SQLException e) {
            System.err.println(ConsoleColors.RED + "[执行失败]");
            System.err.println("[原因]:" + e.getMessage() + ConsoleColors.RESET);
            System.err.println(ConsoleColors.RED + "---------------------------------------------------------" + ConsoleColors.RESET);
        }
        return count;
    }

    public static <T> List<T> executeQuery(String description, String sql, RowMapper<T> mapper, Object... params) {
        System.out.println(ConsoleColors.CYAN + "[查询描述]:" + ConsoleColors.RESET + ConsoleColors.BOLD + description + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE + "[SQL]:" + ConsoleColors.RESET + sql);

        List<T> results = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.map(rs)); // 使用映射器转换每一行
                }
            }
            System.out.println(ConsoleColors.GREEN + "[查询成功]返回行数:" + results.size() + ConsoleColors.RESET);
        } catch (SQLException e) {
            System.err.println(ConsoleColors.RED + "[查询失败][原因]:" + e.getMessage() + ConsoleColors.RESET);
        }
        return results;
    }

    @FunctionalInterface
    public interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}