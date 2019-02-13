package ru.guybydefault.restnetwork.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.guybydefault.restnetwork.entity.Restaurant;
import ru.guybydefault.restnetwork.entity.Shift;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

public interface ShiftRepository extends CrudRepository<Shift, Long> {
    List<Shift> findAllByRestaurant(Restaurant restaurant);

    List<Shift> findAllByRestaurantAndStartDateTimeBetweenOrEndDateTimeBetween(Restaurant restaurant, Instant leftStart, Instant rightStart , Instant leftEnd, Instant rightEnd);
}
