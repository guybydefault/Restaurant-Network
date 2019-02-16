package ru.guybydefault.restnetwork.entity.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.guybydefault.restnetwork.entity.Cook;
import ru.guybydefault.restnetwork.entity.Restaurant;
import ru.guybydefault.restnetwork.entity.Shift;

import java.time.Instant;
import java.util.List;

public interface ShiftRepository extends CrudRepository<Shift, Long> {
    List<Shift> findAllByRestaurant(Restaurant restaurant);

    @Query("SELECT s FROM Shift s WHERE s.restaurant = :restaurant and (s.startDateTime BETWEEN :start AND :end OR s.endDateTime BETWEEN :start AND :end)")
    List<Shift> findAllByRestaurantBetweenDates(@Param(value = "restaurant") Restaurant restaurant, @Param("start") Instant start, @Param("end") Instant end);

    @Query("UPDATE Shift s SET s.cook = NULL WHERE s.cook = :cook")
    @Modifying
    void setCookToNullByCook(@Param("cook") Cook cook);
}
