package week2.services;

import week2.dto.ClassGenderCountDTO;

import java.util.List;

public interface StatisticsService {
    List<ClassGenderCountDTO> getClassGenderStatistics();
}
