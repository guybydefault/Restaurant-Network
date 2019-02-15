package ru.guybydefault.restnetwork.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class Cuisine extends BaseEntity{

    @NotNull
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "cuisine", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CuisineCertification> cookCertificationList;

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

    public List<CuisineCertification> getCookCertificationList() {
        return cookCertificationList;
    }

    public void setCookCertificationList(List<CuisineCertification> cookCertificationList) {
        this.cookCertificationList = cookCertificationList;
    }
}
