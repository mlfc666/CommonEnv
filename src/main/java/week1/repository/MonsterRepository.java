package week1.repository;

import week1.models.MonsterModel;

import java.util.List;
import java.util.Optional;

public interface MonsterRepository {
    void save(MonsterModel monster);

    Optional<MonsterModel> findByName(String name);

    List<MonsterModel> findAll();

    boolean existsByName(String name);
}