package week2.services.impl;

import week2.enums.Gender;
import week2.models.Student;
import week2.services.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    @Override
    public void addStudent(Student student) {

    }

    @Override
    public int addAllStudentsAge(String className) {
        return 0;
    }

    @Override
    public List<Student> findFemaleStudentsOrderByClassAscAgeDesc() {
        return List.of();
    }

    @Override
    public List<Student> findFirst2ByClassNameAndAgeBetweenAndGender(String className, int minAge, int maxAge, Gender gender) {
        return List.of();
    }
}
