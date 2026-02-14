package week3.utils;

import common.utils.DBInitializer;
import week3.ui.Week3ConsoleMenu;

public class Database {
    private static final String createStudentTableSql = """
            CREATE TABLE student (
                -- 主键、自增、非空
                id INT AUTO_INCREMENT PRIMARY KEY,
            
                -- 非空（姓名）
                name VARCHAR(20) NOT NULL,
            
                -- 非空（男/女）
                gender CHAR(1) NOT NULL,
            
                -- 非空（学生年龄）
                age TINYINT NOT NULL,
            
                -- 唯一、非空（学号10位）
                student_no VARCHAR(10) NOT NULL UNIQUE,
            
                -- 非空（创建时间，默认当前时间）
                create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
            );
            """;

    private static final String createScoreTableSql = """
            CREATE TABLE score (
                -- 主键、自增、非空
                id INT AUTO_INCREMENT PRIMARY KEY,
            
                -- 非空（与student表id对应）
                student_id INT NOT NULL,
            
                -- 非空（科目名称）
                subject VARCHAR(50) NOT NULL,
            
                -- 非空（成绩，满分100）
                score DECIMAL(5,2) NOT NULL,
            
                -- 非空（考试日期）
                exam_time DATE NOT NULL,
            
                -- 外键约束：级联删除
                CONSTRAINT fk_student_id FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
            );
            """;

    public static void init(Week3ConsoleMenu ui) {
        DBInitializer.registerTable("student", createStudentTableSql);
        DBInitializer.registerTable("score", createScoreTableSql);
        DBInitializer.initializer(ui);
    }
}