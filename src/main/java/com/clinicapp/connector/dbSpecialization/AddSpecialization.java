package com.clinicapp.connector.dbSpecialization;

import com.clinicapp.classes.Specialization;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class AddSpecialization extends DatabaseConnector {
    public static void add(Specialization spec) {
        // add new spec to database
        try {
            String sql = "INSERT INTO specjalizacje (id_specjalizacji, nazwa) " +
                    "VALUES (spec_id.nextval, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, spec.getName());
            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
