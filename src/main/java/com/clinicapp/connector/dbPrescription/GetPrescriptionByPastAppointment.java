package com.clinicapp.connector.dbPrescription;

import com.clinicapp.classes.PastAppoinment;
import com.clinicapp.classes.Prescription;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class GetPrescriptionByPastAppointment extends DatabaseConnector {
    public static Prescription get (String appId) {
        // shows perscription given during given appointment
        Prescription pres = new Prescription("", LocalDate.of(1970, 1, 1), "", "");
        try {
            String sql = "SELECT * FROM recepty WHERE id_wizyty = "+ appId;
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String recipeId = rs.getString("id_recepty");
                String drugs = rs.getString("leki");
                String desc = rs.getString("opis");
                String pastAppId = rs.getString("id_wizyty");
                String date = rs.getString("data_wystawienia");
                String[] splitted = date.split(" ");
                pres = new Prescription(recipeId, LocalDate.parse(splitted[0]), drugs, desc);
            }
        } catch (Exception e) {e.printStackTrace();}
        return pres;
    }
}
