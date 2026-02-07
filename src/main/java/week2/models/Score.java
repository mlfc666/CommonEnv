package week2.models;

import week2.enums.ScoreRemark;

import java.time.LocalDateTime;

public class Score {
    private Integer scoreId;
    private Integer studentId;
    private Integer courseId;
    private int score;
    private LocalDateTime examTime;
    private ScoreRemark remark;
}
