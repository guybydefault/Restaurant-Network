package ru.guybydefault.restnetwork.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    @Test
    void getWorkingDayHours() {
        Restaurant restaurant = new Restaurant();
        restaurant.setStartingHour(0);
        restaurant.setClosingHour(0);
        Assertions.assertEquals(restaurant.getWorkingDayHours(), 24);
        restaurant.setStartingHour(10);
        restaurant.setClosingHour(23);
        Assertions.assertEquals(restaurant.getWorkingDayHours(), 13);
        restaurant.setStartingHour(2);
        restaurant.setClosingHour(0);
        Assertions.assertEquals(restaurant.getWorkingDayHours(), 22);
        restaurant.setStartingHour(2);
        restaurant.setClosingHour(1);
        Assertions.assertEquals(restaurant.getWorkingDayHours(), 23);
    }
}