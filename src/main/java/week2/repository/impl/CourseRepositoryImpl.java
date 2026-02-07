package week2.repository.impl;

import week2.dto.ClassGenderCountDTO;
import week2.models.Course;
import week2.repository.CourseRepository;
import week2.utils.DBExecutor;

import java.util.List;

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
    public void update(Course course) {
        String sql = """
                UPDATE courses SET course_name = ?, teacher = ?, credit = ? WHERE course_id = ?
                """;

        DBExecutor.executeUpdate(
                "修改课程-ID:" + course.getCourseId(),
                sql,
                course.getCourseName(),
                course.getTeacher(),
                course.getCredit(),
                course.getCourseId()
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
