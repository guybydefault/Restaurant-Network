package ru.guybydefault.restnetwork.planning;

import ru.guybydefault.restnetwork.entity.Cook;
import ru.guybydefault.restnetwork.entity.CookPreferences;
import ru.guybydefault.restnetwork.entity.Cuisine;
import ru.guybydefault.restnetwork.entity.Shift;
import ru.guybydefault.restnetwork.util.Util;

import java.time.OffsetDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class RosterSolution {

    private ArrayList<ArrayList<Shift>> shiftDayList;
    private PlanningData data;

    private ArrayList<int[]> shiftHoursTemplates;
    private HardSoftScore hardSoftScore;

    private boolean changed = true;

    public RosterSolution(PlanningData data) {
        this.hardSoftScore = new HardSoftScore();
        this.data = data;
        this.shiftDayList = new ArrayList<>();
        shiftHoursTemplates = Util.generateSumCombinations(this.data.getMinShiftHours(), this.data.getMaxShiftHours(), this.data.getWorkingDayHours());
    }

    public RosterSolution initialize() {
        // For now let's suppose that period between end and start is multiple of 24 hours
        OffsetDateTime currentDayStart = data.getStartPlanningDateTime();
        while (currentDayStart.isBefore(data.getEndPlanningDateTime())) {
            ArrayList<Shift> shiftDayList = new ArrayList<>();
            this.shiftDayList.add(shiftDayList);
            for (Cuisine cuisine : data.getCuisineList()) {
                OffsetDateTime currentCuisineShiftStart = currentDayStart;
                int[] shiftHoursTemplate = shiftHoursTemplates.get((int) (Math.random() * shiftHoursTemplates.size()));
                for (int shiftDuration : shiftHoursTemplate) {
                    Cook cook = data.getCuisineCookHashMap().get(cuisine).get((int) Math.random() * data.getCuisineCookHashMap().get(cuisine).size());
//                    Shift shift = new Shift(data.getRestaurant(), cook, cuisine, currentCuisineShiftStart, currentCuisineShiftStart.plusHours(shiftDuration));
//                    shiftDayList.add(shift);

                }
            }
            currentDayStart = currentDayStart.plusDays(1);
        }
        return this;
    }

    public ArrayList<ArrayList<Shift>> getShiftDayList() {
        setChanged(true);
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

    public ArrayList<int[]> getShiftHoursTemplates() {
        return shiftHoursTemplates;
    }

    public void setShiftHoursTemplates(ArrayList<int[]> shiftHoursTemplates) {
        this.shiftHoursTemplates = shiftHoursTemplates;
    }

    private void recalculateScore() {
//        hardSoftScore.reset();
//        for (ArrayList<Shift> shiftList : shiftDayList) {
//            for (int i = 0; i < shiftList.size(); i++) {
//                Shift shift1 = shiftList.get(i);
//                for (int k = i + 1; k < shiftList.size(); k++) {
//                    Shift shift2 = shiftList.get(k);
//                    if (shift1.getCook().equals(shift2.getCook())) {
//                        if (shift1.getStartDateTime().getYear() == shift2.getStartDateTime().getYear() && shift1.getStartDateTime().getMonth() == shift2.getStartDateTime().getMonth() && shift1.getStartDateTime().getDayOfMonth() == shift2.getStartDateTime().getDayOfMonth()) {
//                            hardSoftScore.minusHard(20);
//                        }
//                    }
//
//                }
//
//                if (shift1.getCook() == null) {
//                    throw new RuntimeException();
////                    hardSoftScore.minusHard(10);
////                    continue;
//                }
//
//                CookPreferences cookPreferences = shift1.getCook().getCookPreferences();
//                if (!cookPreferences.prefersLateShifts() && shift1.isLate() || !cookPreferences.prefersEarlyShifts() && shift1.isEarly()) {
//                    hardSoftScore.minusHard(10);
//                }
//                if (shift1.getDurationHours() > cookPreferences.getAvailableHoursPerDay()) {
//                    hardSoftScore.minusHard((int) shift1.getDurationHours() - cookPreferences.getAvailableHoursPerDay());
//                }
//                hardSoftScore.minusSoft(Math.abs((int) shift1.getDurationHours() - cookPreferences.getPreferedHoursPerDay()));
//            }
//        }
    }

    public HardSoftScore getHardSoftScore() {
        if (changed) {
            recalculateScore();
            changed = false;
        }
        return hardSoftScore;
    }

    public void setHardSoftScore(HardSoftScore hardSoftScore) {
        this.hardSoftScore = hardSoftScore;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
