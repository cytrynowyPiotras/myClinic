package com.clinicapp.connector.dbPrescription;

import com.clinicapp.connector.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddPrescription extends DatabaseConnector {
    public static void make (String meds, String medsDescription, String IdAppintment) {
        // adding perscription to given appointment
        try {
            String sqlData="";
            String setter = "alter session set nls_date_format='YYYY-MM-DD HH24:MI'";
            PreparedStatement preparedStatement1 = conn.prepareStatement(setter);
            preparedStatement1.executeUpdate();
            preparedStatement1.close();

            String sql = "select * from zrealizowane_wizyty where id_wizyt=" + IdAppintment;
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                sqlData = rs.getString("data_godzina");
            }
            rs.close();
            preparedStatement.close();
            PreparedStatement preparedStatement11 = conn.prepareStatement(setter);
            preparedStatement11.executeUpdate();
            preparedStatement11.close();
            String sql2 = "INSERT INTO recepty (id_recepty, data_wystawienia, leki, opis, id_wizyty) "
                    + "VALUES (rec_id.nextval,'"+ sqlData.substring(0, sqlData.length() - 3)+"','"+meds+"','"+medsDescription+"',"+IdAppintment;
            PreparedStatement preparedStatement2 = conn.prepareStatement(sql2);
            preparedStatement2.executeUpdate();
            preparedStatement2.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
