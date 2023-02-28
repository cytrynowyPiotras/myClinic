package com.clinicapp.classesTests;

import com.clinicapp.classes.Specialization;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpecializationTest {
    private final Specialization spec = new Specialization("1", "Kardiolog");

    @Test
    void specializationConstructorTest(){
        assertEquals("1", spec.getSpecializationId());
        assertEquals("Kardiolog", spec.getName());
    }

    @Test
    void specializationSettersTest(){
        spec.setSpecializationId("3");
        spec.setName("Dermatolog");
        assertEquals("3", spec.getSpecializationId());
        assertEquals("Dermatolog", spec.getName());
    }
}
