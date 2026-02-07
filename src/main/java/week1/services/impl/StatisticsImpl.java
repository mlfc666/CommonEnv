package week1.services.impl;

import week1.models.MonsterModel;
import week1.models.StoryModel;
import week1.services.Statistics;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatisticsImpl implements Statistics {
    @Override
    public Optional<StoryModel> getMostPopular(List<MonsterModel> monsters) {
        // 定义一个临时的数据结构
        record StoryFreq(StoryModel story, long count) {}

        return monsters.stream()
                .flatMap(m -> m.getStoryList().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .map(e -> new StoryFreq(e.getKey(), e.getValue()))
                .max(Comparator.comparingLong(StoryFreq::count))
                .map(StoryFreq::story);
    }
}
