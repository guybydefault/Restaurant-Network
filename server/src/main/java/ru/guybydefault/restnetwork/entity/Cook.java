package ru.guybydefault.restnetwork.entity;


import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

@Entity
public class Cook extends BaseEntity {

    @NotEmpty
    private String fullName;

    @OneToMany(mappedBy = "cook", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = false, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Shift> shiftList;

    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @NotNull
    private Restaurant restaurant;

    @OneToMany(mappedBy = "cook", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CuisineCertification> cuisineCertificationList;

    @Embedded
    @JsonUnwrapped
    @Valid
    private CookPreferences cookPreferences;

    public boolean hasCertification(Cuisine cuisine) {
        return cuisineCertificationList.stream()
                .anyMatch(c -> c.getCuisine().equals(cuisine));
    }

    /**
     * @param cuisine
     * @return true if cook had had certification for {@code cuisine} and it was successfully deleted from the list of certifications, false otherwise
     */
    public boolean pullCertification(Cuisine cuisine) {
        ListIterator<CuisineCertification> cuisineListIterator = cuisineCertificationList.listIterator();
        while (cuisineListIterator.hasNext()) {
            if (cuisineListIterator.next().getCuisine().equals(cuisine)) {
                cuisineListIterator.remove();
                return true;
            }
        }
        return false;
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

    @Override
    public String toString() {
        return fullName;
    }

    @PreRemove
    private void preRemove() {
        shiftList.forEach(shift -> {
            shift.setCook(null);
        });
    }

}
