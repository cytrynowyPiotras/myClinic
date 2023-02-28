package com.clinicapp.connector.dbAppointment;

import com.clinicapp.classes.Appointment;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;

public class GetEarliestAppointments extends DatabaseConnector {
    public static ArrayList<Appointment> get(String spec, int numberOfVisits) {
        // returns [numberOfVisits] closest in time free appointments
        ArrayList<Appointment> ret = new ArrayList<Appointment>();
        try {
            String sql = "SELECT t.* FROM terminy t join lekarze l on (t.id_lekarza = l.id_lekarza) join specjalizacje s on (l.id_specjalizacji = s.id_specjalizacji) WHERE s.nazwa = '"+ spec +"' AND data_godzina > SYSDATE AND t.pesel_pacjenta is null ORDER BY data_godzina fetch first "+ Integer.toString(numberOfVisits) +" rows only";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String docId = rs.getString("id_lekarza");
                String sqlData = rs.getString("data_godzina");
                String appId = rs.getString("id_terminu");
                String[] splitted = sqlData.split(" ");
                Appointment ddd = new Appointment(splitted[0], splitted[1], docId, appId);
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
