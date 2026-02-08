package week2.repository;

import week2.models.Course;

import java.util.List;

public interface CourseRepository {
    Course insert(Course course);

    List<Course> findAll();

    int updateByCourseName(String courseName, String teacherName, Double credit);

    List<Course> findDistinctByCreditGreaterThanOrderByCreditDesc(Double limit);

}
