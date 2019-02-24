package ru.guybydefault.restnetwork.planning;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import ru.guybydefault.restnetwork.entity.Cuisine;
import ru.guybydefault.restnetwork.entity.Restaurant;
import ru.guybydefault.restnetwork.entity.Shift;
import ru.guybydefault.restnetwork.entity.repository.CuisineRepository;
import ru.guybydefault.restnetwork.entity.repository.RestaurantRepository;
import ru.guybydefault.restnetwork.entity.repository.ShiftRepository;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PlanningService implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CuisineRepository cuisineRepository;


    @Autowired
    private ShiftRepository shiftRepository;

    public List<Shift> solve(PlanningData data) {
        SolverFactory<CookRosteringSolution> solutionSolverFactory = SolverFactory.createFromXmlResource("solver.xml");
        TerminationConfig terminationConfig = new TerminationConfig();
        terminationConfig.setSecondsSpentLimit(30L);
        solutionSolverFactory.getSolverConfig().setTerminationConfig(terminationConfig);
        Solver<CookRosteringSolution> solver = solutionSolverFactory.buildSolver();

        CookRosteringSolution cookRosteringSolution = new CookRosteringSolution();
        cookRosteringSolution.setCuisineList(data.getCuisineList());
cookRosteringSolution.setCookList(data.getRestaurant().getCooks());
        List<Shift> shiftList = new ArrayList<>();
        int[] shiftDayTemplate = data.getShiftDayTemplates().get(0);

        int shiftDayIndex = 0;

        while (shiftDayIndex != 30) {
            Instant dayShiftStart = data.getStartPlanningDateTime().withHour(data.getRestaurant().getStartingHour()).plusDays(shiftDayIndex).toInstant();
//            Instant dayShiftEnd = dayShiftStart.plus(data.getWorkingDayHours(), ChronoUnit.HOURS);
            for (Cuisine cuisine : data.getCuisineList()) {
                Instant currentShiftStart = dayShiftStart;
                for (int i = 0; i < shiftDayTemplate.length; i++) {
                    Instant currentShiftEnd = currentShiftStart.plus(shiftDayTemplate[i], ChronoUnit.HOURS);
                    Shift shift = new Shift(data.getRestaurant(), null, cuisine, currentShiftStart, currentShiftEnd);
                    shiftList.add(shift);
                    currentShiftStart = currentShiftEnd;
                }
            }
            shiftDayIndex++;
        }

        cookRosteringSolution.setShiftList(shiftList);

        CookRosteringSolution bestSolution = solver.solve(cookRosteringSolution);
        return bestSolution.getShiftList();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Restaurant restaurant = restaurantRepository.findOne(1);
        OffsetDateTime startPlanningTime = OffsetDateTime.of(LocalDate.now(), LocalTime.of(restaurant.getStartingHour(), 0), restaurant.getZoneOffset());
        OffsetDateTime endPlanningTime = startPlanningTime.plusDays(30).withHour(restaurant.getClosingHour());

        List<Cuisine> cuisineList = new ArrayList<>();
        cuisineRepository.findAll().forEach(cuisineList::add);

        PlanningData planningData = new PlanningData(startPlanningTime, endPlanningTime, 4, 10, cuisineList, restaurant.getCooks(), restaurant);
        List<Shift> solution = solve(planningData);
        shiftRepository.save(solution);
        solve(planningData);
    }
}
