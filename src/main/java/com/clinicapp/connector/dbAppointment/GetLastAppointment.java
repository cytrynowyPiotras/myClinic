package com.clinicapp.connector.dbAppointment;

import com.clinicapp.classes.Appointment;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;

public class GetLastAppointment extends DatabaseConnector {
    public static String get(String docId) {
        // returns last appointment of given doctor
        String lastDate = "";
        try {
            String setter = "alter session set nls_date_format='YYYY-MM-DD HH24:MI'";
            PreparedStatement preparedStatement1 = conn.prepareStatement(setter);
            preparedStatement1.executeUpdate();
            preparedStatement1.close();

            String sql = "SELECT * FROM terminy WHERE id_lekarza =" + docId +" ORDER BY data_godzina DESC FETCH FIRST ROW ONLY";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String termId = rs.getString("id_terminu");
                lastDate = rs.getString("data_godzina");
                String newDocId = rs.getString("id_lekarza");
                String[] splitted = lastDate.split(" ");
                Appointment ddd = new Appointment(splitted[0], splitted[1], newDocId, termId);
            }
                rs.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastDate;
    }
}
