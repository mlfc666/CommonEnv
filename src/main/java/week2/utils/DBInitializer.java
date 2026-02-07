package week2.utils;

public class DBInitializer {
    private static final String createStudentTableSql = """
                CREATE TABLE students (
                -- 主键、自增
                student_id INT AUTO_INCREMENT PRIMARY KEY,
            
                -- 非空
                student_name VARCHAR(30) NOT NULL,
            
                -- 非空、检查约束
                gender ENUM('男', '女') NOT NULL,
            
                -- 非空、取值范围 15-25
                age INT NOT NULL CHECK (age BETWEEN 15 AND 25),
            
                -- 非空
                class VARCHAR(20) NOT NULL,
            
                -- 唯一、允许为空
                phone VARCHAR(20) UNIQUE,
            
                -- 默认当前时间
                create_time DATETIME DEFAULT CURRENT_TIMESTAMP
            );
            """;
    private static final String createCourseTableSql = """
            CREATE TABLE courses (
                -- 主键、自增
                course_id INT AUTO_INCREMENT PRIMARY KEY,
            
                -- 非空
                course_name VARCHAR(20) NOT NULL UNIQUE,
            
                -- 非空
                teacher VARCHAR(30) NOT NULL,
            
                -- 非空、取值范围 1-5
                credit DECIMAL(2,1) NOT NULL CHECK (credit BETWEEN 1 AND 5)
            );
            """;
    private static final String createScoreTableSql = """
            CREATE TABLE scores (
                -- 主键、自增
                score_id INT AUTO_INCREMENT PRIMARY KEY,
            
                -- 非空、外键关联 students.student_id (级联删除)
                student_id INT NOT NULL,
            
                -- 非空、外键关联 courses.course_id (级联删除)
                course_id INT NOT NULL,
            
                -- 非空、取值 0-100
                score DECIMAL(5, 2) NOT NULL CHECK (score BETWEEN 0 AND 100),
            
                -- 非空
                exam_time DATETIME NOT NULL,
            
                -- 允许为空
                remark VARCHAR(4),
            
                -- 设置外键约束
                CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
            
                CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
            );
            """;
    public static void initializer() {
        DBExecutor.executeUpdate("创建学生表", createStudentTableSql);
        DBExecutor.executeUpdate("创建课程表", createCourseTableSql);
        DBExecutor.executeUpdate("创建成绩表", createScoreTableSql);
    }
}
