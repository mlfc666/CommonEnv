package week2.models;

import week2.enums.ScoreRemark;

import java.time.LocalDateTime;

public class Score {
    private Integer scoreId;
    private Integer studentId;
    private Integer courseId;
    private Double score;
    private LocalDateTime examTime;
    private ScoreRemark remark;

    public Score() {
    }

    public Score(Integer studentId, Integer courseId, double score, LocalDateTime examTime, ScoreRemark remark) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.score = score;
        this.examTime = examTime;
        this.remark = remark;
    }

    public Score(Integer scoreId, Integer studentId, Integer courseId, double score, LocalDateTime examTime, ScoreRemark remark) {
        this.scoreId = scoreId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.score = score;
        this.examTime = examTime;
        this.remark = remark;
    }

    public Integer getScoreId() {
        return scoreId;
    }

    public void setScoreId(Integer scoreId) {
        this.scoreId = scoreId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public LocalDateTime getExamTime() {
        return examTime;
    }

    public void setExamTime(LocalDateTime examTime) {
        this.examTime = examTime;
    }

    public ScoreRemark getRemark() {
        return remark;
    }

    public void setRemark(ScoreRemark remark) {
        this.remark = remark;
    }
}
