package ru.guybydefault.restnetwork.planning;

import ru.guybydefault.restnetwork.entity.Cook;
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


    private int shiftDayIndex = 0;
    private int cuisineIndex = 0;
    private int[] shiftTemplate;
    private List<Cook> selectedCookList;
    private List<Cook> availableCookList;
    private List<Integer> shiftAssignment;

    private PlanningData data;

    private HardSoftScore hardSoftScore;

    private PlanningStage planningStage;


    private HashMap<Cook, CookShiftSequenceInfo> shiftSequenceInfoHashMap = new HashMap<Cook, CookShiftSequenceInfo>();


    public RosterIncrementSolution(PlanningData data) {
        this.hardSoftScore = new HardSoftScore(0, 0);
        this.data = data;
        this.shiftDayList = new ArrayList<>((int) ChronoUnit.DAYS.between(data.getStartPlanningDateTime(), data.getEndPlanningDateTime()) + 1);
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

        for (ArrayList<Shift> shiftDay : shiftDayList) {
            copy.getShiftDayList().add((ArrayList<Shift>) shiftDay.clone()); // shallow copy is what we need cause it is not supposed that shift objects will be changed
        }
        for (Map.Entry<Cook, CookShiftSequenceInfo> entry : shiftSequenceInfoHashMap.entrySet()) {
            copy.shiftSequenceInfoHashMap.put(entry.getKey(), (CookShiftSequenceInfo) entry.getValue().clone());
        }
        copy.setHardSoftScore((HardSoftScore) hardSoftScore.clone());

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


    public List<RosterIncrementSolution> nextStep() {
        List<RosterIncrementSolution> otherPossibleSolutions = new ArrayList<>();
        switch (planningStage) {
            case SHIFT_TEMPLATE_SELECTION:
                availableCookList = getAvailableCooksForCuisine(getCurrentCuisine());
                if (availableCookList.size() == 0) {
                    /* no one is free */
                    hardSoftScore.addHard(data.getMinShiftsInTemplate());
                    planningStage = PlanningStage.SKIP_CUISINE;
                    break;
                }
                List<int[]> shiftDayTemplates;
                if (data.getMinShiftsInTemplate() > availableCookList.size()) {
                    shiftDayTemplates = data.getShiftDayTemplates();
                    hardSoftScore.addHard(data.getMinShiftsInTemplate() - availableCookList.size());
                } else {
                    // now we now there are shift templates that can be fully filled with our cooks, so we need them instead of too large shift templates where for some shifts cook may be null
                    shiftDayTemplates = getPreferredShiftTemplatesByAvailableCooks(availableCookList);
                }
                shiftTemplate = shiftDayTemplates.get(0);
                shiftDayTemplates.remove(0);
                planningStage = PlanningStage.COOKS_SELECTION;
                for (int[] shiftDayTemplate : shiftDayTemplates) {
                    RosterIncrementSolution newSolution = (RosterIncrementSolution) this.clone();
                    newSolution.setShiftTemplate(shiftDayTemplate);
                    otherPossibleSolutions.add(newSolution);
                }
                break;
            case COOKS_SELECTION:
                List<List<Cook>> cookCombinations = Util.generateCombinations(availableCookList, Integer.min(availableCookList.size(), shiftTemplate.length));
                selectedCookList = cookCombinations.get(0);
                for (int i = 1; i < cookCombinations.size(); i++) {
                    RosterIncrementSolution newSolution = (RosterIncrementSolution) this.clone();
                    newSolution.setSelectedCookList(cookCombinations.get(i));
                    otherPossibleSolutions.add(newSolution);
                }
                planningStage = PlanningStage.SHIFT_ASSIGNMENT_SELECTION;
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
                }

                List<List<Integer>> shiftAssignments = Util.generatePermutations(IntStream.range(0, shiftTemplate.length).boxed().collect(Collectors.toList()), selectedCookList.size());
                shiftAssignment = shiftAssignments.get(0);
                for (int i = 1; i < shiftAssignments.size(); i++) {
                    RosterIncrementSolution newSolution = (RosterIncrementSolution) this.clone();
                    newSolution.setShiftAssignment(shiftAssignments.get(i));
                    otherPossibleSolutions.add(newSolution);
                }
                planningStage = PlanningStage.COOKS_ROSTERING;
                break;
            case COOKS_ROSTERING:
                // starting at starting restaurant hours on current day
                Instant shiftStart = data.getStartPlanningDateTime().withHour(data.getRestaurant().getStartingHour()).plusDays(shiftDayIndex).toInstant();
                Instant shiftEnd;
                for (int i = 0; i < shiftTemplate.length; i++) {
                    shiftEnd = shiftStart.plus(shiftTemplate[i], ChronoUnit.HOURS);
                    Shift shift = new Shift(data.getRestaurant(), null, getCurrentCuisine(), shiftStart, shiftEnd);
                    shiftDayList.get(shiftDayIndex).add(shift);
                    shiftStart = shiftEnd;
                }
                // setting cooks for the generated shifts according to the template and all cook rostering permutations among the shifts in template
                for (int i = 0; i < selectedCookList.size(); i++) {
                    shiftDayList.get(shiftDayIndex).get(shiftAssignment.get(i)).setCook(selectedCookList.get(i));
                }
                // hardsoftscore depending on early/late/hoursAvailable/etc

                planningStage = PlanningStage.NEXT_CUISINE_PREPARATION;
                break;
            case SKIP_CUISINE:
            case NEXT_CUISINE_PREPARATION:
                // if no cuisines left - decrement remainingWorkingDays
// HARDsoftscore depending on cooks remaining
                // set cooks remaining working days correctly (if it was 0, then it should be 5 or 2)
                // decrement remaining working days
                // set cook is not free today (Shiftsequence)

                if (cuisineIndex + 1 > data.getCuisineList().size()) {
                    cuisineIndex = 0;
                    for (Cook cook : selectedCookList) {
                        // moving next day
                        // clear availability
                    }
                } else {
                    cuisineIndex++;
                }
                shiftDayIndex++;
                shiftDayList.add(new ArrayList<>());
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
}
