package com.clinicapp.connector.dbPastAppointment;

import com.clinicapp.classes.PastAppoinment;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;

public class GetPatientsPastAppointment extends DatabaseConnector {
    public static ArrayList<PastAppoinment> get (String pesel) {
        // returns the patients previous visits
        ArrayList<PastAppoinment> ret = new ArrayList<PastAppoinment>();
        try {

            String setter = "alter session set nls_date_format='YYYY-MM-DD HH24:MI'";
            PreparedStatement preparedStatement1 = conn.prepareStatement(setter);
            preparedStatement1.executeUpdate();
            preparedStatement1.close();

            String sql = "select * from zrealizowane_wizyty where pesel_pacjenta like '" + pesel +"'";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String appId = rs.getString("id_wizyt");
                String sqlData = rs.getString("data");
                String description = rs.getString("opis");
                String ppesel = rs.getString("pesel_pacjenta");
                String docId = rs.getString("id_lekarza");
                String[] splitted = sqlData.split(" ");
                PastAppoinment ddd = new PastAppoinment(splitted[0], splitted[1], docId, appId, description);
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
