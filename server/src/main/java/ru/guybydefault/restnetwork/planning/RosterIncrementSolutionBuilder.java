package ru.guybydefault.restnetwork.planning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RosterIncrementSolutionBuilder {

    private PlanningConfiguration conf;

    private int reproductionResetEliteDayIndex;

    private int dayIndex = 0;
    private int dayEndPlanning;

    private final Comparator<RosterIncrementSolution> rosterSolutionComparator = new Comparator<RosterIncrementSolution>() {
        @Override
        public int compare(RosterIncrementSolution o1, RosterIncrementSolution o2) {
            if (o1.getShiftDayIndex() != o2.getShiftDayIndex()) {
                return o2.getShiftDayIndex() - o1.getShiftDayIndex();
            }
            return o1.getHardSoftScore().compareTo(o2.getHardSoftScore());
        }
    };

    ArrayList<RosterIncrementSolution> rosterIncrementSolutionArrayList = new ArrayList<>();

    public RosterIncrementSolutionBuilder(PlanningConfiguration conf) {
        this.conf = conf;
    }

    private void nextDay() {
        List<List<RosterIncrementSolution>> branchingSolutions = new ArrayList<>();
        for (RosterIncrementSolution rosterIncrementSolution : rosterIncrementSolutionArrayList) {
            rosterIncrementSolution.setReproductionRate(1d / (rosterIncrementSolution.getGeneration() + 1) * conf.getStartingReproductionRate());
            while (rosterIncrementSolution.getShiftDayIndex() < dayIndex + 1) {
                branchingSolutions.add(rosterIncrementSolution.nextStep());
            }
        }
        if (dayIndex == reproductionResetEliteDayIndex) {
            rosterIncrementSolutionArrayList.sort(rosterSolutionComparator);
            List<RosterIncrementSolution> eliteSolutions = new ArrayList<>();
            for (int i = 0; i < conf.getEliteSolutionsNumber(); i++) {
                RosterIncrementSolution eliteSolution = rosterIncrementSolutionArrayList.get(i);
                eliteSolution.setGeneration(0);
            }
        }
        if (rosterIncrementSolutionArrayList.size() > conf.getMaxSolutionSize()) {
            rosterIncrementSolutionArrayList.sort(rosterSolutionComparator);
            rosterIncrementSolutionArrayList.subList(rosterIncrementSolutionArrayList.size() - conf.getMaxSolutionSize(), rosterIncrementSolutionArrayList.size()).clear();
        }
        branchingSolutions.forEach(solutionBranch -> {
            rosterIncrementSolutionArrayList.addAll(solutionBranch);
        });
        dayIndex++;
    }

    public RosterIncrementSolution build(PlanningData planningData) {
        reproductionResetEliteDayIndex = conf.getReproductionResetEliteInterval();
        dayIndex = 0;
        dayEndPlanning = (int) ChronoUnit.DAYS.between(planningData.getStartPlanningDateTime(), planningData.getEndPlanningDateTime());

        rosterIncrementSolutionArrayList.add(new RosterIncrementSolution(planningData));
        while (dayIndex != dayEndPlanning) {
            nextDay();
        }
        rosterIncrementSolutionArrayList.sort(rosterSolutionComparator);
        return rosterIncrementSolutionArrayList.get(0);
    }
}
