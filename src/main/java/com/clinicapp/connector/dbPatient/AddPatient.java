package com.clinicapp.connector.dbPatient;

import com.clinicapp.classes.Address;
import com.clinicapp.classes.Patient;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;

public class AddPatient extends DatabaseConnector {
    public static void add(Patient pat, Address add) {
        // add patient to database
        try {
            String sql1 = "INSERT INTO adresy (id_adresu, ulica, kod_pocztowy, miasto) " +
                    "VALUES (add_id.nextval, ?, ?, ?)";
            PreparedStatement preparedStatement1 = conn.prepareStatement(sql1);
            preparedStatement1.setString(1, add.getStreet());
            preparedStatement1.setString(2, add.getPostalCode());
            preparedStatement1.setString(3, add.getCity());
            preparedStatement1.executeUpdate();
            preparedStatement1.close();

            String newAddName = "";
            String sql2 = "SELECT max(id_adresu) as wynik FROM adresy";
            PreparedStatement preparedStatement2 = conn.prepareStatement(sql2);
            ResultSet rs = preparedStatement2.executeQuery();
            while (rs.next()) {
                newAddName = rs.getString("wynik");
            }
            rs.close();
            preparedStatement2.close();

            String sql = "INSERT INTO pacjenci (pesel, imie, nazwisko, nr_telefonu, wzrost, waga, id_adresu) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, pat.getPesel());
            preparedStatement.setString(2, pat.getName());
            preparedStatement.setString(3, pat.getSurname());
            preparedStatement.setString(4, pat.getPhoneNumber());
            preparedStatement.setString(5, pat.getHeight());
            preparedStatement.setString(6, pat.getWeight());
            preparedStatement.setString(7, newAddName);
            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
