package week2.services.impl;

import week2.models.Course;
import week2.services.CourseService;

import java.util.List;

public class CourseServiceImpl implements CourseService {
    @Override
    public void addCourse(Course course) {

    }

    @Override
    public boolean updateCourseByCourseName(String CourseName, String teacherName, Double credit) {
        return false;
    }

    @Override
    public List<Course> findDistinctByCreditGreaterThanOrderByCreditDesc(Double credit) {
        return List.of();
    }
}
