package com.clinicapp.connector.dbAppointment;

import com.clinicapp.connector.DatabaseConnector;

import java.sql.PreparedStatement;

public class AssignAppointment extends DatabaseConnector {
    public static void assign(String terminId, String patientPesel) {
        // assigning patient to a free visit
        try {

            String sql = "UPDATE terminy SET pesel_pacjenta = '"+ patientPesel +"' WHERE id_terminu = "+terminId;
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.executeQuery();

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
