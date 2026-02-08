package week2.services.impl;

import week2.models.Course;
import week2.repository.CourseRepository;
import week2.services.CourseService;

import java.util.List;

public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void addCourse(Course course) {
        courseRepository.insert(course);
    }

    @Override
    public boolean updateCourseByCourseName(String courseName, String teacherName, Double credit) {
        return courseRepository.updateByCourseName(courseName, teacherName, credit) > 0;
    }

    @Override
    public List<Course> findDistinctByCreditGreaterThanOrderByCreditDesc(Double credit) {
        return courseRepository.findDistinctByCreditGreaterThanOrderByCreditDesc(credit);
    }
}
