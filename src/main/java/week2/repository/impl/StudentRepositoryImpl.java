package week2.repository.impl;

import week2.dto.ClassGenderCountDTO;
import week2.enums.Gender;
import week2.models.Student;
import week2.repository.StudentRepository;
import week2.utils.DBExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StudentRepositoryImpl implements StudentRepository {

    @Override
    public Student insert(Student student) {
        String sql = """
                INSERT INTO students (student_name, gender, age, class, phone) VALUES (?, ?, ?, ?, ?)
                """;
        DBExecutor.executeUpdate(
                "插入学生:" + student.getStudentName(),
                sql,
                student.getStudentName(),
                student.getGender().getGender(),
                student.getAge(),
                student.getClassName(),
                student.getPhone()
        );
        return student;
    }

    @Override
    public List<Student> findAll() {
        String sql = """
                SELECT student_id, student_name, gender, age, class, phone FROM students
                """;
        return DBExecutor.executeQuery(
                "查询所有学生:",
                sql,
                rs -> {
                    Student student = new Student();
                    student.setStudentId(rs.getInt("student_id"));
                    student.setStudentName(rs.getString("student_name"));
                    student.setGender(Gender.fromDesc(rs.getString("gender")));
                    student.setAge(rs.getInt("age"));
                    student.setClassName(rs.getString("class"));
                    student.setPhone(rs.getString("phone"));
                    return student;
                }
        );
    }

    @Override
    public int incrementAgeByClassName(String className) {
        String sql = """
                UPDATE students SET age = age + 1 WHERE class = ?
                """;

        return DBExecutor.executeUpdate(
                "全班学生年龄+1-班级:" + className,
                sql,
                className
        );
    }

    @Override
    public List<Student> findByGenderOrderByClassNameAscAgeDesc(Gender gender) {
        // 对应需求：按性别筛选，班级升序，年龄降序
        String sql = """
                SELECT * FROM students WHERE gender = ? ORDER BY class ASC, age DESC
                """;

        return DBExecutor.executeQuery(
                "查询性别为" + gender.getGender() + "的学生并排序",
                sql,
                this::mapRowToStudent,
                gender.getGender()
        );
    }

    private Student mapRowToStudent(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setStudentId(rs.getInt("student_id"));
        s.setStudentName(rs.getString("student_name"));
        String genderStr = rs.getString("gender");
        if (genderStr != null) {
            try {
                s.setGender(Gender.fromDesc(genderStr));
            } catch (IllegalArgumentException e) {
                s.setGender(null);
            }
        }
        int age = rs.getInt("age");
        s.setAge(rs.wasNull() ? 0 : age);
        s.setClassName(rs.getString("class"));
        s.setPhone(rs.getString("phone"));

        return s;
    }

    @Override
    public List<Student> findFirst2ByClassNameAndAgeBetweenAndGender(String className, int minAge, int maxAge, Gender gender) {
        // 对应需求：特定班级、年龄区间、性别，取前2名
        String sql = """
                SELECT * FROM students  WHERE class = ? AND age BETWEEN ? AND ? AND gender = ? LIMIT 2
                """;

        return DBExecutor.executeQuery(
                "查询班级" + className + "中年龄在" + minAge + "-" + maxAge + "之间的前2名" + gender.name() + "生",
                sql,
                this::mapRowToStudent,
                className,
                minAge,
                maxAge,
                gender.getGender()
        );
    }

    @Override
    public List<ClassGenderCountDTO> countGenderByClass() {
        // 使用 CASE WHEN 进行行转列统计
        String sql = """
                SELECT class,
                SUM(CASE WHEN gender = '男' THEN 1 ELSE 0 END) AS male_count,
                SUM(CASE WHEN gender = '女' THEN 1 ELSE 0 END) AS female_count
                FROM students
                GROUP BY class
                """;

        return DBExecutor.executeQuery(
                "按班级统计男女比例",
                sql,
                rs -> {
                    ClassGenderCountDTO dto = new ClassGenderCountDTO();
                    // 注意：数据库字段是 class
                    dto.setClassName(rs.getString("class"));
                    dto.setMaleCount(rs.getInt("male_count"));
                    dto.setFemaleCount(rs.getInt("female_count"));
                    return dto;
                }
        );
    }
}