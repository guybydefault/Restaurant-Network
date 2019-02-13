package ru.guybydefault.restnetwork.entity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Entity
public class Cuisine extends BaseEntity{

    @NotNull
    private String name;

//    @OneToMany(mappedBy = "cuisine")
//    private List<Shift> shiftList;
//
//    @OneToMany(mappedBy = "cuisine")
//    private List<CookCertification> cookCertificationList;

    public Cuisine() {

    }

    public Cuisine(@NotNull String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cuisine cuisine = (Cuisine) o;
        return Objects.equals(name, cuisine.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
