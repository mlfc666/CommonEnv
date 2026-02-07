package week2.models;

public class Course {
    private Integer courseId;
    private String courseName;
    private String teacher;
    private Double credit; // 泥工的学分可以带小数

    public Course() {
    }

    public Course(String courseName, String teacher, Double credit) {
        this.courseName = courseName;
        this.teacher = teacher;
        this.credit = credit;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTeacher() {
        return teacher;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }
}
