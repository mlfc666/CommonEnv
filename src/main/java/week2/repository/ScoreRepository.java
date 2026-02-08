package week2.repository;

import week2.dto.ScoreDetailDTO;
import week2.enums.ScoreRemark;
import week2.models.Score;

import java.util.List;

public interface ScoreRepository {
    Score insert(Score score);

    int updateScoreByStudentNameAndCourseName(String studentName, String courseName, Double score, ScoreRemark remark);

    int deleteByScoreLessThan(Double limit, ScoreRemark remark);

    int deleteByCourseNameAndScoreLessThan(String courseName, Double limit);

    int deleteByStudentPhoneStartingWith(String phonePrefix);

    List<Score> findByTeacherNameOrderByScoreDesc(String teacherName);

    List<Score> findByStudentNameContainingOrStudentNameContaining(String name1, String name2);

    List<ScoreDetailDTO> findScoreDetailsByTeacher(String teacherName);

    List<ScoreDetailDTO> findScoreDetailsByStudentNameKeywords(String k1, String k2);
}
