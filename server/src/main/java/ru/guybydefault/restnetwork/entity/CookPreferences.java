package ru.guybydefault.restnetwork.entity;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Embeddable
public class CookPreferences {
    @Min(4)
    @Max(10)
    private int availableHoursPerDay;

    @Min(4)
    @Max(10)
    private int preferedHoursPerDay;

    private boolean prefersEarlyShifts;

    private boolean prefersLateShifts;

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


    public CookPreferences(@Min(4) @Max(10) int availableHoursPerDay, @Min(4) @Max(10) int preferred, boolean prefersEarlyShifts, boolean prefersLateShifts, @Min(1) int workDays, @Min(1) int weekDays) {
        this.availableHoursPerDay = availableHoursPerDay;
        this.preferedHoursPerDay = preferred;
        this.prefersEarlyShifts = prefersEarlyShifts;
        this.prefersLateShifts = prefersLateShifts;
        this.workDays = workDays;
        this.weekDays = weekDays;
    }

    public int getAvailableHoursPerDay() {
        return availableHoursPerDay;
    }

    public void setAvailableHoursPerDay(int availableHoursPerDay) {
        this.availableHoursPerDay = availableHoursPerDay;
    }

    public int getPreferedHoursPerDay() {
        return preferedHoursPerDay;
    }

    public void setPreferedHoursPerDay(int preferedHoursPerDay) {
        this.preferedHoursPerDay = preferedHoursPerDay;
    }

    public boolean prefersEarlyShifts() {
        return prefersEarlyShifts;
    }

    public void setPrefersEarlyShifts(boolean prefersEarlyShifts) {
        this.prefersEarlyShifts = prefersEarlyShifts;
    }

    public boolean prefersLateShifts() {
        return prefersLateShifts;
    }

    public void setPrefersLateShifts(boolean prefersLateShifts) {
        this.prefersLateShifts = prefersLateShifts;
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
                preferedHoursPerDay == that.preferedHoursPerDay &&
                prefersEarlyShifts == that.prefersEarlyShifts &&
                prefersLateShifts == that.prefersLateShifts &&
                workDays == that.workDays &&
                weekDays == that.weekDays;
    }

    @Override
    public int hashCode() {
        return Objects.hash(availableHoursPerDay, preferedHoursPerDay, prefersEarlyShifts, prefersLateShifts, workDays, weekDays);
    }
}
