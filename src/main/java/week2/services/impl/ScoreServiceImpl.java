package week2.services.impl;

import week2.dto.ScoreDetailDTO;
import week2.enums.ScoreRemark;
import week2.exception.IllegalScoreException;
import week2.models.Score;
import week2.repository.ScoreRepository;
import week2.services.ScoreService;

import java.util.List;

public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;

    public ScoreServiceImpl(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    @Override
    public void addScore(Score score) {

        if (score.getScore() > 100 || score.getScore() < 0) {
            throw new IllegalScoreException("成绩必须在 0 到 100 之间");
        }
        scoreRepository.insert(score);
    }

    @Override
    public boolean updateScoreByStudentNameAndCourseName(String studentName, String courseName, Double score, ScoreRemark remark) {
        return scoreRepository.updateScoreByStudentNameAndCourseName(studentName, courseName, score, remark) > 0;
    }

    @Override
    public boolean deleteScoreByScoreLessThan(Double score, ScoreRemark remark) {
        return scoreRepository.deleteByScoreLessThan(score, remark) > 0;
    }

    @Override
    public boolean deleteScoreByCourseNameAndScoreLessThan(String courseName, Double score) {
        return scoreRepository.deleteByCourseNameAndScoreLessThan(courseName, score) > 0;
    }

    @Override
    public boolean deleteScoreByStudentPhoneStartingWith(String phonePrefix) {
        return scoreRepository.deleteByStudentPhoneStartingWith(phonePrefix) > 0;
    }

    @Override
    public List<ScoreDetailDTO> findScoresByTeacherNameOrderByScoreDesc(String teacherName) {
        return scoreRepository.findScoreDetailsByTeacher(teacherName);
    }

    @Override
    public List<ScoreDetailDTO> findScoresByStudentNameKeywords(String k1, String k2) {
        return scoreRepository.findScoreDetailsByStudentNameKeywords(k1, k2);
    }
}
