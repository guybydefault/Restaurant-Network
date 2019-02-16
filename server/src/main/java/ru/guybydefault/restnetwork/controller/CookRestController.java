package ru.guybydefault.restnetwork.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.guybydefault.restnetwork.entity.Cook;
import ru.guybydefault.restnetwork.entity.Cuisine;
import ru.guybydefault.restnetwork.entity.CuisineCertification;
import ru.guybydefault.restnetwork.entity.repository.CookRepository;
import ru.guybydefault.restnetwork.entity.repository.CuisineRepository;
import ru.guybydefault.restnetwork.response.ErrorMessage;
import ru.guybydefault.restnetwork.response.RestControllerResponseMessage;
import ru.guybydefault.restnetwork.service.CookService;

@RestController
@RequestMapping("/api/cooks")
public class CookRestController {

    @Autowired
    private CookRepository cookRepository;

    @Autowired
    private CuisineRepository cuisineRepository;

    @Autowired
    private CookService cookService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> exceptionHandler(IllegalArgumentException ex) {
        ErrorMessage error = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<ErrorMessage>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorMessage> exceptionHandler(EmptyResultDataAccessException ex) {
        ErrorMessage error = new ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<ErrorMessage>(error, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "{cookId}", method = RequestMethod.DELETE)
    public ResponseEntity<RestControllerResponseMessage> deleteCook(@PathVariable(value = "cookId") Integer cookId) {
        cookRepository.delete(cookId);
        return new ResponseEntity<RestControllerResponseMessage>(new RestControllerResponseMessage(HttpStatus.OK.value(), "Sucess!"), HttpStatus.OK);
    }

    @RequestMapping(value = "{cookId}/certification", method = RequestMethod.POST)
    public ResponseEntity<RestControllerResponseMessage> changeCookCertification(@PathVariable(value = "cookId") Integer cookId, @RequestParam("cuisineId") Integer cuisineId) {
        if (cuisineId == null) {
            throw new IllegalArgumentException("cuisineId must not be null");
        }
        Cuisine cuisine = cuisineRepository.findOne(cuisineId);
        Cook cook = cookRepository.findOne(cookId);
        if (cook.pullCertification(cuisine)) {
            cookRepository.save(cook);
            return new ResponseEntity<RestControllerResponseMessage>(new RestControllerResponseMessage(HttpStatus.OK.value(), "deleted"), HttpStatus.OK);
        } else {
            cook.getCuisineCertificationList().add(new CuisineCertification(cook, cuisine));
            cookRepository.save(cook);
            return new ResponseEntity<RestControllerResponseMessage>(new RestControllerResponseMessage(HttpStatus.OK.value(), "added"), HttpStatus.OK);
        }
    }

}
