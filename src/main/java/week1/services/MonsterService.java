package week1.services;

import week1.models.MonsterModel;
import week1.models.StoryModel;

import java.util.List;
import java.util.Optional;

public interface MonsterService {
    // 添加怪兽
    boolean addMonster(MonsterModel monster);
    // 获取列表
    List<MonsterModel> getAllMonsters();
    // 按怪兽名查找
    Optional<MonsterModel> findByName(String name);
    // 给怪兽添加故事
    boolean addStoryToMonster(String name, StoryModel story);
}