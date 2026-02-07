package week2.utils;

import common.utils.ConsoleColors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtils {
    // 优先读取环境变量，适配 Docker 部署
    private static final String HOST = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
    private static final String PORT = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "3306";
    private static final String DATABASE = "week2";

    // 添加时区和 SSL 配置
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE +
            "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8";

    private static final String USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
    private static final String PASS = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS") : "root";

    static {
        try {
            // MySQL 8.0+ 的驱动类名
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(ConsoleColors.RED + "[驱动加载失败]:" + e.getMessage() + ConsoleColors.RESET);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println(ConsoleColors.RED + "[结果集关闭异常]:" + e.getMessage() + ConsoleColors.RESET);
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println(ConsoleColors.RED + "[语句对象关闭异常]:" + e.getMessage() + ConsoleColors.RESET);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println(ConsoleColors.RED + "[数据库连接关闭异常]:" + e.getMessage() + ConsoleColors.RESET);
            }
        }
    }
}