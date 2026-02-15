package week3.dao;

import week3.dto.StudentQueryDTO;
import week3.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDao {
    // 新增单条学生数据
    Student insert(Student student);

    //查找全部学生数据
    List<Student> findAll();

    // 根据id修改姓名和年龄
    int updateNameAndAge(Integer id, String name, Integer age);

    // 根据id删除学生
    int deleteById(Integer id);

    // 根据id查询单个学生
    Optional<Student> findById(Integer id);

    // 根据姓名模糊查询学生数据
    List<Student> findByNameKeyword(String keyword);

    // 批量插入学生数据
    int batchInsert(List<Student> students);

    // 分页查询学生数据
    List<Student> findByPage(StudentQueryDTO query, int offset, int size);

    long countByCondition(StudentQueryDTO query);
}