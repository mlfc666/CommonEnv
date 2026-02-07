package week2.services.impl;

import week2.dto.ScoreDetailDTO;
import week2.enums.ScoreRemark;
import week2.models.Score;
import week2.services.ScoreService;

import java.util.List;

public class ScoreServiceImpl implements ScoreService {
    @Override
    public void addScore(Score score) {

    }

    @Override
    public boolean updateScoreByStudentNameAndCourseName(String studentName, String courseName, Double score, ScoreRemark remark) {
        return false;
    }

    @Override
    public boolean deleteScoreByScoreLessThan(Double score, ScoreRemark remark) {
        return false;
    }

    @Override
    public boolean deleteScoreByCourseNameAndScoreLessThan(String courseName, Double score) {
        return false;
    }

    @Override
    public boolean deleteScoreByStudentPhoneStartingWith(String phonePrefix) {
        return false;
    }

    @Override
    public List<ScoreDetailDTO> findScoresByTeacherName(String teacherName) {
        return List.of();
    }

    @Override
    public List<ScoreDetailDTO> findScoresByStudentNameKeywords(List<String> keywords) {
        return List.of();
    }
}
