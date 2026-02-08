package week2.repository;

import week2.dto.ClassGenderCountDTO;
import week2.models.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    Course insert(Course course);

    Optional<Course> findByCourseId(Integer courseId);

    int updateByCourseName(String courseName, String teacherName, Double credit);

    List<Course> findDistinctByCreditGreaterThanOrderByCreditDesc(Double limit);

}
