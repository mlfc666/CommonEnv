package week4.utils;

import common.utils.DBInitializer;
import week4.ui.Week4ConsoleMenu;

public class Database {
    private static final String createUserTableSql = """
            CREATE TABLE IF NOT EXISTS users (
                -- 主键、自增、非空
                id INT AUTO_INCREMENT PRIMARY KEY,
            
                -- 唯一、非空（用户名）
                username VARCHAR(50) NOT NULL UNIQUE,
            
                -- 非空（加盐哈希密码）
                password VARCHAR(255) NOT NULL,
            
                -- 用户头像路径
                avatar VARCHAR(255),
            
                -- 退出登录时间戳（用于JWT撤回校验）
                logout_time BIGINT DEFAULT 0,
            
                -- 非空（账号创建时间戳）
                create_time BIGINT NOT NULL
            );
            """;

    private static final String createMemoTableSql = """
            CREATE TABLE IF NOT EXISTS memos (
                -- 主键、自增、非空
                id INT AUTO_INCREMENT PRIMARY KEY,
            
                -- 非空（关联用户表id）
                user_id INT NOT NULL,
            
                -- 非空（备忘录标题）
                title VARCHAR(100) NOT NULL,
            
                -- 详细内容
                content TEXT,
            
                -- 标签列表（以逗号分隔存储）
                tags VARCHAR(255),
            
                -- 非空（记录创建时间戳）
                create_time BIGINT NOT NULL,
            
                -- 外键约束：级联删除
                CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            );
            """;

    public static void init(Week4ConsoleMenu ui) {
        DBInitializer.registerTable("users", createUserTableSql);
        DBInitializer.registerTable("memos", createMemoTableSql);
        DBInitializer.initializer(ui);
    }
}