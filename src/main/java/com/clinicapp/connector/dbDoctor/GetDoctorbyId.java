package com.clinicapp.connector.dbDoctor;

import com.clinicapp.classes.Doctor;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;

public class GetDoctorbyId extends DatabaseConnector {
    public static ArrayList<Doctor> get(String idd){
        // returns data about doctor by docId
        ArrayList<Doctor> doctors = new ArrayList<Doctor>();
        try {
            String sql = "SELECT * FROM Lekarze WHERE Id_lekarza="+idd;
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String name = rs.getString("imie");
                String surname = rs.getString("nazwisko");
                String phone = rs.getString("nr_telefonu");
                String docId = rs.getString("id_lekarza");
                String userId = rs.getString("id_uzytkownika");
                String specId = rs.getString("id_specjalizacji");
                Doctor doc = new Doctor(name, surname, phone, docId, userId, specId);
                doctors.add(doc);
            }
            rs.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doctors;
    }
}
