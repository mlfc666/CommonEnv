package week1.repository.impl;

import week1.models.MonsterModel;
import week1.repository.MonsterRepository;

import java.util.*;


public class MonsterRepositoryImpl implements MonsterRepository {
    // 具体的存储逻辑封装在实现类内部
    private final Map<String, MonsterModel> storage = new HashMap<>();

    @Override
    public void save(MonsterModel monster) {
        storage.put(monster.getName(), monster);
    }

    @Override
    public Optional<MonsterModel> findByName(String name) {
        return Optional.ofNullable(storage.get(name));
    }

    @Override
    public List<MonsterModel> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean existsByName(String name) {
        return storage.containsKey(name);
    }
}