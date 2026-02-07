package week2.repository;

import week2.dto.ClassGenderCountDTO;
import week2.models.Course;

import java.util.List;

public interface CourseRepository {
    Course insert(Course course);
    void update(Course course);
    int incrementAgeByClassName(String className);
    List<Course> findDistinctByCreditGreaterThanOrderByCreditDesc(double limit);
    List<ClassGenderCountDTO> countGenderByClass();
}
