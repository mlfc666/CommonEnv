package week3.entity;

import java.time.LocalDateTime;

public class Score {
    private Integer id;
    private Integer studentId;
    private String subject;
    private Double score;
    private LocalDateTime examTime;

    public Score() {
    }

    public Score(Integer studentId, String subject, Double score, LocalDateTime examTime) {
        this.studentId = studentId;
        this.subject = subject;
        this.score = score;
        this.examTime = examTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public LocalDateTime getExamTime() {
        return examTime;
    }

    public void setExamTime(LocalDateTime examTime) {
        this.examTime = examTime;
    }

    @Override
    public String toString() {
        return String.format("ID: %-4d | 学生ID: %-4d | 科目: %-12s | 成绩: %-6.1f | 考试时间: %s",
                id,
                studentId,
                subject,
                score,
                examTime != null ? examTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "NULL");
    }
}