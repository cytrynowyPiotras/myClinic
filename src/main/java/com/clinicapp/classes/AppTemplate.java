package com.clinicapp.classes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AppTemplate {
    protected LocalDate day;
    protected LocalTime time;
    protected String doctorId;
    protected String dateId;
    protected String patientPesel;

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDateId() {
        return this.dateId;
    }

    public void setDateId(String dateId) {
        this.dateId = dateId;
    }

    public String getPatientPesel() {
        return this.patientPesel;
    }

    public void setPatientPesel(String patientPesel) {
        if (patientPesel.length() != 11) {
            throw new ArithmeticException("Patient pesel must be 11 numbers");
        }
        this.patientPesel = patientPesel;
    }

    public static void checkFormat(String day, String time) throws Exception {
        String datetime = day + " " + time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            LocalDateTime.parse(datetime, formatter);
        } catch (DateTimeParseException e) { throw new Exception("Invalid datetime format");}
    }

    public void checkPastDay(LocalDate day) throws Exception {
        LocalDate currDay = LocalDate.now();
        if (day.isBefore(currDay)) {
            throw new Exception("Appointment cannot be before current date");
        }
    }

    public void checkPastPrecise(String day, String time) throws Exception {
        String temp = day + " " + time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime tempTime = LocalDateTime.parse(temp, formatter);
        LocalDateTime currTime = LocalDateTime.now();
        if (tempTime.isBefore(currTime)) {
            throw new Exception("Appointment cannot be before current time");
        }
    }

    public AppTemplate(String day, String time, String doctorId, String dateId) {
        this.day = LocalDate.parse(day); // day format : yyyy-mm-dd
        this.time = LocalTime.parse(time); // time format: hh:min
        this.doctorId = doctorId;
        this.dateId = dateId;
    }
}
