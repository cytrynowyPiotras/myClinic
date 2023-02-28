package com.clinicapp.classes;

public class Patient extends Person {
    private String name;
    private String surname;
    private String phoneNumber;
    private String pesel;
    private String weight;
    private String height;
    private String addressId;

    public Patient(String name, String surname, String phoneNumber, String pesel, String weight, String height, String addressId) {
        super(name, surname, phoneNumber);
        setPesel(pesel);
        setHeight(height);
        setWeight(weight);
        setAddressId(addressId);
    }

    public void setPesel(String newPesel) {
        if (newPesel.length() != 11) {
            throw new ArithmeticException("Pesel must consist of 11 numbers");
        }
        this.pesel = newPesel;
    }

    public String getPesel() { return this.pesel; }
    public String getWeight() {return weight;}

    public void setWeight(String weight) {this.weight = weight;}

    public String getHeight() {return height;}

    public void setHeight(String height) {this.height = height;}

    public String getAddressId() {return addressId;}

    public void setAddressId(String addressId) {this.addressId = addressId;}
}
