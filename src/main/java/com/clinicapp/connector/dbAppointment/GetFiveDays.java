package com.clinicapp.connector.dbAppointment;

import com.clinicapp.classes.Appointment;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GetFiveDays extends DatabaseConnector {
    public static ArrayList<ArrayList<Appointment>> get(String docId) {
        // return 5 lists of appointments to doctors each one for one day
        ArrayList<ArrayList<Appointment>> appointments = new ArrayList<ArrayList<Appointment>>();
        LocalDate tempDate = LocalDate.now();
        for (int i = 0; i < 5; i++) {
            ArrayList<Appointment> dayVisits = new ArrayList<Appointment>();
            try {
                String setter = "alter session set nls_date_format='YYYY-MM-DD HH24:MI'";
                PreparedStatement preparedStatement1 = conn.prepareStatement(setter);
                preparedStatement1.executeUpdate();
                preparedStatement1.close();

                String sql = "SELECT * FROM terminy WHERE data_godzina > ? AND data_godzina <= ? AND id_lekarza = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, tempDate + " 00:00");
                preparedStatement.setString(2, tempDate + " 23:59");
                preparedStatement.setString(3, docId);
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    String term_id = rs.getString("id_terminu");
                    String sql_data = rs.getString("data_godzina");
                    String doc_id = rs.getString("id_lekarza");
                    String pesel = rs.getString("pesel_pacjenta");
                    String[] splitted = sql_data.split(" ");
                    Appointment tempAppointment = new Appointment(splitted[0], splitted[1], doc_id, term_id);
                    if (pesel != null) {
                        tempAppointment.setPatientPesel(pesel);
                    }
                    dayVisits.add(tempAppointment);
                }
                rs.close();
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            tempDate = tempDate.plusDays(1);
            appointments.add(dayVisits);
        }
        return appointments;
    }


}
