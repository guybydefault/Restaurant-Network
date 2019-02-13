package ru.guybydefault.restnetwork.entity;


import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Cook extends BaseEntity {

    private String fullName;

    @OneToMany(mappedBy = "cook")
    private List<Shift> shiftList;

    @ManyToOne
    private Restaurant restaurant;

    @OneToMany(mappedBy = "cook")
    private Set<CookCertification> cookCertificationsSet;

    @Embedded
    private CookPreferences cookPreferences;

    public boolean hasCertification(Cuisine cuisine) {
        return cookCertificationsSet.stream()
                .anyMatch(c -> c.getCuisine().equals(cuisine));
    }

    public List<Shift> getShiftList() {
        return shiftList;
    }

    public void setShiftList(List<Shift> shiftList) {
        this.shiftList = shiftList;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Set<CookCertification> getCookCertificationsSet() {
        return cookCertificationsSet;
    }

    public void setCookCertificationsSet(Set<CookCertification> cookCertificationsSet) {
        this.cookCertificationsSet = cookCertificationsSet;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public CookPreferences getCookPreferences() {
        return cookPreferences;
    }

    public void setCookPreferences(CookPreferences cookPreferences) {
        this.cookPreferences = cookPreferences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cook cook = (Cook) o;
        return Objects.equals(fullName, cook.fullName) &&
                Objects.equals(shiftList, cook.shiftList) &&
                Objects.equals(restaurant, cook.restaurant) &&
                Objects.equals(cookCertificationsSet, cook.cookCertificationsSet) &&
                Objects.equals(cookPreferences, cook.cookPreferences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, shiftList, restaurant, cookCertificationsSet, cookPreferences);
    }
}
