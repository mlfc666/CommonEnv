package week2.repository.impl;

import week2.dto.ClassGenderCountDTO;
import week2.models.Course;
import week2.repository.CourseRepository;

import java.util.List;

public class CoureseRepositoryImpl implements CourseRepository {
    @Override
    public Course insert(Course course) {
        return null;
    }

    @Override
    public void update(Course course) {

    }

    @Override
    public int incrementAgeByClassName(String className) {
        return 0;
    }

    @Override
    public List<Course> findDistinctByCreditGreaterThanOrderByCreditDesc(double limit) {
        return List.of();
    }

    @Override
    public List<ClassGenderCountDTO> countGenderByClass() {
        return List.of();
    }
}
