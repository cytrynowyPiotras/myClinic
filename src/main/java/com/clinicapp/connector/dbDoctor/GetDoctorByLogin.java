package com.clinicapp.connector.dbDoctor;

import com.clinicapp.classes.Doctor;
import com.clinicapp.connector.DatabaseConnector;
import com.clinicapp.connector.dbUser.PasswordHash;


import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class GetDoctorByLogin extends DatabaseConnector {
    public static Doctor get(String login, String password){
        // returns doctor by login
        Doctor ret = new Doctor("nie", "istnieje", "123456789", "0", "0", "0");
        try {
            String sql = "select l.* from lekarze l join uzytkownicy u on (l.id_uzytkownika = u.id_uzytkownika) where login = '"+ login +"' and haslo='"+ PasswordHash.hash(password) +"'";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String name = rs.getString("imie");
                String surname = rs.getString("nazwisko");
                String phone = rs.getString("nr_telefonu");
                String docId = rs.getString("id_lekarza");
                String userId = rs.getString("id_uzytkownika");
                String specId = rs.getString("id_specjalizacji");
                ret = new Doctor(name, surname, phone, docId, userId, specId);
            }
            rs.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
