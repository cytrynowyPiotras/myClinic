package com.clinicapp.connector.dbSpecialization;

import com.clinicapp.connector.DatabaseConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RemoveSpecialization extends DatabaseConnector {
    public static void remove(String specId) {
        // deleting spec from database
        try {
            String sql = "DELETE FROM specjalizacje WHERE id_specjalizacji=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, specId);
            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
