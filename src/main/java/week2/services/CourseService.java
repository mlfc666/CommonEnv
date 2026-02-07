package week2.services;

import week2.models.Course;

import java.util.List;

public interface CourseService {
    void addCourse(Course course);

    boolean updateCourseByCourseName(String CourseName, String teacherName, Double credit);

    List<Course> findDistinctByCreditGreaterThanOrderByCreditDesc(Double credit);

}
