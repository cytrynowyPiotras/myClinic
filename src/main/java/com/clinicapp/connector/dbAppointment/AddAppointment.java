package com.clinicapp.connector.dbAppointment;

import com.clinicapp.classes.Appointment;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.PreparedStatement;

public class AddAppointment extends DatabaseConnector {
    public static void add(Appointment dat) {
        // Saving appiontment class in database
        try {
            String setter = "alter session set nls_date_format='YYYY-MM-DD HH24:MI'";
            PreparedStatement preparedStatement1 = conn.prepareStatement(setter);
            preparedStatement1.executeUpdate();
            preparedStatement1.close();

            String sql = "INSERT INTO terminy (id_terminu, data_godzina, id_lekarza) VALUES (term_id.nextval, '"+dat.getDay().toString()+" "+dat.getTime().toString()+"', "+dat.getDoctorId()+")";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
