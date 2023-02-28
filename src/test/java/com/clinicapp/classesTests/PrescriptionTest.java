package com.clinicapp.classesTests;

import com.clinicapp.classes.Prescription;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrescriptionTest {
    private final Prescription prescription = new Prescription("1", LocalDate.of(2022, 10, 25), "lek1, lek2", "2 razy dziennie");

    @Test
    void prescriptionConstructorTest(){
        assertEquals("lek1, lek2", prescription.getDrugs());
        assertEquals("1", prescription.getRecipeId());
        assertEquals("2 razy dziennie", prescription.getDescription());
        assertEquals(LocalDate.of(2022, 10, 25), prescription.getIssueDate());
    }

    @Test
    void prescriptionSetterTest(){
        prescription.setDescription("3 razy dziennie");
        prescription.setDrugs("lek3, lek4");
        prescription.setIssueDate(LocalDate.of(2023,1,2));
        prescription.setRecipeId("12");
        assertEquals("lek3, lek4", prescription.getDrugs());
        assertEquals("12", prescription.getRecipeId());
        assertEquals("3 razy dziennie", prescription.getDescription());
        assertEquals(LocalDate.of(2023, 1, 2), prescription.getIssueDate());
    }
}
