package com.clinicapp.connector.dbPatient;

import com.clinicapp.connector.DatabaseConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RemovePatient extends DatabaseConnector {
    public static void remove(String ppesel) {
        // deleting patient from database
        try {
            String sql = "DELETE FROM pacjenci WHERE pesel='" + ppesel + "'";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
