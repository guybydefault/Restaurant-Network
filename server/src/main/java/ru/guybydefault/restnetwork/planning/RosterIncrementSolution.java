package ru.guybydefault.restnetwork.planning;

import ru.guybydefault.restnetwork.entity.Cook;
import ru.guybydefault.restnetwork.entity.Cuisine;
import ru.guybydefault.restnetwork.entity.Shift;
import ru.guybydefault.restnetwork.util.Util;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RosterIncrementSolution {

    private ArrayList<ArrayList<Shift>> shiftDayList;

    private int currShiftDay = 0;

    private PlanningData data;

    private HardSoftScore hardSoftScore;

    private PlanningStage planningStage;

    public RosterIncrementSolution(PlanningData data) {
        this.hardSoftScore = new HardSoftScore(0, 0);
        this.data = data;
        this.shiftDayList = new ArrayList<>((int) ChronoUnit.DAYS.between(data.getStartPlanningDateTime(), data.getEndPlanningDateTime()) + 1);
    }

//    public RosterIncrementSolution initialize() {
//        // For now let's suppose that period between end and start is multiple of 24 hours
//        OffsetDateTime currentDayStart = data.getStartPlanningDateTime();
//        while (currentDayStart.isBefore(data.getEndPlanningDateTime())) {
//            ArrayList<Shift> shiftDayList = new ArrayList<>();
//            this.shiftDayList.add(shiftDayList);
//            for (Cuisine cuisine : data.getCuisineList()) {
//                OffsetDateTime currentCuisineShiftStart = currentDayStart;
//                int[] shiftHoursTemplate = shiftHoursTemplates.get((int) (Math.random() * shiftHoursTemplates.size()));
//                for (int shiftDuration : shiftHoursTemplate) {
//                    Cook cook = data.getCuisineCookHashMap().get(cuisine).get((int) Math.random() * data.getCuisineCookHashMap().get(cuisine).size());
////                    Shift shift = new Shift(data.getRestaurant(), cook, cuisine, currentCuisineShiftStart, currentCuisineShiftStart.plusHours(shiftDuration));
////                    shiftDayList.add(shift);
//
//                }
//            }
//            currentDayStart = currentDayStart.plusDays(1);
//        }
//        return this;
//    }

    public List<RosterIncrementSolution> nextStep() {
        switch (planningStage) {
            case INITIAL:
                break;
            case COOKS_SELECTED:
                break;
            case COOKS_NUMBER_SELECTED:
                break;
            case SHIFT_TEMPLATE_SELECTED:
                break;
        }
        return null;
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
}
