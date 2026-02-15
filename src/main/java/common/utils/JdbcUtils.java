package common.utils;

import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import common.ui.ConsoleColors;
import common.ui.ConsoleMenu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtils {
    // 优先读取环境变量，适配 Docker 部署
    private static final String HOST = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
    private static final String PORT = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "3306";
    public static final String DATABASE = "commonenv";
    // 添加时区和 SSL 配置
    private static final String BASE_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" +
            "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8";
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE +
            "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8";
    private static final String USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
    private static final String PASS = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS") : "root";

    private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();
    private static DB embeddedDB;

    static {
        try {
            // MySQL 8.0+ 的驱动类名
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(ConsoleColors.RED + "[驱动加载失败]:" + e.getMessage() + ConsoleColors.RESET);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = threadLocal.get();
        // 如果当前线程没有连接，或者连接已关闭，则创建新连接并存入 ThreadLocal
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(URL, USER, PASS);
            threadLocal.set(conn);
        }
        return conn;
    }

    public static void closeConnection() {
        Connection conn = threadLocal.get();
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println(ConsoleColors.RED + "[关闭连接失败]:" + e.getMessage() + ConsoleColors.RESET);
            } finally {
                threadLocal.remove();
            }
        }
    }

    public static Connection getBaseConnection() throws SQLException {
        return DriverManager.getConnection(BASE_URL, USER, PASS);
    }

    public static void startEmbeddedDB(ConsoleMenu ui) {
        try {
            if (embeddedDB == null) {
                DBConfigurationBuilder configBuilder = DBConfigurationBuilder.newBuilder();
                configBuilder.setPort(3306);
                configBuilder.addArg("--user=root");
                embeddedDB = DB.newEmbeddedDB(configBuilder.build());
                embeddedDB.start();

                ui.showMessage("MySQL 服务启动成功。", ConsoleColors.GREEN);
            }
        } catch (Exception e) {
            ui.showMessage("启动失败: " + e.getMessage(), ConsoleColors.RED);
        }
    }

    public static void stopEmbeddedDB(ConsoleMenu ui) {
        try {
            if (embeddedDB != null) embeddedDB.stop();
            ui.showMessage("数据库已关闭。", ConsoleColors.GREEN);
        } catch (Exception e) {
            ui.showMessage("数据库关闭失败: " + e.getMessage(), ConsoleColors.RED);
        }
    }

    public static boolean resetEmbeddedDB(ConsoleMenu ui) {
        ui.printHeader("准备重置数据库");
        if (!ui.askForString("确定删除并重建 " + JdbcUtils.DATABASE + " 数据库吗？(y/n)").equalsIgnoreCase("y"))
            return false;

        try (Connection conn = JdbcUtils.getBaseConnection();
             Statement stmt = conn.createStatement()) {

            ui.showMessage("正在销毁旧数据库...", ConsoleColors.YELLOW);
            stmt.executeUpdate("DROP DATABASE IF EXISTS " + JdbcUtils.DATABASE);

            ui.showMessage("正在创建新数据库...", ConsoleColors.GREEN);
            stmt.executeUpdate("CREATE DATABASE " + JdbcUtils.DATABASE + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");

            ui.showMessage("数据库重建成功！", ConsoleColors.GREEN);
            return true;
        } catch (SQLException e) {
            ui.showMessage("操作失败: " + e.getMessage(), ConsoleColors.RED);
        }
        return false;
    }
}