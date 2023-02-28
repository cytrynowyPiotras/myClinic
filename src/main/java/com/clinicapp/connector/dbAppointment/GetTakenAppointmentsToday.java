package com.clinicapp.connector.dbAppointment;

import com.clinicapp.classes.Appointment;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

public class GetTakenAppointmentsToday extends DatabaseConnector {
    public static ArrayList<Appointment> get() {
        // returns list of taken appointment in current day
        ArrayList<Appointment> appointments = new ArrayList<>();
        String startDay = LocalDate.now() + " 00:00";
        String endDay = LocalDate.now() + " 23:59";
        try {
            String setter = "alter session set nls_date_format='YYYY-MM-DD HH24:MI'";
            PreparedStatement preparedStatement1 = conn.prepareStatement(setter);
            preparedStatement1.executeUpdate();
            preparedStatement1.close();

            String sql = "SELECT * FROM terminy WHERE data_godzina BETWEEN ? and ? and pesel_pacjenta is not null";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, startDay);
            preparedStatement.setString(2, endDay);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {

                String appId = rs.getString("id_terminu");
                String sqlDate = rs.getString("data_godzina");
                String docId = rs.getString("id_lekarza");
                String pesel = rs.getString("pesel_pacjenta");
                String[] splitted = sqlDate.split(" ");
                Appointment app = new Appointment(splitted[0], splitted[1], docId, appId);
                if (pesel != null) {
                    app.setPatientPesel(pesel);
                }
                appointments.add(app);
            }
            rs.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointments;
    }
}
