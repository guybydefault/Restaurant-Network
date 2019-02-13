package ru.guybydefault.restnetwork.entity;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Entity
@PlanningEntity
public class Shift extends BaseEntity {


    /**
     * Employee
     */
    @ManyToOne
    @PlanningVariable(valueRangeProviderRefs = "cookRange")
    private Cook cook;

    @NotNull
    @ManyToOne
    private Restaurant restaurant;

    @ManyToOne
    @NotNull
    private Cuisine cuisine;

    @NotNull
    private OffsetDateTime startDateTime;

    @NotNull
    private OffsetDateTime endDateTime;


    public Shift() {

    }

    public Shift(@NotNull Restaurant restaurant, Cook cook, @NotNull Cuisine cuisine, @NotNull OffsetDateTime startDateTime, @NotNull OffsetDateTime endDateTime) {
        this.restaurant = restaurant;
        this.cook = cook;
        this.cuisine = cuisine;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public void setCook(Cook cook) {
        this.cook = cook;
    }

    public Cook getCook() {
        return cook;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }

    public OffsetDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(OffsetDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public OffsetDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(OffsetDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    @Override
    public String toString() {
        return cook.getFullName() + " ID" + cook.getId() +" "+ getCuisine().getName() + " [" + startDateTime + " - " + endDateTime + "] ";
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public boolean isEarly() {
        return !isLate();
    }

    public boolean isLate() {
        return endDateTime.isAfter(startDateTime.withHour(17).withMinute(0));
    }

    public long getDurationHours() {
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }
}
