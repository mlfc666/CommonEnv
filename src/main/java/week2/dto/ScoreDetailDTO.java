package week2.dto;

public class ScoreDetailDTO {
    private String studentName;
    private String courseName;

    private Double score;

    public ScoreDetailDTO() {
    }

    public ScoreDetailDTO(String studentName, String courseName, Double score) {
        this.studentName = studentName;
        this.courseName = courseName;
        this.score = score;
    }
}