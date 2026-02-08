package week2.repository;

import week2.dto.ClassGenderCountDTO;
import week2.enums.Gender;
import week2.models.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Student insert(Student student);

    Optional<Student> findByStudentId(Integer studentId);

    void update(Student student);

    int incrementAgeByClassName(String className);

    // 查询所有女生的姓名、班级、手机号，按班级升序、年龄降序排列；
    List<Student> findByGenderOrderByClassNameAscAgeDesc(Gender gender);

    List<Student> findFirst2ByClassNameAndAgeBetweenAndGender(String className, int minAge, int maxAge, Gender gender);

    List<ClassGenderCountDTO> countGenderByClass();
}
