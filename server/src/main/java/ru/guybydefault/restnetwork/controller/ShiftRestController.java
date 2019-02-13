package ru.guybydefault.restnetwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.guybydefault.restnetwork.entity.Cuisine;
import ru.guybydefault.restnetwork.entity.Restaurant;
import ru.guybydefault.restnetwork.entity.Shift;
import ru.guybydefault.restnetwork.planning.PlanningData;
import ru.guybydefault.restnetwork.planning.RosterIncrementSolutionBuilder;
import ru.guybydefault.restnetwork.repository.CuisineRepository;
import ru.guybydefault.restnetwork.response.ErrorMessage;
import ru.guybydefault.restnetwork.repository.RestaurantRepository;
import ru.guybydefault.restnetwork.repository.ShiftRepository;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
public class ShiftRestController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private CuisineRepository cuisineRepository;

    @Autowired
    private RosterIncrementSolutionBuilder rosterIncrementSolutionBuilder;


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> exceptionHandler(Exception ex) {
        ErrorMessage error = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<ErrorMessage>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     *
     * @param restaurantId
     * @return
     * @throws IllegalArgumentException if restaurantId is null or restaurant by this id has not been found
     */
    private Restaurant findRestaurantById(Integer restaurantId) throws IllegalArgumentException {
        if (restaurantId == null) {
            throw new IllegalArgumentException("restaurantId can not be null");
        }
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant with ID " + restaurantId + " has not been found");
        }
        return restaurant;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public List<Shift> getShiftsByRestaurantAndDates(@RequestParam Integer restaurantId,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end) {
       Restaurant restaurant = findRestaurantById(restaurantId);
        return shiftRepository.findAllByRestaurantAndStartDateTimeBetweenOrEndDateTimeBetween(restaurant, start, end, start, end);
    }

    @RequestMapping(value = "buildRoster", method = RequestMethod.GET)
    public String buildSchedule(Restaurant restaurant) {
        OffsetDateTime startPlanningTime = OffsetDateTime.of(LocalDate.now(), LocalTime.of(restaurant.getStartingHour(), 0), restaurant.getZoneOffset());
        OffsetDateTime endPlanningTime = startPlanningTime.plusDays(30).withHour(restaurant.getClosingHour());

        List<Cuisine> cuisineList = new ArrayList<>();
        cuisineRepository.findAll().forEach(c -> {cuisineList.add(c);});

        PlanningData planningData = new PlanningData(startPlanningTime, endPlanningTime, 4, 14, cuisineList, restaurant.getCooks(), restaurant);

        return "hey";
    }
}
