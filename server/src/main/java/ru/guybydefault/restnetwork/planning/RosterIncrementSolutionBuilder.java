package ru.guybydefault.restnetwork.planning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.guybydefault.restnetwork.entity.Cuisine;
import ru.guybydefault.restnetwork.entity.Restaurant;
import ru.guybydefault.restnetwork.planning.genetic.GeneticAlgorithm;
import ru.guybydefault.restnetwork.planning.genetic.GeneticAlgorithmConfiguration;
import ru.guybydefault.restnetwork.repository.CuisineRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
public class RosterBuilder {

    public RosterSolution build(Restaurant restaurant) {
        build(restaurant, 30);
    }

    public RosterSolution build(Restaurant restaurant, int numberOfDays) {
       return build(restaurant, startPlanningTime, endPlanningTime);
    }

    public void build(PlanningData planningData) {
//        int workingDayHours = restaurant.getWorkingDayHours();


//
//        GeneticAlgorithmConfiguration configuration = GeneticAlgorithmConfiguration.buildDefaultParametrization();
//        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(data, configuration);
//
//        Population population = new Population(configuration.populationSize, data).sortByScore();
//        System.out.println(population.getRosterSolutionList().get(0).getHardSoftScore());
//
//        for (int generationNumber = 0; generationNumber < 1000; generationNumber++) {
//            population = geneticAlgorithm.evolve(population).sortByScore();
//            System.out.println(population.getRosterSolutionList().get(0).getHardSoftScore());
//        }
//        RosterSolution bestRosterSolution = population.getRosterSolutionList().get(0);
//        System.out.println(bestRosterSolution.getHardSoftScore());
//
//        bestRosterSolution.getShiftDayList().forEach(day -> {
//            day.forEach(shift -> {
//                entityManager.persist(shift);
//            });
//        });
    }

}
