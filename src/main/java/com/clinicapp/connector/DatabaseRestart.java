package com.clinicapp.connector;

import java.io.*;
import java.sql.PreparedStatement;


public class DatabaseRestart extends DatabaseConnector{
    public static void init() throws IOException {
        // creating necessary tables in database
        try {
            File file = new File("sql/create_tables.sql");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String command = "";
            while ((line = br.readLine()) != null) {
                if (line.length() == 0) continue;
                if (line.charAt(line.length() - 1) == ';') {
                    command += line;
                    PreparedStatement preparedStatement1 = conn.prepareStatement(command);
                    preparedStatement1.executeUpdate();
                    command = "";
                } else {
                    command += " " + line;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(){
        // deleting every table in database
        try {
            String sql = "exec destroy_tables";
            PreparedStatement ps1 = conn.prepareCall(sql);
            ps1.execute();
            } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


