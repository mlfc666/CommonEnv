package week1.services.impl;

import week1.models.Monster;
import week1.models.Story;
import week1.repository.MonsterRepository;
import week1.repository.impl.MonsterRepositoryImpl;
import week1.services.MonsterService;
import week1.ui.IllegalMonsterException;

import java.util.List;
import java.util.Optional;

public class MonsterServiceImpl implements MonsterService {
    // 注入Repository
    private final MonsterRepository repository = new MonsterRepositoryImpl();

    @Override
    public boolean addMonster(Monster monster) {
        if (monster == null) {
            throw new IllegalMonsterException("怪兽对象不能为空");
        }

        if (monster.getAge() < 0) {
            throw new IllegalMonsterException("年龄不能小于0");
        }

        if (repository.existsByName(monster.getName())) {
            // 已存在
            throw new IllegalMonsterException("怪兽名称 [" + monster.getName() + "] 已存在，请更换名称");
        }

        repository.insert(monster);
        return true;
    }

    @Override
    public boolean addStoryToMonster(String name, Story story) {
        return repository.findByName(name)
                .map(monster -> monster.addStory(story))
                .orElse(false);
    }

    @Override
    public List<Monster> getAllMonsters() {
        return repository.findAll();
    }

    @Override
    public Optional<Monster> findByName(String name) {
        return repository.findByName(name);
    }
}