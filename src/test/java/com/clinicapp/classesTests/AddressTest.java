package com.clinicapp.classesTests;

import com.clinicapp.classes.Address;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AddressTest {
    private final Address address = new Address("1", "strname", "00-111", "Koszalin");

    @Test
    void addressConstructorTest(){
        assertEquals("1", address.getAddressId());
        assertEquals("strname", address.getStreet());
        assertEquals("00-111", address.getPostalCode());
        assertEquals("Koszalin", address.getCity());
    }

    @Test
    void addressSetterTest() {
        address.setAddressId("2");
        assertEquals("2", address.getAddressId());
        address.setCity("Kozienice");
        assertEquals("Kozienice", address.getCity());
        address.setStreet("Adama Mickiewicza");
        assertEquals("Adama Mickiewicza", address.getStreet());
        address.setPostalCode("341-000");
        assertEquals("341-000", address.getPostalCode());
    }

    @Test
    void addressSetterTestEmpty() {
        address.setPostalCode("");
        address.setCity("");
        address.setAddressId("");
        address.setStreet("");
        assertEquals("", address.getCity());
        assertEquals("", address.getPostalCode());
        assertEquals("", address.getStreet());
        assertEquals("", address.getAddressId());
    }

}
