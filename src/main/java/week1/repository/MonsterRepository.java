package week1.repository;

import week1.models.Monster;
import week1.models.Story;

import java.util.List;
import java.util.Optional;

public interface MonsterRepository {
    Monster insert(Monster monster);

    Optional<Monster> findByName(String name);

    List<Monster> findAll();

    boolean existsByName(String name);

    Optional<Story> findStoryByMonsterAndTitle(String name, String title);
}