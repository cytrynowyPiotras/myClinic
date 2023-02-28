package com.clinicapp.connector.dbAppointment;

import com.clinicapp.classes.Appointment;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;

public class GetAvailableAppointments extends DatabaseConnector {
    public static ArrayList<Appointment> get(){
        // gets all available slots on appointments from database
        ArrayList<Appointment> appoitments = new ArrayList<Appointment>();
        try {
            String sql = "select * from terminy where pesel_pacjenta is null";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String docId = rs.getString("id_lekarza");
                String sqlData = rs.getString("data_godzina");
                String appId = rs.getString("id_terminu");
                String[] splitted = sqlData.split(" ");
                Appointment ddd = new Appointment(splitted[0], splitted[1], docId, appId);
                appoitments.add(ddd);
            }
            rs.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appoitments;
    }
}
