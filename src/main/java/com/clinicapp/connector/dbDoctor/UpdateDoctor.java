package com.clinicapp.connector.dbDoctor;

import com.clinicapp.classes.Doctor;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class UpdateDoctor extends DatabaseConnector {
    public static void update(Doctor doc){
        // changing data in database of given doctor
        try {
            String sql = "UPDATE lekarze set imie='"+ doc.getName()+"', nazwisko='"+ doc.getSurname()+"', nr_telefonu='"+ doc.getPhoneNumber()+"' where id_lekarza=" + doc.getId();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
