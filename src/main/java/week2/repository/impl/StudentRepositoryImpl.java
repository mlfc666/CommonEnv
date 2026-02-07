package week2.repository.impl;

import week2.enums.Gender;
import week2.models.Student;
import week2.repository.StudentRepository;
import week2.utils.DBExecutor;

import java.util.List;

public class StudentRepositoryImpl implements StudentRepository {

    @Override
    public Student insert(Student student) {
        String sql = """
                INSERT INTO students (student_name, gender, age, class, phone) VALUES (?, ?, ?, ?, ?)
                """;
        // 注意：gender.name() 将枚举转为 '男'/'女' 字符串存入数据库
        DBExecutor.executeUpdate(
                "插入学生:" + student.getStudentName(),
                sql,
                student.getStudentName(),
                student.getGender().name(),
                student.getAge(),
                student.getClassName(),
                student.getPhone()
        );
        return student;
    }

    @Override
    public void update(Student student) {
        String sql = """
                UPDATE students SET student_name = ?, gender = ?, age = ?, class = ?, phone = ? WHERE student_id = ?
                """;
        DBExecutor.executeUpdate(
                "更新学生信息-ID:" + student.getStudentId(),
                sql,
                student.getStudentName(),
                student.getGender().name(),
                student.getAge(),
                student.getClassName(),
                student.getPhone(),
                student.getStudentId()
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
                "查询性别为" + gender.name() + "的学生并排序",
                sql,
                rs -> {
                    Student s = new Student();
                    s.setStudentId(rs.getInt("student_id"));
                    s.setStudentName(rs.getString("student_name"));
                    s.setGender(Gender.valueOf(rs.getString("gender")));
                    s.setAge(rs.getInt("age"));
                    s.setClassName(rs.getString("class"));
                    s.setPhone(rs.getString("phone"));
                    return s;
                },
                gender.name()
        );
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
                rs -> {
                    Student s = new Student();
                    s.setStudentId(rs.getInt("student_id"));
                    s.setStudentName(rs.getString("student_name"));
                    s.setGender(Gender.valueOf(rs.getString("gender")));
                    s.setAge(rs.getInt("age"));
                    s.setClassName(rs.getString("class"));
                    s.setPhone(rs.getString("phone"));
                    return s;
                },
                className,
                minAge,
                maxAge,
                gender.name()
        );
    }
}