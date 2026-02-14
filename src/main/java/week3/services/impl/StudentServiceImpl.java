package week3.services.impl;

import week3.dto.StudentPageDTO;
import week3.dto.StudentQueryDTO;
import week3.entity.Student;
import week3.services.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    @Override
    public void addStudent(Student student) {

    }

    @Override
    public void updateStudentInfo(Integer id, String name, Integer age) {

    }

    @Override
    public void deleteStudentComplete(Integer id) {

    }

    @Override
    public Student getStudentById(Integer id) {
        return null;
    }

    @Override
    public List<Student> searchStudentsByNameKeyword(String keyword) {
        return List.of();
    }

    @Override
    public void batchImportStudents(List<Student> students) {

    }

    @Override
    public StudentPageDTO getStudentsByPage(StudentQueryDTO query, int page, int size) {
        return null;
    }
}
