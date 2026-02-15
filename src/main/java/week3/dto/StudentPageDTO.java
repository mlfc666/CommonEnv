package week3.dto;

import week3.entity.Student;

import java.util.List;

public class StudentPageDTO {
    private final List<Student> data;

    private final long total;

    public StudentPageDTO(List<Student> data, long total) {
        this.data = data;
        this.total = total;
    }

    public List<Student> getData() {
        return data;
    }

    public long getTotal() {
        return total;
    }
}
