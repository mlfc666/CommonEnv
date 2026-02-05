package week1.repository;

import week1.models.MonsterModel;

import java.util.*;

public class MonsterRepository {
    private final Map<String, MonsterModel> storage = new HashMap<>();

    public void save(MonsterModel monster) {
        storage.put(monster.getName(), monster);
    }

    public Optional<MonsterModel> findByName(String name) {
        return Optional.ofNullable(storage.get(name));
    }

    public List<MonsterModel> findAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean existsByName(String name) {
        return storage.containsKey(name);
    }
}
