package ru.guybydefault.restnetwork.planning;

import ru.guybydefault.restnetwork.entity.Cook;
import ru.guybydefault.restnetwork.entity.Cuisine;
import ru.guybydefault.restnetwork.entity.Restaurant;
import ru.guybydefault.restnetwork.util.Util;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlanningData {

    private OffsetDateTime startPlanningDateTime;
    private OffsetDateTime endPlanningDateTime;

    private Restaurant restaurant;

    private int workingDayHours;

    private int minShiftHours;
    private int maxShiftHours;

    private List<Cuisine> cuisineList;

    private HashMap<Cuisine, ArrayList<Cook>> cuisineCookHashMap;

    private ArrayList<int[]> shiftHoursTemplates;


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
        shiftHoursTemplates = Util.generateSumCombinations(minShiftHours, maxShiftHours, workingDayHours);
    }


    public OffsetDateTime getStartPlanningDateTime() {
        return startPlanningDateTime;
    }

    public void setStartPlanningDateTime(OffsetDateTime startPlanningDateTime) {
        this.startPlanningDateTime = startPlanningDateTime;
    }

    public OffsetDateTime getEndPlanningDateTime() {
        return endPlanningDateTime;
    }

    public void setEndPlanningDateTime(OffsetDateTime endPlanningDateTime) {
        this.endPlanningDateTime = endPlanningDateTime;
    }


    public List<Cuisine> getCuisineList() {
        return cuisineList;
    }

    public void setCuisineList(List<Cuisine> cuisineList) {
        this.cuisineList = cuisineList;
    }

    public int getWorkingDayHours() {
        return workingDayHours;
    }

    public void setWorkingDayHours(int workingDayHours) {
        this.workingDayHours = workingDayHours;
    }

    public int getMinShiftHours() {
        return minShiftHours;
    }

    public void setMinShiftHours(int minShiftHours) {
        this.minShiftHours = minShiftHours;
    }

    public int getMaxShiftHours() {
        return maxShiftHours;
    }

    public void setMaxShiftHours(int maxShiftHours) {
        this.maxShiftHours = maxShiftHours;
    }

    public HashMap<Cuisine, ArrayList<Cook>> getCuisineCookHashMap() {
        return cuisineCookHashMap;
    }

    public void setCuisineCookHashMap(HashMap<Cuisine, ArrayList<Cook>> cuisineCookHashMap) {
        this.cuisineCookHashMap = cuisineCookHashMap;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
