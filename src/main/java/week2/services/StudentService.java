package week2.services;

import week2.enums.Gender;
import week2.models.Student;

import java.util.List;

public interface StudentService {
    void addStudent(Student student);

    int addAllStudentsAge(String className);

    List<Student> findFemaleStudentsOrderByClassAscAgeDesc();

    List<Student> findFirst2ByClassNameAndAgeBetweenAndGender(String className, int minAge, int maxAge, Gender gender);
}
