package week1.services;

import week1.models.Monster;
import week1.models.Story;

import java.util.List;
import java.util.Optional;

public interface MonsterService {
    // 添加怪兽
    boolean addMonster(Monster monster);
    // 获取列表
    List<Monster> getAllMonsters();
    // 按怪兽名查找
    Optional<Monster> findByName(String name);
    // 给怪兽添加故事
    boolean addStoryToMonster(String name, Story story);
}