package week1.services;

import week1.models.MonsterModel;
import week1.models.StoryModel;

import java.util.*;

public class MonsterServiceImpl implements IMonsterService {
    // Key 是名字，Value 是怪兽对象
    private final Map<String, MonsterModel> monsterMap = new HashMap<>();

    @Override
    public Optional<MonsterModel> findByName(String name) {
        return Optional.ofNullable(monsterMap.get(name));
    }


    @Override
    public boolean addMonster(MonsterModel monster) {
        if (monster == null || monsterMap.containsKey(monster.getName())) return false;
        monsterMap.put(monster.getName(), monster);
        return true;
    }

    @Override
    public boolean addStoryToMonster(String name, StoryModel story) {
        return findByName(name)
                .map(monster -> monster.addStory(story)) // 如果找到怪兽
                .orElse(false); // 如果没找到怪兽
    }

    @Override
    public List<MonsterModel> getAllMonsters() {
        // 转换成List再返回
        return new ArrayList<>(monsterMap.values());
    }
}