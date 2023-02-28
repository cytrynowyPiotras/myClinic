package com.clinicapp.connector.dbDoctor;

import com.clinicapp.classes.Doctor;
import com.clinicapp.connector.DatabaseConnector;
import com.clinicapp.connector.dbUser.PasswordHash;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class AddDoctor extends DatabaseConnector {
    /**
     * Adds new doctor in database and new user in database. User's
     * password is hashed before saving. Method creates reference key between doctor
     * and user in database, login has to be unique
     * <p>
     *
     * @param  doc  new doctor object
     * @param  login unique string
     * @param  password string which will be hashed before save in database
     *
     *
     */
    public static void add(Doctor doc, String login, String password) {
        try {
            // creating new doctor with new user id
            String sql = "{call dodaj_nowego_lekarza('"+doc.getName()+"', '"+doc.getSurname()+"', '"+doc.getPhoneNumber()+"', '"+login+"', '"+PasswordHash.hash(password)+"', 10000, u_id.nextval, 1, 4)}";
            PreparedStatement cstmt = conn.prepareCall(sql);
            cstmt.execute();
            cstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
