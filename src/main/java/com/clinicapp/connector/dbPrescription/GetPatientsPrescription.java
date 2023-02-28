package com.clinicapp.connector.dbPrescription;

import com.clinicapp.classes.Prescription;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

public class GetPatientsPrescription extends DatabaseConnector {
    public static ArrayList<Prescription> get (String pesel) {
        // returns given patients perscriptions
        ArrayList<Prescription> ret = new ArrayList<Prescription>();
        try {
            String setter = "alter session set nls_date_format='YYYY-MM-DD HH24:MI'";
            PreparedStatement preparedStatement1 = conn.prepareStatement(setter);
            preparedStatement1.executeUpdate();
            preparedStatement1.close();
            String sql = "select r.* from recepty r join zrealizowane_wizyty zw on (r.id_wizyty = zw.id_wizyt) where zw.pesel_pacjenta like '" + pesel +"'";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String appId = rs.getString("id_wizyty");
                String sqlData = rs.getString("data_wystawienia");
                String description = rs.getString("opis");
                String med = rs.getString("leki");
                String perId = rs.getString("id_recepty");
                String[] splitted = sqlData.split(" ");
                Prescription ddd = new Prescription(perId, LocalDate.parse(splitted[0]), med, description);
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
