package ru.guybydefault.restnetwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.guybydefault.restnetwork.entity.Restaurant;
import ru.guybydefault.restnetwork.entity.Shift;
import ru.guybydefault.restnetwork.repository.RestaurantRepository;
import ru.guybydefault.restnetwork.repository.ShiftRepository;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
public class ShiftController {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    ShiftRepository shiftRepository;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public List<Shift> getShiftsByRestaurantAndDates(Integer restaurantId, OffsetDateTime start, OffsetDateTime end) {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        return shiftRepository.findAllByRestaurantAndStartDateTimeBetweenOrEndDateTimeBetween(restaurant, start, end, start, end);
    }
}
