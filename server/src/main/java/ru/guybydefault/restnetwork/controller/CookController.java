package ru.guybydefault.restnetwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.guybydefault.restnetwork.entity.Cook;
import ru.guybydefault.restnetwork.entity.Cuisine;
import ru.guybydefault.restnetwork.entity.Restaurant;
import ru.guybydefault.restnetwork.entity.repository.CookRepository;
import ru.guybydefault.restnetwork.entity.repository.CuisineRepository;
import ru.guybydefault.restnetwork.entity.repository.RestaurantRepository;
import ru.guybydefault.restnetwork.entity.repository.ShiftRepository;
import ru.guybydefault.restnetwork.planning.PlanningService;
import ru.guybydefault.restnetwork.service.CookService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Iterator;
import java.util.List;

@RequestMapping(value = "/cooks")
@Controller
@Transactional // todo bad practice; should create service layer
public class CookController {
    @Autowired
    private CookRepository cookRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private CuisineRepository cuisineRepository;

    @Autowired
    private CookService cookService;


    @ModelAttribute("cuisines")
    public Iterable<Cuisine> cuisineListModelAttribute() {
        return cuisineRepository.findAll();
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String getCooks(Model model, @RequestParam(required = false) Integer restaurantId) {
        if (restaurantId != null) {
            Restaurant restaurant = restaurantRepository.findOne(restaurantId);
            if (restaurant == null) {
                throw new IllegalArgumentException("Restaurant with ID " + restaurantId + " has not been found");
            }
            model.addAttribute("restaurant", restaurant);
            model.addAttribute("cookList", cookRepository.findAllByRestaurantByOrderByIdAsc(restaurantId));
            return "restaurantCookList";
        }
        model.addAttribute("cookList", cookRepository.findAllByOrderByIdAsc());
        return "fullCookList";
    }

    @RequestMapping(value = "createCook", method = RequestMethod.GET)
    public String initSubmitCookForm(Model model, Cook cook) {
        model.addAttribute("restaurants", restaurantRepository.findAll());
        return "cookForm";
    }

    @RequestMapping(value = "cook/{cookId}/update", method = RequestMethod.GET)
    public String initUpdateCookForm(@PathVariable("cookId") Integer cookId, Model model) {
        return initSubmitCookForm(model, cookService.findCookById(cookId));
    }

    @RequestMapping(value = "cook", method = RequestMethod.POST)
    public String submitPostCookForm(@Valid Cook cook, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurants", restaurantRepository.findAll());
            return "cookForm";
        }
        if (!cook.isNew()) {
            Cook existingCook = cookRepository.findOne(cook.getId());
            cook.setCuisineCertificationList(existingCook.getCuisineCertificationList());
            if (!existingCook.getRestaurant().equals(cook.getRestaurant())) {
                shiftRepository.setCookToNullByCook(existingCook);
            }
            cook.setCuisineCertificationList(existingCook.getCuisineCertificationList());
        }
        cookRepository.save(cook);
        return "redirect:/cooks/list";
    }
}
