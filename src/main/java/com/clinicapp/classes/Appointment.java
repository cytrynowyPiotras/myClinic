package com.clinicapp.classes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Appointment extends AppTemplate {

    @Override
    public void setDay(LocalDate day) {
        try {
            checkPastDay(day);
        } catch (Exception e) { e.printStackTrace(); }
        this.day = day;
    }

    public void checkData (LocalDate day, LocalTime time) throws Exception{ // future boolean
        LocalDate currentDate = LocalDate.now();
        String temp = day.toString() + " " + time.toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            LocalDateTime.parse(temp, formatter);
        } catch (DateTimeParseException e) { throw new Exception("Invalid datetime format"); }
        if (day.isBefore(currentDate)) {
            throw new Exception("Appointment cannot be before current date");
        }
    }

    public Appointment(String day, String time, String doctorId, String dateId) throws Exception {
        super(day, time, doctorId, dateId);
//        checkFormat(day, time);
//        checkPastPrecise(day, time);
        // patient pesel is optional - if needed add with setter
    }
}
