package ru.guybydefault.restnetwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.guybydefault.restnetwork.entity.Restaurant;
import ru.guybydefault.restnetwork.repository.CookRepository;
import ru.guybydefault.restnetwork.repository.RestaurantRepository;

import java.util.stream.Collectors;

@RequestMapping(value = "/cooks")
@Controller
public class CookController {
    @Autowired
    private CookRepository cookRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String getAllCooks(Model model, @RequestParam(required = false) Integer restaurantId) {
        if (restaurantId != null) {
            Restaurant restaurant = restaurantRepository.findOne(restaurantId);
            if (restaurant == null) {
                throw new IllegalArgumentException("Restaurant with ID " + restaurantId + " has not been found");
            }
            model.addAttribute("restaurant", restaurant);
            model.addAttribute("cookList", cookRepository.findAllByRestaurant(restaurantId));
            return "restaurantCookList";
        }
        model.addAttribute("cookList", cookRepository.findAll());
        return "fullCookList";
    }


}
