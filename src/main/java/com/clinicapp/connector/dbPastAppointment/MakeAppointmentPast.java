package com.clinicapp.connector.dbPastAppointment;

import com.clinicapp.classes.PastAppoinment;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;

public class MakeAppointmentPast extends DatabaseConnector {
    public static void make (String description, String IdAppintment) {
        // execute visit by a doctor, saved new object of PastAppointment in database and delete object appointment
        // tworzy historie wizyty i usuwa termin
        try {
            PastAppoinment ddd = new PastAppoinment("2099-09-01", "04:30", "1", "1", "");
            String sqlData="";
            String setter = "alter session set nls_date_format='YYYY-MM-DD HH24:MI'";
            PreparedStatement preparedStatement1 = conn.prepareStatement(setter);
            preparedStatement1.executeUpdate();
            preparedStatement1.close();

            String sql = "select * from terminy where id_terminu=" + IdAppintment;
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String docId = rs.getString("id_lekarza");
                sqlData = rs.getString("data_godzina");
                String appId = rs.getString("id_terminu");
                String ppesel = rs.getString("pesel_pacjenta");
                String[] splitted = sqlData.split(" ");
                ddd = new PastAppoinment(splitted[0], splitted[1].substring(0, splitted[1].length() - 3), docId, appId, description);
                ddd.setPatientPesel(ppesel);
            }
            rs.close();
            preparedStatement.close();

            PreparedStatement preparedStatement11 = conn.prepareStatement(setter);
            preparedStatement11.executeUpdate();
            preparedStatement11.close();
            System.out.println(sqlData);
                String sql2 = "INSERT INTO ZREALIZOWANE_WIZYTY (id_wizyt, opis, data, pesel_pacjenta, id_lekarza) "
                        + "VALUES (" + IdAppintment + ",'" + description + "','" + sqlData.substring(0, sqlData.length() - 3) + "','" + ddd.getPatientPesel() + "', "+ddd.getDoctorId()+")";
                PreparedStatement preparedStatement2 = conn.prepareStatement(sql2);
                preparedStatement2.executeUpdate();
                preparedStatement2.close();

                String sql3 = "DELETE FROM terminy WHERE id_terminu=" + IdAppintment;
                PreparedStatement preparedStatement3 = conn.prepareStatement(sql3);
                preparedStatement3.executeUpdate();
                preparedStatement3.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
