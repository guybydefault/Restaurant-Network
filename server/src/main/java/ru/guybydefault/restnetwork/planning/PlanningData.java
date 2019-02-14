package ru.guybydefault.restnetwork.planning;

import ru.guybydefault.restnetwork.entity.Cook;
import ru.guybydefault.restnetwork.entity.Cuisine;
import ru.guybydefault.restnetwork.entity.Restaurant;
import ru.guybydefault.restnetwork.util.Util;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class PlanningData {

    private final OffsetDateTime startPlanningDateTime;
    private final OffsetDateTime endPlanningDateTime;

    private final Restaurant restaurant;

    private final int workingDayHours;

    private final int minShiftHours;
    private final int maxShiftHours;

    private final List<Cuisine> cuisineList;

    private final HashMap<Cuisine, ArrayList<Cook>> cuisineCookHashMap;

    private final ArrayList<int[]> shiftDayTemplates;


    public PlanningData(OffsetDateTime startPlanningDateTime, OffsetDateTime endPlanningDateTime, int minShiftHours, int maxShiftHours, List<Cuisine> cuisineList, List<Cook> cookList, Restaurant restaurant) {
        this.startPlanningDateTime = startPlanningDateTime;
        this.endPlanningDateTime = endPlanningDateTime;
        this.workingDayHours = restaurant.getWorkingDayHours();
        this.minShiftHours = minShiftHours;
        this.maxShiftHours = maxShiftHours;
        this.cuisineList = cuisineList;
        this.restaurant = restaurant;

        cuisineCookHashMap = new HashMap<>();
        cuisineList.forEach(cuisine -> {
            ArrayList<Cook> cookListByCuisine = new ArrayList<>();
            cuisineCookHashMap.put(cuisine, cookListByCuisine);
            cookList.forEach(cook -> {
                if (cook.hasCertification(cuisine)) {
                    cookListByCuisine.add(cook);
                }
            });
        });
        shiftDayTemplates = Util.generateSumCombinations(minShiftHours, maxShiftHours, workingDayHours);
    }


    public OffsetDateTime getStartPlanningDateTime() {
        return startPlanningDateTime;
    }


    public OffsetDateTime getEndPlanningDateTime() {
        return endPlanningDateTime;
    }


    public List<Cook> getCooksByCuisine(Cuisine cuisine) {
        return cuisineCookHashMap.get(cuisine);
    }


    public List<Cuisine> getCuisineList() {
        return cuisineList;
    }


    public int getWorkingDayHours() {
        return workingDayHours;
    }


    public int getMinShiftHours() {
        return minShiftHours;
    }


    public int getMaxShiftHours() {
        return maxShiftHours;
    }


    public HashMap<Cuisine, ArrayList<Cook>> getCuisineCookHashMap() {
        return cuisineCookHashMap;
    }


    public Restaurant getRestaurant() {
        return restaurant;
    }


    public ArrayList<int[]> getShiftDayTemplates() {
        return shiftDayTemplates;
    }


    private Comparator<int[]> intArrayByLengthComparator = new Comparator<int[]>() {
        @Override
        public int compare(int[] o1, int[] o2) {
            return o1.length - o2.length;
        }
    };

    public int getMinShiftsInTemplate() {
        return shiftDayTemplates.stream().min(intArrayByLengthComparator).get().length;
    }

    public int getMaxShiftsInTemplate() {
        return shiftDayTemplates.stream().max(intArrayByLengthComparator).get().length;
    }
}
