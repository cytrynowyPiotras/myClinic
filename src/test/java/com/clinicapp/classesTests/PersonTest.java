package com.clinicapp.classesTests;

import com.clinicapp.classes.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PersonTest {
    public Person person = new Person("Adam", "Nowak", "111222333");

    @Test
    void personConstructorTest(){
        assertEquals("Adam", person.getName());
        assertEquals("Nowak", person.getSurname());
        assertEquals("111222333", person.getPhoneNumber());
    }

    @Test
    void personSetterTest(){
        person.setName("Ewa");
        person.setSurname("Nowacka");
        person.setPhoneNumber("111222334");
        assertEquals("Ewa", person.getName());
        assertEquals("Nowacka", person.getSurname());
        assertEquals("111222334", person.getPhoneNumber());
    }

    @Test
    void personErrorTest(){
        assertThrows(RuntimeException.class, ()->{
            Person error = new Person("", "", "1");
        });
    }
}
