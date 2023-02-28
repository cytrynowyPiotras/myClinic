package com.clinicapp.connector.dbAppointment;

import com.clinicapp.classes.Appointment;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;

public class GetPastAppointmentsByDocId extends DatabaseConnector {
    public static ArrayList<Appointment> get (String docId) {
        // returns list of taken appointments for given doctor, past by current date
        ArrayList<Appointment> ret = new ArrayList<Appointment>();
        try {

            String setter = "alter session set nls_date_format='YYYY-MM-DD HH24:MI'";
            PreparedStatement preparedStatement1 = conn.prepareStatement(setter);
            preparedStatement1.executeUpdate();
            preparedStatement1.close();

            String sql = "SELECT * FROM terminy WHERE id_lekarza ="+ docId +" AND data_godzina < SYSDATE AND pesel_pacjenta is not null ORDER BY data_godzina";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String doctId = rs.getString("id_lekarza");
                String sqlData = rs.getString("data_godzina");
                String appId = rs.getString("id_terminu");
                String ppesel = rs.getString("pesel_pacjenta");
                String[] splitted = sqlData.split(" ");
                Appointment ddd = new Appointment(splitted[0], splitted[1], doctId, appId);
                ddd.setPatientPesel(ppesel);
                ret.add(ddd);
            }
            rs.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
}

