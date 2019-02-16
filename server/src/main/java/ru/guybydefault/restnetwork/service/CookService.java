package ru.guybydefault.restnetwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.guybydefault.restnetwork.entity.Cook;
import ru.guybydefault.restnetwork.entity.repository.CookRepository;

@Service
public class CookService {
    @Autowired
    private CookRepository cookRepository;

    public Cook findCookById(Integer cookId) throws IllegalArgumentException {
        if (cookId == null) {
            throw new IllegalArgumentException("cookId can not be null");
        }
        Cook cook = cookRepository.findOne(cookId);
        if (cook == null) {
            throw new IllegalArgumentException("Cook with ID " + cookId + " has not been found");
        }
        return cook;
    }
}
