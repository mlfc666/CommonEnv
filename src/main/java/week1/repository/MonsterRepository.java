package week1.repository;

import week1.models.Monster;

import java.util.List;
import java.util.Optional;

public interface MonsterRepository {
    void save(Monster monster);

    Optional<Monster> findByName(String name);

    List<Monster> findAll();

    boolean existsByName(String name);
}