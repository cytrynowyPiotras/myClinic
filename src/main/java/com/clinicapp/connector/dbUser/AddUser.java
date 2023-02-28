package com.clinicapp.connector.dbUser;

import com.clinicapp.classes.Doctor;
import com.clinicapp.classes.UserStatus;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class AddUser extends DatabaseConnector {
    public static void add(String login, String password, UserStatus status) {
        // Saving new user in database
        String statt = "";
        switch (status) {
            case Doctor:
                statt = "1";
            case Receptionist:
                statt = "2";
            case Admin:
                statt = "3";
        }
        try {
            String sql = "INSERT INTO uzytkownicy (id_uzytkownika, login, haslo, id_statusu) " +
                    "VALUES (u_id.nextval, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, PasswordHash.hash(password));
            preparedStatement.setString(3, statt);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
