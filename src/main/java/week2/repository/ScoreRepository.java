package week2.repository;

import week2.enums.ScoreRemark;
import week2.models.Score;

import java.util.List;

public interface ScoreRepository {
    Score insert(Score score);

    void update(Score score);

    int deleteByScoreLessThan(double limit, ScoreRemark remark);

    int deleteByCourseNameAndScoreLessThan(String courseName, double limit);

    int deleteByStudentPhoneStartingWith(String phonePrefix);

    List<Score> findByCourseTeacherNameOrderByScoreDesc(String teacherName);
    List<Score> findByStudentNameContainingOrStudentNameContaining(String name1, String name2);

}
