package ru.guybydefault.restnetwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.guybydefault.restnetwork.entity.Restaurant;
import ru.guybydefault.restnetwork.repository.RestaurantRepository;
import ru.guybydefault.restnetwork.repository.ShiftRepository;

@Controller
@RequestMapping(value = {"/restaurants", "/index.html", ""})
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @RequestMapping(value = {"/restaurants/list", "/index.html", ""})
    public String getRestaurantList(Model model) {
        model.addAttribute("restaurantList", restaurantRepository.findAll());
        return "restaurantList";
    }

    @RequestMapping(value = {"/restaurants/schedule"})
    public String getSchedule(Model model, @RequestParam Integer restaurantId) {
        if (restaurantId == null) {
            throw  new IllegalArgumentException("Restaurant id can not be null");
        }
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        if (restaurant == null) {
            throw  new IllegalArgumentException("Restaurant with ID " + restaurantId + " not found");
        }
        model.addAttribute("restaurant", restaurant);
//        shiftRepository.findAllByRestaurant(restaurant);
        return "schedule";
    }
}
