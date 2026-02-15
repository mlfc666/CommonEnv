package common.utils;

import common.ui.ConsoleColors;

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
        Connection conn = JdbcUtils.getConnection();
        try (Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }

    public static int executeUpdate(String description, String sql, Object... params) {
        long start = System.currentTimeMillis();
        int count;
        try {
            Connection conn = JdbcUtils.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                if (params != null && params.length > 0) {
                    for (int i = 0; i < params.length; i++) {
                        stmt.setObject(i + 1, params[i]);
                    }
                }
                System.out.println(ConsoleColors.CYAN + "[描述]:" + ConsoleColors.RESET + ConsoleColors.BOLD + description + ConsoleColors.RESET);
                System.out.println(ConsoleColors.BLUE + "[SQL]:" + ConsoleColors.RESET + extractSql(stmt));

                count = stmt.executeUpdate();
                long end = System.currentTimeMillis();

                System.out.println(ConsoleColors.GREEN + "[执行成功]耗时:" + (end - start) + "ms|影响行数:" + count + ConsoleColors.RESET);
                System.out.println(ConsoleColors.CYAN + "---------------------------------------------------------" + ConsoleColors.RESET);
            }
        } catch (SQLException e) {
            System.err.println(ConsoleColors.RED + "[执行失败][原因]:" + e.getMessage() + ConsoleColors.RESET);
            throw new RuntimeException(e);
        }
        return count;
    }

    public static <T> List<T> executeQuery(String description, String sql, RowMapper<T> mapper, Object... params) {
        List<T> results = new ArrayList<>();
        try {
            Connection conn = JdbcUtils.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        pstmt.setObject(i + 1, params[i]);
                    }
                }

                System.out.println(ConsoleColors.CYAN + "[查询描述]:" + ConsoleColors.RESET + ConsoleColors.BOLD + description + ConsoleColors.RESET);
                System.out.println(ConsoleColors.BLUE + "[SQL]:" + ConsoleColors.RESET + extractSql(pstmt));

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        results.add(mapper.map(rs));
                    }
                }
                System.out.println(ConsoleColors.GREEN + "[查询成功]返回行数:" + results.size() + ConsoleColors.RESET);
            }
        } catch (SQLException e) {
            System.err.println(ConsoleColors.RED + "[查询失败][原因]:" + e.getMessage() + ConsoleColors.RESET);
        }
        return results;
    }

    public static int[] executeBatch(String description, String sql, List<Object[]> paramsList) {
        long start = System.currentTimeMillis();
        int[] results;

        try {
            Connection conn = JdbcUtils.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                boolean originalAutoCommit = conn.getAutoCommit();
                conn.setAutoCommit(false);

                System.out.println(ConsoleColors.CYAN + "[批处理描述]:" + ConsoleColors.RESET + ConsoleColors.BOLD + description + ConsoleColors.RESET);
                System.out.println(ConsoleColors.BLUE + "[SQL模板]:" + ConsoleColors.RESET + sql);

                if (paramsList != null && !paramsList.isEmpty()) {
                    for (Object[] params : paramsList) {
                        if (params != null) {
                            for (int i = 0; i < params.length; i++) {
                                stmt.setObject(i + 1, params[i]);
                            }
                            System.out.println(ConsoleColors.PURPLE + "  -> [准备执行]: " + ConsoleColors.RESET + extractSql(stmt));
                            stmt.addBatch();
                        }
                    }
                }

                results = stmt.executeBatch();
                conn.commit();
                conn.setAutoCommit(originalAutoCommit);

                long end = System.currentTimeMillis();
                System.out.println(ConsoleColors.GREEN + "[批处理成功]总记录数:" + paramsList.size() + "|耗时:" + (end - start) + "ms" + ConsoleColors.RESET);
            }
        } catch (SQLException e) {
            try { JdbcUtils.getConnection().rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println(ConsoleColors.RED + "[批处理失败][原因]:" + e.getMessage() + ConsoleColors.RESET);
            throw new RuntimeException(e);
        }
        return results;
    }

    @FunctionalInterface
    public interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}