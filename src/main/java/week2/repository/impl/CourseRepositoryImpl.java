package week2.repository.impl;

import week2.models.Course;
import week2.repository.CourseRepository;
import week2.utils.DBExecutor;

import java.util.List;
import java.util.Optional;

public class CourseRepositoryImpl implements CourseRepository {

    @Override
    public Course insert(Course course) {
        // 使用文本块定义 SQL，保持整洁
        String sql = """
                INSERT INTO courses (course_name, teacher, credit) VALUES (?, ?, ?)
                """;

        DBExecutor.executeUpdate(
                "新增课程-" + course.getCourseName(),
                sql,
                course.getCourseName(),
                course.getTeacher(),
                course.getCredit()
        );

        return course;
    }

    @Override
    public Optional<Course> findByCourseId(Integer courseId) {
        String sql = "SELECT course_id, course_name, teacher, credit FROM courses WHERE course_id = ?";

        List<Course> list = DBExecutor.executeQuery(
                "查询ID为" + courseId + "的课程",
                sql,
                (rs) -> new Course(
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getString("teacher"),
                        rs.getDouble("credit")
                ),
                courseId
        );
        return list.stream().findFirst();
    }

    @Override
    public int updateByCourseName(String courseName, String teacherName, Double credit) {
        String sql = "UPDATE courses SET teacher = ?, credit = ? WHERE course_name = ?";
        return DBExecutor.executeUpdate(
                "根据课程名修改:" + courseName,
                sql,
                teacherName,
                credit,
                courseName
        );
    }

    @Override
    public List<Course> findDistinctByCreditGreaterThanOrderByCreditDesc(Double limit) {
        String sql = """
                SELECT DISTINCT course_name, teacher, credit FROM courses WHERE credit > ? ORDER BY credit DESC
                """;
        return DBExecutor.executeQuery(
                "筛选高于" + limit + "学分课程:",
                sql,
                rs -> {
                    Course course = new Course();
                    course.setCourseName(rs.getString("course_name"));
                    course.setTeacher(rs.getString("teacher"));
                    course.setCredit(rs.getDouble("credit"));
                    return course;
                },
                limit
        );
    }
}
