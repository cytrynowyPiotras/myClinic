package com.clinicapp.classesTests;
import com.clinicapp.classes.Appointment;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentTest {

    @Test
    void apointmentConstructorTest() {
        Appointment app = null;
        try {
            app = new Appointment("2044-01-21", "00:18", "1", "1");
        } catch (Exception e)  { e.printStackTrace(); }
        LocalTime time = LocalTime.parse("00:18");
        assertEquals(LocalDate.of(2044, 01, 21), app.getDay());
        assertEquals(time, app.getTime());
        assertEquals("1", app.getDoctorId());
        assertEquals("1", app.getDateId());
    }

    @Test
    void appointmentCheckPastDayCorrect() {
        Appointment app = null;
        try {
            app = new Appointment("2044-01-21", "00:18", "1", "1");
        } catch (Exception e)  { e.printStackTrace(); }
        LocalDate day = LocalDate.parse("2033-01-03");
        Appointment finalApp = app;
        assertDoesNotThrow(()->{
            finalApp.checkPastDay(day);
        });
    }

    @Test
    void appointmentCheckPastDayIncorrect() {
        Appointment app = null;
        try {
            app = new Appointment("2044-01-21", "00:18", "1", "1");
        } catch (Exception e)  { e.printStackTrace(); }
        LocalDate day = LocalDate.parse("2023-01-03");
        Appointment finalApp = app;
        assertThrows(Exception.class, ()->{
            finalApp.checkPastDay(day);
        });
    }

    @Test
    void appointmentCheckCurrentDay() {
        Appointment app = null;
        try {
            app = new Appointment("2044-01-21", "00:18", "1", "1");
        } catch (Exception e)  { e.printStackTrace(); }
        LocalDate day = LocalDate.now();
        Appointment finalApp = app;
        assertDoesNotThrow(()->{
            finalApp.checkPastDay(day);
        });
    }

    @Test
    void appointmentCheckTimePrecise() {
        Appointment app = null;
        try {
            app = new Appointment("2044-01-21", "00:18", "1", "1");
        } catch (Exception e)  { e.printStackTrace(); }
        String day = "2045-01-01";
        String time = "23:59";
        Appointment finalApp = app;
        assertDoesNotThrow(()->{
            finalApp.checkPastPrecise(day, time);
        });
    }

    @Test
    void appointmentCheckTimePreciseInvalidTime() {
        Appointment app = null;
        try {
            app = new Appointment("2044-01-21", "00:18", "1", "1");
        } catch (Exception e)  { e.printStackTrace(); }
        String day = LocalDate.now().toString();
        String time = "00:00";
        Appointment finalApp = app;
        assertThrows(Exception.class, ()->{
            finalApp.checkPastPrecise(day, time);
        });
    }

    @Test
    void appointmentCheckTimePreciseInvalidDay() {
        Appointment app = null;
        try {
            app = new Appointment("2044-01-21", "00:18", "1", "1");
        } catch (Exception e)  { e.printStackTrace(); }
        String day = "1970-01-01";
        String time = "00:00";
        Appointment finalApp = app;
        assertThrows(Exception.class, ()->{
            finalApp.checkPastPrecise(day, time);
        });
    }

    @Test
    void appointmentCheckTimePreciseNow() {
        Appointment app = null;
        try {
            app = new Appointment("2044-01-21", "00:18", "1", "1");
        } catch (Exception e)  { e.printStackTrace(); }
        String day = LocalDate.now().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = LocalTime.now().format(formatter).toString();
        Appointment finalApp = app;
        assertThrows(Exception.class, ()->{
            finalApp.checkPastPrecise(day, time);
        });
    }

    @Test
    void appointmentCheckFormatValid() {
        String day = "2022-01-01";
        String time = "12:30";
        assertDoesNotThrow(()->{
            Appointment.checkFormat(day, time);
        });
    }

    @Test
    void appointmentCheckFormatRandom() {
        String day = "aaa";
        String time = "12:30";
        assertThrows(Exception.class, ()->{
            Appointment.checkFormat(day, time);
        });
    }

    @Test
    void appointmentCheckFormatEmpty() {
        String day = "2023-01-01";
        String time = "";
        assertThrows(Exception.class, ()->{
            Appointment.checkFormat(day, time);
        });
    }

    @Test
    void appointmentCheckFormatInvalidDay() {
        String day = "9999-99-99";
        String time = "12:30";
        assertThrows(Exception.class, ()->{
            Appointment.checkFormat(day, time);
        });
    }

    @Test
    void appointmentCheckFormatInvalidTime() {
        String day = "2022-01-01";
        String time = "25:67";
        assertThrows(Exception.class, ()->{
            Appointment.checkFormat(day, time);
        });
    }

    @Test
    void appointmentSetPatientPesel() {
        Appointment app = null;
        try {
            app = new Appointment("2044-01-21", "00:18", "1", "1");
        } catch (Exception e)  { e.printStackTrace(); }
        String pesel = "11122233322";
        Appointment finalApp = app;
        assertDoesNotThrow(()->finalApp.setPatientPesel(pesel));
    }

    @Test
    void appointmentSetPatientPeselInvalid() {
        Appointment app = null;
        try {
            app = new Appointment("2044-01-21", "00:18", "1", "1");
        } catch (Exception e)  { e.printStackTrace(); }
        String pesel = "000000000000";
        Appointment finalApp = app;
        assertThrows(ArithmeticException.class, ()->{
            finalApp.setPatientPesel(pesel);
        });
    }
}
