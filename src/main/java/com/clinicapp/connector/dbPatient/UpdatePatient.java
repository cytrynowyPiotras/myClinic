package com.clinicapp.connector.dbPatient;

import com.clinicapp.classes.Address;
import com.clinicapp.classes.Patient;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;

public class UpdatePatient extends DatabaseConnector {

    public static void update(Patient pat, Address add) {
        // update patient data
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

            String sql = "UPDATE pacjenci set imie='" + pat.getName() + "', nazwisko='" + pat.getSurname() + "', nr_telefonu='" + pat.getPhoneNumber() + "',wzrost= " + pat.getHeight() + ",waga= " + pat.getWeight() + ",id_adresu= " + newAddName + " where pesel= " + pat.getPesel();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
