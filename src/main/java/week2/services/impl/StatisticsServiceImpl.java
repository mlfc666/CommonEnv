package week2.services.impl;

import week2.dto.ClassGenderCountDTO;
import week2.repository.StudentRepository;
import week2.services.StatisticsService;

import java.util.List;

public class StatisticsServiceImpl implements StatisticsService {
    private final StudentRepository studentRepository;

    public StatisticsServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<ClassGenderCountDTO> getClassGenderStatistics() {
        return studentRepository.countGenderByClass();
    }
}
