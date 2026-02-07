package week2.repository.impl;

import week2.enums.ScoreRemark;
import week2.models.Score;
import week2.repository.ScoreRepository;

import java.util.List;

public class ScoreRepositoryImpl implements ScoreRepository {
    @Override
    public Score insert(Score score) {
        return null;
    }

    @Override
    public void update(Score score) {

    }

    @Override
    public int deleteByScoreLessThan(double limit, ScoreRemark remark) {
        return 0;
    }

    @Override
    public int deleteByCourseNameAndScoreLessThan(String courseName, double limit) {
        return 0;
    }

    @Override
    public int deleteByStudentPhoneStartingWith(String phonePrefix) {
        return 0;
    }

    @Override
    public List<Score> findByCourseTeacherNameOrderByScoreDesc(String teacherName) {
        return List.of();
    }

    @Override
    public List<Score> findByStudentNameContainingOrStudentNameContaining(String name1, String name2) {
        return List.of();
    }
}
