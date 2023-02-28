package com.clinicapp.connector.dbUser;

import com.clinicapp.classes.Patient;
import com.clinicapp.classes.UserStatus;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;

public class GetUserStatus extends DatabaseConnector {
    public static UserStatus get(String login, String password) throws Exception{
        // returns status of given user or rasing error if password incorrect
        String status = "5";
        String hash_password = "";
        try {
            String sql = "SELECT id_statusu, haslo FROM uzytkownicy WHERE login = '" + login + "'";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                status = rs.getString("id_statusu");
                hash_password = rs.getString("haslo");
            }
            rs.close();
            preparedStatement.close();
            if(PasswordHash.checkPassword(password, hash_password)){
            return switch (status) {
                case "1" -> UserStatus.Doctor;
                case "2" -> UserStatus.Receptionist;
                case "3" -> UserStatus.Admin;
                default -> UserStatus.UserNotFound;
            };}
            else {
                throw new Exception("Invalid password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return UserStatus.Doctor;
    }
}
