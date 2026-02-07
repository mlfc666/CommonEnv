package week1.services;

import week1.models.MonsterModel;
import week1.models.StoryModel;

import java.util.List;
import java.util.Optional;

public interface Statistics {
    Optional<StoryModel> getMostPopular(List<MonsterModel> monsters);
}