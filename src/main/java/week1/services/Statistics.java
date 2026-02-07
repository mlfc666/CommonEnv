package week1.services;

import week1.models.Monster;
import week1.models.Story;

import java.util.List;
import java.util.Optional;

public interface Statistics {
    Optional<Story> getMostPopular(List<Monster> monsters);
}