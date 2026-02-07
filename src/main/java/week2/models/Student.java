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

}
