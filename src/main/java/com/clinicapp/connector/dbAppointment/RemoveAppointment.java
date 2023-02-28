package com.clinicapp.connector.dbAppointment;


import com.clinicapp.connector.DatabaseConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RemoveAppointment extends DatabaseConnector {
    public static void remove (String appointmentId) {
        // deletes appointment from database
        try {
            String sql = "DELETE FROM terminy WHERE id_terminu=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, appointmentId);
            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
