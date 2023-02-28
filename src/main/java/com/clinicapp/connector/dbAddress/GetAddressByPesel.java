package com.clinicapp.connector.dbAddress;

import com.clinicapp.classes.Address;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;

public class GetAddressByPesel extends DatabaseConnector {
    public static Address get(String pesel) {
        // returns patients address by patients pesel
        Address address = null;
        try {
            String sql = "SELECT a.* FROM adresy a JOIN pacjenci p on (p.id_adresu = a.id_adresu) WHERE p.pesel = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, pesel);
            ResultSet rs = preparedStatement.executeQuery();
            String addressId ="";
            while (rs.next()) {
                addressId = rs.getString("id_adresu");
                String street = rs.getString("ulica");
                String postalCode = rs.getString("kod_pocztowy");
                String city = rs.getString("miasto");
                address = new Address(addressId, street, postalCode, city);
            }
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }
}
