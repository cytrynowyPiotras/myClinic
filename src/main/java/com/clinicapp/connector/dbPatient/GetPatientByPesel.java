package com.clinicapp.connector.dbPatient;

import com.clinicapp.classes.Patient;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;

public class GetPatientByPesel extends DatabaseConnector {
    public static ArrayList<Patient> get(String ppesel) {
        // returns patient from database by given pesel
        ArrayList<Patient> patients = new ArrayList<Patient>();
        try {
            String sql = "SELECT * FROM PACJENCI WHERE PESEL LIKE '" + ppesel + "'";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String name = rs.getString("imie");
                String surname = rs.getString("nazwisko");
                String phone = rs.getString("nr_telefonu");
                String height = rs.getString("wzrost");
                String weight = rs.getString("waga");
                String addId = rs.getString("id_adresu");
                Patient pat1 = new Patient(name, surname, phone, ppesel, weight, height, addId);
                patients.add(pat1);
            }
            rs.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patients;
    }
}
