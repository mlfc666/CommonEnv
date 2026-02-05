package week1.services;

import week1.models.MonsterModel;

import java.util.List;

public interface IStatistics {
    void showReport(List<MonsterModel> monsters);
}