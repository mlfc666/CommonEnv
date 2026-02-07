package week2.models;

import week2.enums.Gender;

import java.time.LocalDateTime;

public class Student {
    private Integer studentId;
    private String studentName;
    private Gender gender;
    private int age;
    private String className;// 用class会出事
    private String phone;
    private LocalDateTime createTime;

    public Student() {
    }

    public Student(String studentName, Gender gender, int age, String className, String phone) {
        this.studentName = studentName;
        this.gender = gender;
        this.age = age;
        this.className = className;
        this.phone = phone;
        this.createTime = LocalDateTime.now();
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
