package com.clinicapp.connector.dbSpecialization;

import com.clinicapp.classes.Specialization;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;

public class GetSpecById extends DatabaseConnector {
    public static ArrayList<Specialization> get(String specId) {
        // returns spec by given ID
        ArrayList<Specialization> specializations = new ArrayList<Specialization>();
        try {
            String sql = "SELECT * FROM specjalizacje WHERE id_specjalizacji=" + specId;
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String spId = rs.getString("id_specjalizacji");
                String name = rs.getString("nazwa");
                Specialization specialization= new Specialization(spId, name);
                specializations.add(specialization);
            }
            rs.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return specializations;
    }
}
