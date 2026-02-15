package week3.dao;

import week3.entity.Score;

import java.util.List;

public interface ScoreDao {
    // 新增单条学生成绩
    Score insert(Score score);

    List<Score> findAll();

    // 根据student_id删除学生的所有成绩
    int deleteByStudentId(Integer studentId);

    // 根据学号student_no查询指定学生的所有科目成绩
    List<Score> findByStudentNo(String studentNo);
}