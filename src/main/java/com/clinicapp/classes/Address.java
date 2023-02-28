package com.clinicapp.classes;

public class Address {
    private String addressId;
    private String street;
    private String postalCode;
    private String city;

    public Address(String addressId, String street, String postalCode, String city) {
        this.addressId = addressId;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
    }

    public String getAddressId() {return addressId;}

    public void setAddressId(String addressId) {this.addressId = addressId;}

    public String getStreet() {return street;}

    public void setStreet(String street) {this.street = street;}

    public String getPostalCode() {return postalCode;}

    public void setPostalCode(String postalCode) {this.postalCode = postalCode;}

    public String getCity() {return city;}

    public void setCity(String city) {this.city = city;}

}
