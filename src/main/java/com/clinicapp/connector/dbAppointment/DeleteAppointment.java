package com.clinicapp.connector.dbAppointment;

import com.clinicapp.connector.DatabaseConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DeleteAppointment extends DatabaseConnector {
    public static void delete(String appointmentId) {
        // deleting appointment form database
        try {

            String sql = "DELETE FROM terminy WHERE id_terminu='" + appointmentId + "'";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
