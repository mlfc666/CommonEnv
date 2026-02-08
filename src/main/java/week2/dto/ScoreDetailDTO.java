package week2.dto;

public class ScoreDetailDTO {
    private String studentName;
    private String courseName;

    private Double score;

    public ScoreDetailDTO(String studentName, String courseName, Double score) {
        this.studentName = studentName;
        this.courseName = courseName;
        this.score = score;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}