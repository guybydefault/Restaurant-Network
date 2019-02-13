package ru.guybydefault.restnetwork.entity;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.*;

public class ShiftTest {


    @Test
    public void isEarly() {
        Shift shift = new Shift();
        shift.setStartDateTime(OffsetDateTime.of(2017, 1, 1, 2, 00, 0, 0, ZoneOffset.UTC));
        shift.setEndDateTime(OffsetDateTime.of(2017, 1, 1, 17, 00, 0, 0, ZoneOffset.UTC));
        Assertions.assertTrue(shift.isEarly());

        shift.setStartDateTime(OffsetDateTime.of(2017, 1, 1, 2, 00, 0, 0, ZoneOffset.UTC));
        shift.setEndDateTime(OffsetDateTime.of(2017, 1, 1, 17, 01, 0, 0, ZoneOffset.UTC));
        Assertions.assertFalse(shift.isEarly());

        shift.setStartDateTime(OffsetDateTime.of(2017, 1, 1, 17, 05, 0, 0, ZoneOffset.UTC));
        shift.setEndDateTime(OffsetDateTime.of(2017, 1, 1, 19, 00, 0, 0, ZoneOffset.UTC));
        Assertions.assertFalse(shift.isEarly());

        shift.setStartDateTime(OffsetDateTime.of(2017, 1, 1, 11, 00, 0, 0, ZoneOffset.UTC));
        shift.setEndDateTime(OffsetDateTime.of(2017, 1, 2, 11, 00, 0, 0, ZoneOffset.UTC));
        Assertions.assertFalse(shift.isEarly());

    }

    @Test
    public void isLate() {
        Shift shift = new Shift();
        shift.setStartDateTime(OffsetDateTime.of(2017, 1, 1, 2, 00, 0, 0, ZoneOffset.UTC));
        shift.setEndDateTime(OffsetDateTime.of(2017, 1, 1, 17, 00, 0, 0, ZoneOffset.UTC));
        Assertions.assertFalse(shift.isLate());

        shift.setStartDateTime(OffsetDateTime.of(2017, 1, 1, 2, 00, 0, 0, ZoneOffset.UTC));
        shift.setEndDateTime(OffsetDateTime.of(2017, 1, 1, 17, 01, 0, 0, ZoneOffset.UTC));
        Assertions.assertTrue(shift.isLate());
    }


}