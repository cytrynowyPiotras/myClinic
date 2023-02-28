package com.clinicapp.classesTests;

import com.clinicapp.classes.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PatientTest {
    private final Patient patient = new Patient("Adam", "Nowak", "111222333","12345678901", "72", "180", "2");

    @Test
    void patientConstructorTest(){
        assertEquals("Adam", patient.getName());
        assertEquals("Nowak", patient.getSurname());
        assertEquals("111222333", patient.getPhoneNumber());
        assertEquals("12345678901", patient.getPesel());
        assertEquals("72", patient.getWeight());
        assertEquals("180", patient.getHeight());
        assertEquals("2", patient.getAddressId());
    }

    @Test
    void patientSetterTest(){
        patient.setAddressId("3");
        patient.setHeight("170");
        patient.setPesel("12345678902");
        patient.setWeight("75");
        patient.setName("Ewa");
        patient.setSurname("Nowacka");
        patient.setPhoneNumber("111222334");
        assertEquals("Ewa", patient.getName());
        assertEquals("Nowacka", patient.getSurname());
        assertEquals("111222334", patient.getPhoneNumber());
        assertEquals("12345678902", patient.getPesel());
        assertEquals("75", patient.getWeight());
        assertEquals("170", patient.getHeight());
        assertEquals("3", patient.getAddressId());
    }

    @Test
    void patientInvalidDataTest(){
        assertThrows(ArithmeticException.class, ()->{
            Patient patient1 = new Patient("Adam", "Nowak", "111222333","123456789011", "72", "180", "2");
        });
    }
}
