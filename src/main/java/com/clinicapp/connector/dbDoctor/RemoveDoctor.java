package com.clinicapp.connector.dbDoctor;

import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;

public class RemoveDoctor extends DatabaseConnector {
    public static void remove(String doc_id) {
        // deleting doctor from database
        try {
            String sql1 = "SELECT id_uzytkownika FROM uzytkownicy JOIN lekarze using (id_uzytkownika) WHERE id_lekarza = ?";
            PreparedStatement preparedStatement1 = conn.prepareStatement(sql1);
            preparedStatement1.setString(1, doc_id);
            ResultSet rs = preparedStatement1.executeQuery();
            String userId = null;
            while (rs.next()) {
                userId = rs.getString("id_uzytkownika");
            }
            rs.close();
            preparedStatement1.close();

            String sql = "DELETE FROM lekarze WHERE id_lekarza=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, doc_id);
            preparedStatement.executeUpdate();

            preparedStatement.close();

            if (userId != null && !userId.isEmpty()) {
                String sql2 = "DELETE FROM uzytkownicy WHERE id_uzytkownika=?";
                PreparedStatement preparedStatement2 = conn.prepareStatement(sql2);
                preparedStatement2.setString(1, userId);
                preparedStatement2.executeUpdate();

                preparedStatement2.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
