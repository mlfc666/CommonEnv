package week3.dto;

import week3.entity.Student;

import java.util.List;

public record StudentPageDTO(List<Student> data, long total){}
