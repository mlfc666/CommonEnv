package week2.repository;

import week2.dto.ClassGenderCountDTO;
import week2.enums.Gender;
import week2.models.Student;

import java.util.List;

public interface StudentRepository {
    Student insert(Student student);

    List<Student> findAll();

    int incrementAgeByClassName(String className);

    List<Student> findByGenderOrderByClassNameAscAgeDesc(Gender gender);

    List<Student> findFirst2ByClassNameAndAgeBetweenAndGender(String className, int minAge, int maxAge, Gender gender);

    List<ClassGenderCountDTO> countGenderByClass();
}
