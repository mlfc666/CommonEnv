package week3.dao.impl;

import week3.dao.StudentDao;
import week3.dto.StudentQueryDTO;
import week3.entity.Student;

import java.util.List;

public class StudentDaoImpl implements StudentDao {
    @Override
    public int insert(Student student) {
        return 0;
    }

    @Override
    public int updateNameAndAge(Integer id, String name, Integer age) {
        return 0;
    }

    @Override
    public int deleteById(Integer id) {
        return 0;
    }

    @Override
    public Student findById(Integer id) {
        return null;
    }

    @Override
    public List<Student> findByNameKeyword(String keyword) {
        return List.of();
    }

    @Override
    public int batchInsert(List<Student> students) {
        return 0;
    }

    @Override
    public List<Student> findByPage(StudentQueryDTO query, int offset, int size) {
        return List.of();
    }
}