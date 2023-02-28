package com.clinicapp.connector.dbAppointment;

import com.clinicapp.classes.Appointment;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;

public class GetAppointmentByDateSpec extends DatabaseConnector {
    /**
     * Returns list of available appointments with given doctors spec and date
     * The date argument must have format "yyyy-mm-dd". The spec
     * argument is specialization saved in database.
     * <p>
     * This method always returns ArrayList, if there is no appointment
     * in selected date and spec list will be empty
     *
     * @param  date  an absolute URL giving the base location of the image
     * @param  spec the location of the image, relative to the url argument
     * @return      ArrayList of objects Appointment
     *
     */
    public static ArrayList<Appointment> getVisitsByDateSpec (String date, String spec) {
        ArrayList<Appointment> ret = new ArrayList<Appointment>();
        try {
            // set proper date format
            String setter = "alter session set nls_date_format='YYYY-MM-DD HH24:MI'";
            PreparedStatement preparedStatement1 = conn.prepareStatement(setter);
            preparedStatement1.executeUpdate();
            preparedStatement1.close();
            //finding all appointments in database
            String sql = "SELECT t.* FROM terminy t join lekarze l on (t.id_lekarza = l.id_lekarza) join specjalizacje s on (l.id_specjalizacji = s.id_specjalizacji) WHERE s.nazwa = '"+ spec+"' AND DATA_GODZINA BETWEEN '"+ date +" 00:00' AND '"+ date +" 23:59'  AND data_godzina > SYSDATE AND t.pesel_pacjenta is null ORDER BY data_godzina";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String docId = rs.getString("id_lekarza");
                String sqlData = rs.getString("data_godzina");
                String appId = rs.getString("id_terminu");
                String[] splitted = sqlData.split(" ");
                Appointment ddd = new Appointment(splitted[0], splitted[1], docId, appId);
                // adding appointments to retruning ArrayList
                ret.add(ddd);
            }
            rs.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
