package ru.guybydefault.restnetwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.guybydefault.restnetwork.entity.Cuisine;
import ru.guybydefault.restnetwork.repository.CuisineRepository;

import java.util.List;

@RestController
@RequestMapping("/api/cuisines")
public class CuisineRestController {
    @Autowired
    private CuisineRepository cuisineRepository;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Iterable<Cuisine> getAllCuisines() {
        return cuisineRepository.findAll();
    }
}
