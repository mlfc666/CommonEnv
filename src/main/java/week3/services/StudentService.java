package week3.services;

import week3.dto.StudentQueryDTO;
import week3.entity.Student;
import week3.dto.StudentPageDTO;
import java.util.List;
import java.util.Optional;

public interface StudentService {

    // 新增单条学生数据
    void addStudent(Student student);

    // 查询所有学生数据
    List<Student> findAll();

    // 根据id修改学生的姓名和年龄
    void updateStudentInfo(Integer id, String name, Integer age);

    // 根据id删除学生数据及其所有成绩
    void deleteStudentComplete(Integer id);

    // 根据id查询单个学生数
    Optional<Student> getStudentById(Integer id);

    // 根据姓名模糊查询学生数据
    List<Student> searchStudentsByNameKeyword(String keyword);

    // 批量新增学生
    void batchImportStudents(List<Student> students);

    // 条件分页查询学生
    StudentPageDTO getStudentsByPage(StudentQueryDTO query, int page, int size);
}