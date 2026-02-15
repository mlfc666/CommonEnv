package week3.services.impl;

import common.utils.JdbcUtils;
import week3.dao.ScoreDao;
import week3.dao.StudentDao;
import week3.dto.StudentPageDTO;
import week3.dto.StudentQueryDTO;
import week3.entity.Student;
import week3.services.StudentService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class StudentServiceImpl implements StudentService {
    private final StudentDao studentDao;
    private final ScoreDao scoreDao;

    public StudentServiceImpl(StudentDao studentDao, ScoreDao scoreDao) {
        this.studentDao = studentDao;
        this.scoreDao = scoreDao;
    }

    @Override
    public void addStudent(Student student) {
        studentDao.insert(student);
    }

    @Override
    public List<Student> findAll() {
        return studentDao.findAll();
    }

    @Override
    public void updateStudentInfo(Integer id, String name, Integer age) {
        studentDao.updateNameAndAge(id, name, age);
    }

    public void deleteStudentComplete(Integer id) {
        try {
            // 获取连接并开启事务
            JdbcUtils.getConnection().setAutoCommit(false);

            // 删除成绩
            scoreDao.deleteByStudentId(id);

            // 删除学生
            studentDao.deleteById(id);

            // 提交事务
            JdbcUtils.getConnection().commit();

        } catch (Exception e) {
            try {
                // 失败回滚
                JdbcUtils.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("数据库事务操作异常: " + e.getMessage(), e);
        } finally {
            JdbcUtils.closeConnection();
        }
    }

    @Override
    public Optional<Student> getStudentById(Integer id) {
        return studentDao.findById(id);
    }

    @Override
    public List<Student> searchStudentsByNameKeyword(String keyword) {
        return studentDao.findByNameKeyword(keyword);
    }

    @Override
    public void batchImportStudents(List<Student> students) {
        studentDao.batchInsert(students);
    }

    @Override
    public StudentPageDTO getStudentsByPage(StudentQueryDTO query, int page, int size) {

        int offset = (page - 1) * size;
        // 调用DAO查询当前页数据
        List<Student> data = studentDao.findByPage(query, offset, size);

        // 调用DAO查询总记录数
        long total = studentDao.countByCondition(query);

        // 4. 封装结果返回
        return new StudentPageDTO(data, total);
    }
}
