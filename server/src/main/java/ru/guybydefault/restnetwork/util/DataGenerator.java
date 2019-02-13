package ru.guybydefault.restnetwork.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.guybydefault.restnetwork.entity.*;
import ru.guybydefault.restnetwork.planning.genetic.GeneticAlgorithm;
import ru.guybydefault.restnetwork.planning.genetic.GeneticAlgorithmConfiguration;
import ru.guybydefault.restnetwork.util.StringDataGenerator;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Component
@Transactional
public class DataGenerator {

    @Autowired
    private EntityManager entityManager;

    private final int RESTAURANT_NUMBER = 17;

    private final int MIN_COOKS_PER_RESTAURANT = 15;
    private final int MAX_COOKS_PER_RESTAURANT = 20;

    private final int CERTIFICATIONS_PER_CUISINE_ON_RESTAURANT = 15;

    private final OffsetDateTime START_PERIOD = OffsetDateTime.of(2019, 02, 01, 10, 0, 0, 0, ZoneOffset.UTC);
    private final OffsetDateTime END_PERIOD = OffsetDateTime.of(2019, 03, 01, 0, 0, 0, 0, ZoneOffset.UTC);


    public void buildSchedule() {

        Random random = new Random(37);

        List<Restaurant> restaurantList = new LinkedList<>();
        StringDataGenerator locationGenerator = StringDataGenerator.buildLocationNames();
        locationGenerator.predictMaximumSizeAndReset(RESTAURANT_NUMBER);

        StringDataGenerator restaurantNamesGenerator = StringDataGenerator.buildRestaurantNames();
        restaurantNamesGenerator.predictMaximumSizeAndReset(RESTAURANT_NUMBER);

        for (int i = 0; i < RESTAURANT_NUMBER; i++) {
            Restaurant restaurant = new Restaurant();
            restaurant.setName(restaurantNamesGenerator.generateNextValue());
            restaurant.setAddress(locationGenerator.generateNextValue());
            restaurant.setCooks(new LinkedList<>());
            restaurantList.add(restaurant);
            entityManager.persist(restaurant);
        }

        StringDataGenerator namesGenerator = StringDataGenerator.buildFullNames();
        namesGenerator.predictMaximumSizeAndReset(MAX_COOKS_PER_RESTAURANT * RESTAURANT_NUMBER);
        List<Cook> cookList = new LinkedList<>();
        for (Restaurant restaurant : restaurantList) {
            for (int i = 0; i < random.nextInt(MAX_COOKS_PER_RESTAURANT - MIN_COOKS_PER_RESTAURANT + 1) + MIN_COOKS_PER_RESTAURANT; i++) {
                Cook cook = new Cook();
                cook.setFullName(namesGenerator.generateNextValue());
                cook.setRestaurant(restaurant);
                cook.setCuisineCertificationList(new ArrayList<>());
                cook.setCookPreferences(new CookPreferences(random.nextInt(7) + 4, random.nextInt(7) + 4,
                        random.nextBoolean(), random.nextBoolean(), random.nextBoolean() ? 5 : 2, 2));
                restaurant.getCooks().add(cook);
                cookList.add(cook);
                entityManager.persist(cook);
            }
        }



        List<Cuisine> cuisineList = new LinkedList<>();
        cuisineList.add(new Cuisine("Russian"));
        cuisineList.add(new Cuisine("Italian"));
        cuisineList.add(new Cuisine("Japanese"));
        cuisineList.forEach(c -> {entityManager.persist(c);});

        for (Cook cook : cookList) {
            Collections.shuffle(cuisineList);
            double probability = 1;
            for (Cuisine cuisine : cuisineList) {
                if (Math.random() < probability) {
                    cook.getCuisineCertificationList().add(new CuisineCertification(cook, cuisine));
                }
                probability /= 3;
            }

        }

//        OffsetDateTime startPlanningTime = OffsetDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT, ZoneOffset.ofHours(3)).withHour(10);
//        OffsetDateTime endPlanningTime = startPlanningTime.plusDays(30);
//        PlanningData data = new PlanningData(startPlanningTime, endPlanningTime, 14, 4, 10, cuisineList, cookList, restaurantList.get(0));
//
//        GeneticAlgorithmConfiguration configuration = GeneticAlgorithmConfiguration.buildDefaultParametrization();
//        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(data, configuration);
//
//        Population population = new Population(configuration.populationSize, data).sortByScore();
//        System.out.println(population.getRosterIncrementSolutionList().get(0).getHardSoftScore());
//
//        for (int generationNumber = 0; generationNumber < 1000; generationNumber++) {
//            population = geneticAlgorithm.evolve(population).sortByScore();
//            System.out.println(population.getRosterIncrementSolutionList().get(0).getHardSoftScore());
//        }
//        RosterIncrementSolution bestRosterIncrementSolution = population.getRosterIncrementSolutionList().get(0);
//        System.out.println(bestRosterIncrementSolution.getHardSoftScore());
//
//        bestRosterIncrementSolution.getShiftDayList().forEach(day -> {
//            day.forEach(shift -> {
//                entityManager.persist(shift);
//            });
//        });
//        entityManager.flush();
    }




//    List<Shift> shiftList = new LinkedList<>();
//        for (int i = 0; i < ChronoUnit.DAYS.between(START_PERIOD, END_PERIOD); i++) {
//            OffsetDateTime dayStart = START_PERIOD.plusDays(i);
//            for (Restaurant restaurant : restaurantList) {
//                for (Cuisine cuisine : cuisineList) {
//                    shiftList.add(new Shift(restaurant, null, cuisine, dayStart, dayStart.plusHours(5)));
//                    shiftList.add(new Shift(restaurant, null, cuisine, dayStart.plusHours(5), dayStart.plusHours(10)));
//                    shiftList.add(new Shift(restaurant, null, cuisine, dayStart.plusHours(10), dayStart.plusHours(14)));
//                }
//            }
//        }
}
