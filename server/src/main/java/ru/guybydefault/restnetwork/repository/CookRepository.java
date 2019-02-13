package ru.guybydefault.restnetwork.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.guybydefault.restnetwork.entity.Cook;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public interface CookRepository  extends CrudRepository<Cook, Integer> {

}
