package ru.guybydefault.restnetwork.entity;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Objects;

@Embeddable
public class CookPreferences {
    @Min(4)
    @Max(10)
    private int availableHoursPerDay;

    @Min(4)
    @Max(10)
    private int preferredHoursPerDay;

    private boolean availableEarlyShifts;

    private boolean availableLateShifts;

    /**
     * <strong>workDays</strong> and <strong>weekDays</strong> describe employee's working schedule (2/2 5/2 etc)
     */
    @Min(1)
    private int workDays;

    /**
     * <strong>workDays</strong> and <strong>weekDays</strong> describe employee's working schedule (2/2, 5/2, etc)
     */
    @Min(1)
    private int weekDays;

    public CookPreferences() {
    }


    public CookPreferences(@Min(4) @Max(10) int availableHoursPerDay, @Min(4) @Max(10) int preferred, boolean availableEarlyShifts, boolean availableLateShifts, @Min(1) int workDays, @Min(1) int weekDays) {
        this.availableHoursPerDay = availableHoursPerDay;
        this.preferredHoursPerDay = preferred;
        this.availableEarlyShifts = availableEarlyShifts;
        this.availableLateShifts = availableLateShifts;
        this.workDays = workDays;
        this.weekDays = weekDays;
    }

    public int getAvailableHoursPerDay() {
        return availableHoursPerDay;
    }

    public void setAvailableHoursPerDay(int availableHoursPerDay) {
        this.availableHoursPerDay = availableHoursPerDay;
    }

    public int getPreferredHoursPerDay() {
        return preferredHoursPerDay;
    }

    public void setPreferredHoursPerDay(int preferedHoursPerDay) {
        this.preferredHoursPerDay = preferedHoursPerDay;
    }

    public boolean isAvailableEarlyShifts() {
        return availableEarlyShifts;
    }

    public void setAvailableEarlyShifts(boolean prefersEarlyShifts) {
        this.availableEarlyShifts = prefersEarlyShifts;
    }

    public boolean isAvailableLateShifts() {
        return availableLateShifts;
    }

    public void setAvailableLateShifts(boolean prefersLateShifts) {
        this.availableLateShifts = prefersLateShifts;
    }

    public int getWorkDays() {
        return workDays;
    }

    public void setWorkDays(int workDays) {
        this.workDays = workDays;
    }

    public int getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(int weekDays) {
        this.weekDays = weekDays;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CookPreferences that = (CookPreferences) o;
        return availableHoursPerDay == that.availableHoursPerDay &&
                preferredHoursPerDay == that.preferredHoursPerDay &&
                availableEarlyShifts == that.availableEarlyShifts &&
                availableLateShifts == that.availableLateShifts &&
                workDays == that.workDays &&
                weekDays == that.weekDays;
    }

    @Override
    public int hashCode() {
        return Objects.hash(availableHoursPerDay, preferredHoursPerDay, availableEarlyShifts, availableLateShifts, workDays, weekDays);
    }
}
