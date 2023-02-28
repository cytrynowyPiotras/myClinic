package com.clinicapp.connector.dbAppointment;

import com.clinicapp.connector.DatabaseConnector;

import java.sql.PreparedStatement;

public class DeleteExpiredAppointments extends DatabaseConnector {
    public static void delete() {
        // deleting empty appointments slots from past
        try {
            String sql = "DELETE FROM terminy WHERE data_godzina < to_char(sysdate, 'yyyy-MM-dd') AND pesel_pacjenta IS NULL";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
