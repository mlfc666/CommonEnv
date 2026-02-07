package week2.utils;

import common.utils.ConsoleColors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBExecutor {
    /**
     * 执行 SQL 更新操作（增、删、改、建表）
     *
     * @param description 业务步骤描述
     * @param sql         带有 ? 占位符的 SQL 语句
     * @param params      对应的参数列表
     */
    @SuppressWarnings("SqlInjection")
    public static int executeUpdate(String description, String sql, Object... params) {
        System.out.println(ConsoleColors.CYAN + "[描述]:" + ConsoleColors.RESET + ConsoleColors.BOLD + description + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE + "[SQL]:" + ConsoleColors.RESET + sql);

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

    // 简单的内部接口，用于处理结果集转换
    @FunctionalInterface
    public interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}