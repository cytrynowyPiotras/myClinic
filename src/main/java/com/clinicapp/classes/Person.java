package com.clinicapp.classes;

public class Person {
    private String name;
    private String surname;
    private String phoneNumber;

    boolean correctData() {
        if (name.length() == 0) {return false;}
        if (surname.length() == 0) {{return false;}}
        if (phoneNumber.length() != 9) {{return false;}}
        return true;
    }
    public String getName() {return this.name;}
    public String getSurname() {return this.surname;}
    public String getPhoneNumber() {return this.phoneNumber;}

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Person(String name, String surname, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        if (!correctData()) {throw new RuntimeException("Persons data incorrect");}
    }
}
