package com.clinicapp.classesTests;
import com.clinicapp.classes.Address;
import com.clinicapp.classes.DataChecker;
import com.clinicapp.classes.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataCheckerTest {
    private final Patient pat = new Patient("Piotr", "Kowalski", "111222333", "11133322299", "70","180", "1");
    private final Address addr = new Address("1", "warynskiego", "00-000", "Szczecin");


    @Test
    void dataCheckerCheckPatient() {
        assertTrue((DataChecker.checkPatientsData(pat)));
    }

    @Test
    void dataCheckerCheckPatientInvalidName() {
        pat.setName("");
        assertFalse(DataChecker.checkPatientsData(pat));
    }

    @Test
    void dataCheckerCheckPatientInvalidSurname() {
        pat.setName("Piotr");
        pat.setSurname("");
        assertFalse(DataChecker.checkPatientsData(pat));
    }

    @Test
    void dataCheckerCheckPatientInvalidPhone() {
        pat.setSurname("Kowalski");
        pat.setPhoneNumber("");
        assertFalse(DataChecker.checkPatientsData(pat));
    }

    @Test
    void dataCheckerCheckPatientInvalidPhoneValue() {
        pat.setPhoneNumber("aaabbbccc");
        assertFalse(DataChecker.checkPatientsData(pat));
    }

    @Test
    void dataCheckerCheckPatientInvalidWeight() {
        pat.setPhoneNumber("111222333");
        pat.setWeight("");
        assertFalse(DataChecker.checkPatientsData(pat));
    }

    @Test
    void dataCheckerCheckPatientInvalidWeightValue() {
        pat.setWeight("lll");
        assertFalse(DataChecker.checkPatientsData(pat));
    }

    @Test
    void dataCheckerCheckPatientInvalidHeight() {
        pat.setWeight("80");
        pat.setHeight("");
        assertFalse(DataChecker.checkPatientsData(pat));
    }

    @Test
    void dataCheckerCheckPatientInvalidHeightValue() {
        pat.setHeight("aaa");
        assertFalse(DataChecker.checkPatientsData(pat));
    }

    @Test
    void dataCheckerCheckAddress() {
        assertTrue(DataChecker.checkAddress(addr));
    }

    @Test
    void dataCheckerCheckAddressInvalidStreet() {
        addr.setStreet("");
        assertFalse(DataChecker.checkAddress(addr));
    }

    @Test
    void dataCheckerCheckAddressInvalidCity() {
        addr.setStreet("ulica");
        addr.setCity("");
        assertFalse(DataChecker.checkAddress(addr));
    }

    @Test
    void dataCheckerCheckAddressInvalidPostalCode() {
        addr.setCity("Warszawa");
        addr.setPostalCode("");
        assertFalse(DataChecker.checkAddress(addr));
    }

    @Test
    void dataCheckerCheckNameSurnamePhone() {
        final String name = "Adam";
        final String surname = "aaaa";
        final String phoneNumber = "111222333";
        assertTrue(DataChecker.checkNameSurnamePhone(name, surname, phoneNumber));
    }

    @Test
    void dataCheckerCheckNameSurnamePhoneInvalidName() {
        final String name = "";
        final String surname = "aaaa";
        final String phoneNumber = "111222333";
        assertFalse(DataChecker.checkNameSurnamePhone(name, surname, phoneNumber));
    }

    @Test
    void dataCheckerCheckNameSurnamePhoneInvalidSurname() {
        final String name = "test";
        final String surname = "";
        final String phoneNumber = "111222333";
        assertFalse(DataChecker.checkNameSurnamePhone(name, surname, phoneNumber));
    }

    @Test
    void dataCheckerCheckNameSurnamePhoneInvalidPhone() {
        final String name = "test";
        final String surname = "test";
        final String phoneNumber = "";
        assertFalse(DataChecker.checkNameSurnamePhone(name, surname, phoneNumber));
    }

    @Test
    void dataCheckerCheckNameSurnamePhoneInvalidPhoneValue() {
        final String name = "test";
        final String surname = "teste";
        final String phoneNumber = "kkkaaavvv";
        assertFalse(DataChecker.checkNameSurnamePhone(name, surname, phoneNumber));
    }

    @Test
    void dataCheckerCheckNameSurnamePhoneInvalidLogin() {
        final String name = "test";
        final String surname = "test";
        final String phoneNumber = "111222333";
        final String login = "";
        final String pass = "admin";
        assertFalse(DataChecker.checkNameSurnamePhone(name, surname, phoneNumber, login, pass));
    }

    @Test
    void dataCheckerCheckNameSurnamePhoneInvalidPassword() {
        final String name = "test";
        final String surname = "test";
        final String phoneNumber = "111222333";
        final String login = "admin";
        final String pass = "";
        assertFalse(DataChecker.checkNameSurnamePhone(name, surname, phoneNumber, login, pass));
    }

    @Test
    void dataCheckerCheckPesel() {
        final String pesel = "11122299988";
        assertTrue(DataChecker.checkPesel(pesel));
    }

    @Test
    void dataCheckerCheckPeselInvalid() {
        final String pesel = "1112229998";
        assertFalse(DataChecker.checkPesel(pesel));
    }

    @Test
    void dataCheckerCheckDate() {
        final String date = "2023-01-21";
        assertTrue(DataChecker.checkDate(date));
    }

    @Test
    void dataCheckerCheckDateEmpty() {
        final String date = "";
        assertFalse(DataChecker.checkDate(date));
    }

    @Test
    void dataCheckerCheckDateRandomString() {
        final String date = "ajxe";
        assertFalse(DataChecker.checkDate(date));
    }

    @Test
    void dataCheckerCheckDateInvalidDate() {
        final String date = "9999-99-99";
        assertFalse(DataChecker.checkDate(date));
    }
}
