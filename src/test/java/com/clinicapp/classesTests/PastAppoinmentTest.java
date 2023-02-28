package com.clinicapp.classesTests;

import com.clinicapp.classes.PastAppoinment;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PastAppoinmentTest {
    private PastAppoinment pastAppoinment = null;

    @Test
    void pastAppointmentConstructorTest(){
        try{
            pastAppoinment = new PastAppoinment("2023-01-20", "07:30","1", "1", "opis");
        }catch (Exception e){
            e.printStackTrace();
        }
        assertEquals("opis", pastAppoinment.getDescription());
        assertEquals("1", pastAppoinment.getDoctorId());
        assertEquals("1", pastAppoinment.getDateId());
        assertEquals(LocalTime.of(7,30), pastAppoinment.getTime());
        assertEquals(LocalDate.of(2023,1,20), pastAppoinment.getDay());
    }

    @Test
    void pastAppointmentSetterTest(){
        try{
            pastAppoinment = new PastAppoinment("2023-01-20", "07:30","1", "1", "opis");
        }catch (Exception e){
            e.printStackTrace();
        }
        pastAppoinment.setDescription("inny opis");
        assertEquals("inny opis", pastAppoinment.getDescription());
        assertEquals("1", pastAppoinment.getDoctorId());
        assertEquals("1", pastAppoinment.getDateId());
        assertEquals(LocalTime.of(7,30), pastAppoinment.getTime());
        assertEquals(LocalDate.of(2023,1,20), pastAppoinment.getDay());
    }

    @Test
    void pastAppointmentInvalidData(){
        assertThrows(Exception.class, ()->{
            pastAppoinment = new PastAppoinment("23123", "1231", "1", "1", "1");
        });
    }
}
