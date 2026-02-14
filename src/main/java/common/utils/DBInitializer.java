package common.utils;


import common.ui.ConsoleColors;
import common.ui.ConsoleMenu;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;


public class DBInitializer {
    private static final LinkedHashMap<String, String> TABLE_SCHEMAS = new LinkedHashMap<>();

    public static void initializer(ConsoleMenu ui) {
        // 确保数据库存在
        ensureDatabaseExists(ui);

        // 遍历执行所有注册过的表 SQL
        TABLE_SCHEMAS.forEach((tableName, sql) -> {
            ui.showMessage("正在初始化: " + tableName, ConsoleColors.CYAN);
            DBExecutor.executeUpdate(tableName, sql);
        });
    }

    // 注册表
    public static void registerTable(String displayName, String createSql) {
        TABLE_SCHEMAS.put(displayName, createSql);
    }

    private static void ensureDatabaseExists(ConsoleMenu ui) {
        try (Connection conn = JdbcUtils.getBaseConnection();
             Statement stmt = conn.createStatement()) {
            ui.showMessage("正在检测/创建数据库...", ConsoleColors.PURPLE);
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + JdbcUtils.DATABASE + " CHARACTER SET utf8mb4");
        } catch (SQLException e) {
            ui.showMessage("数据库创建失败: " + e.getMessage(), ConsoleColors.RED);
        }
    }
}