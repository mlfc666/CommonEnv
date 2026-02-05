package week1.services;

import week1.models.MonsterModel;
import week1.models.StoryModel;
import week1.repository.MonsterRepository;
import week1.ui.IllegalMonsterException;

import java.util.List;
import java.util.Optional;

public class MonsterServiceImpl implements IMonsterService {
    // 注入Repository
    private final MonsterRepository repository = new MonsterRepository();

    @Override
    public boolean addMonster(MonsterModel monster) {
        if (monster == null) {
            throw new IllegalMonsterException("怪兽对象不能为空");
        }

        if (repository.existsByName(monster.getName())) {
            // 已存在
            throw new IllegalMonsterException("怪兽名称 [" + monster.getName() + "] 已存在，请更换名称");
        }

        repository.save(monster);
        return true;
    }

    @Override
    public boolean addStoryToMonster(String name, StoryModel story) {
        return repository.findByName(name)
                .map(monster -> monster.addStory(story))
                .orElse(false);
    }

    @Override
    public List<MonsterModel> getAllMonsters() {
        return repository.findAll();
    }

    @Override
    public Optional<MonsterModel> findByName(String name) {
        return repository.findByName(name);
    }
}