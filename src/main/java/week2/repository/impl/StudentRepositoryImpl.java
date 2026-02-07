package week2.repository.impl;

import week2.enums.Gender;
import week2.models.Student;
import week2.repository.StudentRepository;

import java.util.List;

public class StudentRepositoryImpl implements StudentRepository {
    @Override
    public Student insert(Student student) {
        return null;
    }

    @Override
    public void update(Student student) {

    }

    @Override
    public List<Student> findByGenderOrderByClassNameAscAgeDesc(Gender gender) {
        return List.of();
    }

    @Override
    public List<Student> findFirst2ByClassNameAndAgeBetweenAndGender(String className, int minAge, int maxAge, Gender gender) {
        return List.of();
    }
}
