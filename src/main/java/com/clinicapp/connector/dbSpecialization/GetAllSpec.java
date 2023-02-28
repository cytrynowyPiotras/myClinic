package com.clinicapp.connector.dbSpecialization;

import com.clinicapp.classes.Specialization;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;

public class GetAllSpec extends DatabaseConnector {
    public static ArrayList<Specialization> specializations() {
        // shows all specs in database
        ArrayList<Specialization> specializations = new ArrayList<Specialization>();
        try {
            String sql = "SELECT * FROM specjalizacje";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String specId = rs.getString("id_specjalizacji");
                String specName = rs.getString("nazwa");
                Specialization spec = new Specialization(specId, specName);
                specializations.add(spec);
            }
            rs.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return specializations;
    }
}
