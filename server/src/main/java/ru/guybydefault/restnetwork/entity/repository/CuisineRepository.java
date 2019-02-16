package ru.guybydefault.restnetwork.entity.repository;

import org.springframework.data.repository.CrudRepository;
import ru.guybydefault.restnetwork.entity.Cuisine;

public interface CuisineRepository extends CrudRepository<Cuisine, Integer> {
}
