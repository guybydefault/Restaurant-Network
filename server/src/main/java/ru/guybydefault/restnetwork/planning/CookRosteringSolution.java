package ru.guybydefault.restnetwork.planning;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import ru.guybydefault.restnetwork.entity.Cook;
import ru.guybydefault.restnetwork.entity.Cuisine;
import ru.guybydefault.restnetwork.entity.Shift;

import java.util.List;

@PlanningSolution
public class CookRosteringSolution {

    private List<Cuisine> cuisineList;

    private List<Cook> cookList;

    private List<Shift> shiftList;

    private HardSoftScore score;

    @ValueRangeProvider(id = "cookRange")
    public List<Cook> getCookList() {
        return cookList;
    }

    public void setCookList(List<Cook> cookList) {
        this.cookList = cookList;
    }

    @ProblemFactCollectionProperty
    public List<Cuisine> getCuisineList() {
        return cuisineList;
    }

    public void setCuisineList(List<Cuisine> cuisineList) {
        this.cuisineList = cuisineList;
    }

    @PlanningEntityCollectionProperty
    public List<Shift> getShiftList() {
        return shiftList;
    }

    public void setShiftList(List<Shift> shiftList) {
        this.shiftList = shiftList;
    }

    @PlanningScore
    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }
}
