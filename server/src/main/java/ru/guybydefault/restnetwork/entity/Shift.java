package ru.guybydefault.restnetwork.entity;


import com.fasterxml.jackson.annotation.*;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@PlanningEntity
public class Shift extends BaseEntity {

    /**
     * Employee
     */
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Cook cook;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Restaurant restaurant;

    @ManyToOne
    @NotNull
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Cuisine cuisine;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant startDateTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant endDateTime;


    public Shift() {

    }

    public Shift(@NotNull Restaurant restaurant, Cook cook, @NotNull Cuisine cuisine, @NotNull Instant startDateTime, @NotNull Instant endDateTime) {
        this.restaurant = restaurant;
        this.cook = cook;
        this.cuisine = cuisine;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public void setCook(Cook cook) {
        this.cook = cook;
    }

    @PlanningVariable(valueRangeProviderRefs = {"cookRange"}, nullable = true)
    public Cook getCook() {
        return cook;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }

    public Instant getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Instant startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Instant getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Instant endDateTime) {
        this.endDateTime = endDateTime;
    }

    @Override
    public String toString() {
        return cook.getFullName() + " ID" + cook.getId() + " " + getCuisine().getName() + " [" + startDateTime + " - " + endDateTime + "] ";
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @JsonIgnore
    public boolean isEarly() {
        return !isLate();
    }

    @JsonIgnore
    public boolean isLate() {
        return endDateTime.atOffset(ZoneOffset.ofHours(3)).isAfter(startDateTime.atOffset(ZoneOffset.ofHours(3)).withHour(17).withMinute(0));
    }

    @JsonIgnore
    public long getDurationHours() {
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shift shift = (Shift) o;
        return
                Objects.equals(restaurant, shift.restaurant) &&
                        Objects.equals(cuisine, shift.cuisine) &&
                        Objects.equals(startDateTime, shift.startDateTime) &&
                        Objects.equals(endDateTime, shift.endDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurant, cuisine, startDateTime, endDateTime);
    }
}
