package ru.guybydefault.restnetwork.entity;


import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Cook extends BaseEntity {

    private String fullName;

    @OneToMany(mappedBy = "cook", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = true)
    @JsonIgnore
    private List<Shift> shiftList;

    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "cook", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<CuisineCertification> cuisineCertificationList;

    @Embedded
    @JsonUnwrapped
    private CookPreferences cookPreferences;

    public boolean hasCertification(Cuisine cuisine) {
        return cuisineCertificationList.stream()
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

    public List<CuisineCertification> getCuisineCertificationList() {
        return cuisineCertificationList;
    }

    public void setCuisineCertificationList(List<CuisineCertification> cuisineCertificationList) {
        this.cuisineCertificationList = cuisineCertificationList;
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
                Objects.equals(restaurant, cook.restaurant) &&
                Objects.equals(cookPreferences, cook.cookPreferences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, restaurant, cookPreferences);
    }
}
