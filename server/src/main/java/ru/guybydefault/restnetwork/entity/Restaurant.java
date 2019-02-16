package ru.guybydefault.restnetwork.entity;

import org.hibernate.validator.constraints.Range;
import ru.guybydefault.restnetwork.util.ZoneOffsetJpaConverter;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Entity
public class Restaurant extends BaseEntity {

    @NotNull
    private String name;

    @NotNull
    private String address;

    @NotNull
    @Range(min = 0, max = 23)
    private int startingHour;

    @NotNull
    @Range(min = 0, max = 23)
    private int closingHour;

    @NotNull
    @Convert(converter = ZoneOffsetJpaConverter.class)
    private ZoneOffset zoneOffset;

    @OneToMany(mappedBy = "restaurant", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Cook> cooks;

    @OneToMany(mappedBy = "restaurant", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Shift> shiftList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Cook> getCooks() {
        return cooks;
    }

    public void setCooks(List<Cook> cooks) {
        this.cooks = cooks;
    }

    public List<Shift> getShiftList() {
        return shiftList;
    }

    public void setShiftList(List<Shift> shiftList) {
        this.shiftList = shiftList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }

    public int getStartingHour() {
        return startingHour;
    }

    public void setStartingHour(int startingHour) {
        this.startingHour = startingHour;
    }

    public int getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(int closingHour) {
        this.closingHour = closingHour;
    }

    public ZoneOffset getZoneOffset() {
        return zoneOffset;
    }

    public void setZoneOffset(ZoneOffset zoneOffset) {
        this.zoneOffset = zoneOffset;
    }

    public int getWorkingDayHours() {
        if (closingHour > startingHour) {
            return closingHour - startingHour;
        } else {
            return 24 - startingHour + closingHour;
        }
    }
}

