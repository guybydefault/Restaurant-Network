package ru.guybydefault.restnetwork.entity.repository;

import org.springframework.data.repository.CrudRepository;
import ru.guybydefault.restnetwork.entity.Restaurant;

public interface RestaurantRepository extends CrudRepository<Restaurant, Integer> {
}
