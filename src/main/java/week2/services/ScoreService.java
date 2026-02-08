package week2.services;

import week2.dto.ScoreDetailDTO;
import week2.enums.ScoreRemark;
import week2.models.Score;

import java.util.List;

public interface ScoreService {
    void addScore(Score score);

    boolean updateScoreByStudentNameAndCourseName(String studentName, String courseName, Double score, ScoreRemark remark);

    boolean deleteScoreByScoreLessThan(Double score, ScoreRemark remark);

    boolean deleteScoreByCourseNameAndScoreLessThan(String courseName, Double score);

    boolean deleteScoreByStudentPhoneStartingWith(String phonePrefix);

    List<ScoreDetailDTO> findScoresByTeacherNameOrderByScoreDesc(String teacherName);

    List<ScoreDetailDTO> findScoresByStudentNameKeywords(String keyword1, String keyword2);
}
