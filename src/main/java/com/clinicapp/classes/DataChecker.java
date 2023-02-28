package com.clinicapp.classes;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DataChecker {
    public static boolean checkPatientsData(Patient pat){
        String name = pat.getName();
        String surname = pat.getSurname();
        String phone = pat.getPhoneNumber();
        String weight = pat.getWeight();
        String height = pat.getHeight();
        if (name.isEmpty()) {return false;}
        if (surname.isEmpty()) {return false;}
        if (phone.length()!=9) {return false;}
        if (weight.isEmpty()) {return false;}
        if(height.isEmpty()) {return false;}
        try {
            Integer.valueOf(phone);
            Integer.valueOf(weight);
            Integer.valueOf(height);
        }
        catch (Exception e) {return false;}
        return true;
    }
    public static boolean checkAddress(Address addr) {
        if(addr.getStreet().isEmpty()) {return false;}
        if (addr.getCity().isEmpty()) {return false;}
        if (addr.getPostalCode().isEmpty()) {return false;}
        return true;
    }
    public static boolean checkNameSurnamePhone(String name, String surname, String phoneNumber, String login, String password) {
        if (name.length() == 0) {
            return false;
        }
        if (surname.length() == 0) {
            return false;
        }
        if (phoneNumber.length() != 9) {
            return false;
        }
        if (login.length() == 0) {
            return false;
        }
        if (password.length() == 0) {
            return false;
        }
        try {
            int phoneNumberInt = Integer.parseInt(phoneNumber);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public static boolean checkNameSurnamePhone(String name, String surname, String phoneNumber) {
        if (name.length() == 0) {
            return false;
        }
        if (surname.length() == 0) {
            return false;
        }
        try {
            int i = Integer.parseInt(phoneNumber);
        } catch (Exception k) {
            return false;
        }
        return phoneNumber.length() == 9;
    }
    public static boolean checkPesel(String pesel) {
        return pesel.length() == 11;
    }
    public static boolean checkDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(date, formatter);
        } catch (DateTimeParseException r) {
            return false;
        }
        return true;
    }
}
