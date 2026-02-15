package week3.services;

import week3.entity.Score;

import java.util.List;

public interface ScoreService {
    // 新增单条学生成绩
    void addScore(Score score);

    // 查找所有学生成绩
    List<Score> findAll();

    // 根据学号查询指定学生的所有科目成绩
    List<Score> findScoresByStudentNo(String studentNo);
}
