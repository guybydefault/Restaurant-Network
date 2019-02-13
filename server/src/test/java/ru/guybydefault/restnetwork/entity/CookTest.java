package ru.guybydefault.restnetwork.entity;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.guybydefault.restnetwork.repository.CookRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CookTest {

    @Autowired
    private EntityManager em;

    @Test
    public void testEmbeddedObject() {
        Cook cook = new Cook();
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Dodo");
        restaurant.setAddress("Lenina st.");
        em.persist(restaurant);
        cook.setRestaurant(restaurant);
        cook.setFullName("John Smith");
        cook.setCookPreferences(new CookPreferences(1, 1, false, true, 5, 2));
        em.persist(cook);
        em.flush();
        em.detach(cook);

        Cook cookFromDB = em.find(Cook.class, cook.getId());

        assertEquals(cook.getCookPreferences(), cookFromDB.getCookPreferences());
    }
}