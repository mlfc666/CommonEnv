package week3.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Score {
    private Integer id;
    private Integer studentId;
    private String subject;
    private BigDecimal score;
    private LocalDate examTime;

    public Score() {
    }

    public Score(Integer studentId, String subject, BigDecimal score, LocalDate examTime) {
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

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public LocalDate getExamTime() {
        return examTime;
    }

    public void setExamTime(LocalDate examTime) {
        this.examTime = examTime;
    }
}