package com.clinicapp.connector.dbAppointment;

import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;

public class GetAllAppointments extends DatabaseConnector {
    public static ArrayList<String[]> get() {
        // showing all appointments from database
        ArrayList<String[]> ret = new ArrayList<String[]>();
        try {
            String sql = "select l.imie , l.nazwisko, t.data_godzina, nvl(p.imie, 'wolny termin') as p_imie ,nvl(p.nazwisko, 'wolny termin') as p_nazwisko,nvl(p.pesel, 'wolny termin') as pesel from terminy t join lekarze l on(t.id_lekarza = l.id_lekarza) left outer join pacjenci p on (t.pesel_pacjenta = p.pesel)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {

                String lName = rs.getString("imie");
                String lSurname = rs.getString("nazwisko");
                String sql_data = rs.getString("data_godzina");
                String pName = rs.getString("p_imie");
                String pSurname = rs.getString("p_nazwisko");
                String ppesel = rs.getString("pesel");
                String[] splitted = sql_data.split(" ");

                String[] appData = {lName + " " + lSurname, splitted[0], splitted[1], pName, pSurname, ppesel};
                ret.add(appData);
            }
            rs.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
