package ru.guybydefault.restnetwork.entity.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.guybydefault.restnetwork.entity.Cook;

import java.util.List;

public interface CookRepository  extends CrudRepository<Cook, Integer> {

    @Query(value = "SELECT c FROM Cook c WHERE c.restaurant.id = :restaurantId ORDER BY c.id ASC")
    List<Cook> findAllByRestaurantByOrderByIdAsc(@Param("restaurantId") int restaurantId);

    List<Cook> findAllByOrderByIdAsc();

}
