package ru.guybydefault.restnetwork.planning;

import ru.guybydefault.restnetwork.entity.Cook;
import ru.guybydefault.restnetwork.entity.CookPreferences;
import ru.guybydefault.restnetwork.entity.Cuisine;
import ru.guybydefault.restnetwork.entity.Shift;
import ru.guybydefault.restnetwork.util.Util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RosterIncrementSolution implements Cloneable {

    private ArrayList<ArrayList<Shift>> shiftDayList;


    private int generation = 0;
    private int shiftDayIndex = 0;
    private int cuisineIndex = 0;
    private int[] shiftTemplate;
    private List<Cook> selectedCookList;
    private List<Cook> availableCookList;
    private List<Integer> shiftAssignment;

    private PlanningData data;

    private HardSoftScore hardSoftScore;

    private PlanningStage planningStage = PlanningStage.SHIFT_TEMPLATE_SELECTION;


    private HashMap<Cook, CookShiftSequenceInfo> shiftSequenceInfoHashMap;

    private double reproductionRate;


    public RosterIncrementSolution(PlanningData data) {
        this.hardSoftScore = new HardSoftScore(0, 0);
        this.data = data;
        this.shiftDayList = new ArrayList<>((int) ChronoUnit.DAYS.between(data.getStartPlanningDateTime(), data.getEndPlanningDateTime()) + 1);
        this.shiftDayList.add(new ArrayList<>()); // for the first initial day
        this.shiftSequenceInfoHashMap = new HashMap<>();
        for (List<Cook> cookList : data.getCuisineCookHashMap().values()) {
            for (Cook cook : cookList) {
                shiftSequenceInfoHashMap.put(cook, new CookShiftSequenceInfo());
            }
        }
    }

    private class CookShiftSequenceInfo implements Cloneable {
        /**
         * Stores number of consecutive shifts cook should have
         */
        int remainingWorkingDays;
        /**
         * Stores number of consecutive week days should have
         */
        int remainingWeekDays;

        boolean isFreeToday = true;

        private boolean isAvailable() {
            return remainingWeekDays == 0 && remainingWorkingDays >= 0 && isFreeToday;
        }

        @Override
        public Object clone() {
            CookShiftSequenceInfo cookShiftSequenceInfo = null;
            try {
                cookShiftSequenceInfo = (CookShiftSequenceInfo) super.clone();
            } catch (CloneNotSupportedException e) {
                // this shouldn't happen, since we are Cloneable
                throw new InternalError(e);
            }
            return cookShiftSequenceInfo;
        }
    }

    @Override
    public Object clone() {
        RosterIncrementSolution copy = null;
        try {
            copy = (RosterIncrementSolution) super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }

        copy.setShiftDayList(new ArrayList<>());
        for (ArrayList<Shift> shiftDay : shiftDayList) {
            copy.getShiftDayList().add((ArrayList<Shift>) shiftDay.clone()); // shallow copy is what we need cause it is not supposed that shift objects will be changed
        }

        copy.shiftSequenceInfoHashMap = new HashMap<>();
        for (Map.Entry<Cook, CookShiftSequenceInfo> entry : shiftSequenceInfoHashMap.entrySet()) {
            copy.shiftSequenceInfoHashMap.put(entry.getKey(), (CookShiftSequenceInfo) entry.getValue().clone());
        }
        copy.setHardSoftScore((HardSoftScore) hardSoftScore.clone());
        copy.incrementGeneration();
        return copy;
    }


    private List<Cook> getAvailableCooksForCuisine(Cuisine cuisine) {
        return data.getCooksByCuisine(cuisine).stream().filter(c -> shiftSequenceInfoHashMap.get(c).isAvailable()).collect(Collectors.toList());
    }

    private List<int[]> getPreferredShiftTemplatesByAvailableCooks(List<Cook> availableCookList) {
        return data.getShiftDayTemplates().stream().filter(templateIntArray -> {
            return templateIntArray.length <= availableCookList.size();
        }).collect(Collectors.toList());
    }

    private Cuisine getCurrentCuisine() {
        return data.getCuisineList().get(cuisineIndex);
    }

    private RosterIncrementSolution createNextGeneration(List<RosterIncrementSolution> childSolutions) {
        if (Math.random() < reproductionRate) {
            RosterIncrementSolution newSolution = (RosterIncrementSolution) this.clone();
            childSolutions.add(newSolution);
            return newSolution;
        }
        return null;
    }

    // TODO refactor (at least split into separate methods)
    // TODO newSolutions contains same tmp variables (ex: cooksAvailableList) - this can lead to the bugs in future
    public List<RosterIncrementSolution> nextStep() {
        List<RosterIncrementSolution> otherPossibleSolutions = new ArrayList<>();
        switch (planningStage) {
            case SHIFT_TEMPLATE_SELECTION:
                availableCookList = getAvailableCooksForCuisine(getCurrentCuisine());
                if (availableCookList.size() == 0) {
                    /* no one is free */
                    Instant shiftStart = data.getStartPlanningDateTime().withHour(data.getRestaurant().getStartingHour()).plusDays(shiftDayIndex).toInstant();
                    Instant shiftEnd = shiftStart.plus(data.getWorkingDayHours(), ChronoUnit.HOURS);
                    Shift mockShift = new Shift(data.getRestaurant(), null, getCurrentCuisine(), shiftStart, shiftEnd);
                    shiftDayList.get(shiftDayIndex).add(mockShift);

                    hardSoftScore.minusHard(data.getMinShiftsInTemplate());
                    planningStage = PlanningStage.SKIP_CUISINE;
                    break;
                }
                List<int[]> shiftDayTemplates;
                if (data.getMinShiftsInTemplate() > availableCookList.size()) {
                    shiftDayTemplates = data.getShiftDayTemplates();
                    hardSoftScore.minusHard(data.getMinShiftsInTemplate() - availableCookList.size());
                } else {
                    // now we now there are shift templates that can be fully filled with our cooks, so we need them instead of too large shift templates where for some shifts cook may be null
                    shiftDayTemplates = getPreferredShiftTemplatesByAvailableCooks(availableCookList);
                }
                shiftTemplate = shiftDayTemplates.get(0);
                planningStage = PlanningStage.COOKS_SELECTION;
                for (int i = 1; i < shiftDayTemplates.size(); i++) {
                    RosterIncrementSolution childSolution = createNextGeneration(otherPossibleSolutions);
                    if (childSolution != null) {
                        childSolution.setShiftTemplate(shiftDayTemplates.get(i));
                    }
                }
                break;
            case COOKS_SELECTION:
                List<List<Cook>> cookCombinations = Util.generateCombinations(availableCookList, Integer.min(availableCookList.size(), shiftTemplate.length));
                selectedCookList = cookCombinations.get(0);
                planningStage = PlanningStage.SHIFT_ASSIGNMENT_SELECTION;
                for (int i = 1; i < cookCombinations.size(); i++) {
                    RosterIncrementSolution childSolution = createNextGeneration(otherPossibleSolutions);
                    if (childSolution != null) {
                        childSolution.setSelectedCookList(cookCombinations.get(i));
                    }
                }
                break;
            case SHIFT_ASSIGNMENT_SELECTION:
                for (Cook cook : selectedCookList) {
                    CookShiftSequenceInfo cookShiftSequenceInfo = shiftSequenceInfoHashMap.get(cook);
                    cookShiftSequenceInfo.isFreeToday = false;
                    if (cookShiftSequenceInfo.remainingWorkingDays == 0) {
                        // means that it is his first shift in a row
                        cookShiftSequenceInfo.remainingWorkingDays = cook.getCookPreferences().getWorkDays();
                    }
                    cookShiftSequenceInfo.remainingWorkingDays--;
                    if (cookShiftSequenceInfo.remainingWorkingDays == 0) {
                        cookShiftSequenceInfo.remainingWeekDays = cook.getCookPreferences().getWeekDays();
                    }
                }

                List<List<Integer>> shiftAssignments = Util.generatePermutations(IntStream.range(0, shiftTemplate.length).boxed().collect(Collectors.toList()), selectedCookList.size());
                shiftAssignment = shiftAssignments.get(0);
                planningStage = PlanningStage.COOKS_ROSTERING;
                for (int i = 1; i < shiftAssignments.size(); i++) {
                    RosterIncrementSolution childSolution = createNextGeneration(otherPossibleSolutions);
                    if (childSolution != null) {
                        childSolution.setShiftAssignment(shiftAssignments.get(i));
                    }
                }
                break;
            case COOKS_ROSTERING:
                // starting at starting restaurant hours on current day
                Instant shiftStart = data.getStartPlanningDateTime().withHour(data.getRestaurant().getStartingHour()).plusDays(shiftDayIndex).toInstant();
                Instant shiftEnd;
                for (int i = 0; i < shiftTemplate.length; i++) {
                    shiftEnd = shiftStart.plus(shiftTemplate[i], ChronoUnit.HOURS);

                    Shift shift = new Shift(data.getRestaurant(), null, getCurrentCuisine(), shiftStart, shiftEnd);
                    if (shiftAssignment.indexOf(i) != -1) {
                        // setting cooks for the generated shifts according to the template and all cook rostering permutations among the shifts in template
                        Cook cook = selectedCookList.get(shiftAssignment.indexOf(i));
                        shift.setCook(cook);
                        CookPreferences cookPreferences = cook.getCookPreferences();
                        if (!cookPreferences.isAvailableLateShifts() && shift.isLate() || !cookPreferences.isAvailableEarlyShifts() && shift.isEarly()) {
                            hardSoftScore.minusHard(1);
                        }
                        if (shift.getDurationHours() > cookPreferences.getAvailableHoursPerDay()) {
                            hardSoftScore.minusHard(((int) shift.getDurationHours() - cookPreferences.getAvailableHoursPerDay()));
                        }
                        hardSoftScore.minusSoft(Math.abs((int) shift.getDurationHours() - cookPreferences.getPreferredHoursPerDay()));

                    }
                    shiftDayList.get(shiftDayIndex).add(shift);
                    shiftStart = shiftEnd;
                }
                planningStage = PlanningStage.NEXT_CUISINE_PREPARATION;
                break;
            case SKIP_CUISINE:
            case NEXT_CUISINE_PREPARATION:
                if (cuisineIndex + 1 >= data.getCuisineList().size()) {
                    planningStage = PlanningStage.NEXT_DAY_PREPARATION;
                } else {
                    cuisineIndex++;
                    planningStage = PlanningStage.SHIFT_TEMPLATE_SELECTION;
                }
                break;
            case NEXT_DAY_PREPARATION:
                // TODO hardsoftscore depending on cooks having workingDaysRemaining left without job
                shiftDayIndex++;
                cuisineIndex = 0;
                shiftDayList.add(new ArrayList<>());
                for (CookShiftSequenceInfo cookShiftSequenceInfo : shiftSequenceInfoHashMap.values()) {
                    if (cookShiftSequenceInfo.isFreeToday && cookShiftSequenceInfo.remainingWeekDays > 0) {
                        cookShiftSequenceInfo.remainingWeekDays--;
                    }
                    cookShiftSequenceInfo.isFreeToday = true;

                }
                planningStage = PlanningStage.SHIFT_TEMPLATE_SELECTION;
                break;
        }

        return otherPossibleSolutions;
    }

    public ArrayList<ArrayList<Shift>> getShiftDayList() {
        return shiftDayList;
    }

    public void setShiftDayList(ArrayList<ArrayList<Shift>> shiftDayList) {
        this.shiftDayList = shiftDayList;
    }

    public PlanningData getData() {
        return data;
    }

    public void setData(PlanningData data) {
        this.data = data;
    }

    public HardSoftScore getHardSoftScore() {
        return hardSoftScore;
    }


    public HashMap<Cook, CookShiftSequenceInfo> getShiftSequenceInfoHashMap() {
        return shiftSequenceInfoHashMap;
    }

    public void setShiftSequenceInfoHashMap(HashMap<Cook, CookShiftSequenceInfo> shiftSequenceInfoHashMap) {
        this.shiftSequenceInfoHashMap = shiftSequenceInfoHashMap;
    }

    public void setHardSoftScore(HardSoftScore hardSoftScore) {
        this.hardSoftScore = hardSoftScore;
    }

    public int[] getShiftTemplate() {
        return shiftTemplate;
    }

    public void setShiftTemplate(int[] shiftTemplate) {
        this.shiftTemplate = shiftTemplate;
    }

    public List<Cook> getSelectedCookList() {
        return selectedCookList;
    }

    public void setSelectedCookList(List<Cook> selectedCookList) {
        this.selectedCookList = selectedCookList;
    }

    public List<Integer> getShiftAssignment() {
        return shiftAssignment;
    }

    public void setShiftAssignment(List<Integer> shiftAssignment) {
        this.shiftAssignment = shiftAssignment;
    }

    public int getShiftDayIndex() {
        return shiftDayIndex;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public void incrementGeneration() {
        generation++;
    }

    public double getReproductionRate() {
        return reproductionRate;
    }

    public void setReproductionRate(double reproductionRate) {
        this.reproductionRate = reproductionRate;
    }
}
