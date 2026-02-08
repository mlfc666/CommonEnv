package week2.services.impl;

import week2.enums.Gender;
import week2.models.Student;
import week2.repository.StudentRepository;
import week2.services.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void addStudent(Student student) {
        studentRepository.insert(student);
    }

    @Override
    public int addAllStudentsAge(String className) {
        return studentRepository.incrementAgeByClassName(className);
    }

    @Override
    public List<Student> findFemaleStudentsOrderByClassAscAgeDesc() {
        return studentRepository.findByGenderOrderByClassNameAscAgeDesc(Gender.FEMALE);
    }

    @Override
    public List<Student> findFirst2ByClassNameAndAgeBetweenAndGender(String className, int minAge, int maxAge, Gender gender) {
        return studentRepository.findFirst2ByClassNameAndAgeBetweenAndGender(className, minAge, maxAge, gender);
    }
}
