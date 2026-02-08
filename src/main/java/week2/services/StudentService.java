package week2.services;

import week2.enums.Gender;
import week2.models.Student;

import java.util.List;

public interface StudentService {
    void addStudent(Student student);

    List<Student> findAll();

    void addStudent(String studentName, Gender gender, String className, int age, String phone);

    int addAllStudentsAge(String className);

    List<Student> findFemaleStudentsOrderByClassAscAgeDesc();

    List<Student> findFirst2ByClassNameAndAgeBetweenAndGender(String className, int minAge, int maxAge, Gender gender);
}
