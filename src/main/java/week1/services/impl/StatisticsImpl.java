package week1.services.impl;

import week1.models.Monster;
import week1.models.Story;
import week1.services.Statistics;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatisticsImpl implements Statistics {
    @Override
    public Optional<Story> getMostPopular(List<Monster> monsters) {
        // 定义一个临时的数据结构
        record StoryFreq(Story story, long count) {}

        return monsters.stream()
                .flatMap(m -> m.getStoryList().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .map(e -> new StoryFreq(e.getKey(), e.getValue()))
                .max(Comparator.comparingLong(StoryFreq::count))
                .map(StoryFreq::story);
    }
}
