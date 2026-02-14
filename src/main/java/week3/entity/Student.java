package week3.entity;

import week3.enums.Gender;

import java.time.LocalDateTime;

public class Student {
    private Integer id;
    private String name;
    private Gender gender;
    private Integer age;
    private String studentNo;
    private LocalDateTime createTime;

    public Student() {
    }

    public Student(String name, Gender gender, Integer age, String studentNo) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.studentNo = studentNo;
    }

    public Student(Integer id, String name, Gender gender, Integer age, String studentNo, LocalDateTime createTime) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.studentNo = studentNo;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}