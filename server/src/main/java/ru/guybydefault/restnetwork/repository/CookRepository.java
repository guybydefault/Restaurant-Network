package ru.guybydefault.restnetwork.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.guybydefault.restnetwork.entity.Cook;
import ru.guybydefault.restnetwork.entity.Restaurant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public interface CookRepository  extends CrudRepository<Cook, Integer> {

    @Query(value = "Select c from Cook c where c.restaurant.id = :restaurantId")
    List<Cook> findAllByRestaurant(@Param("restaurantId") int restaurantId);

}
