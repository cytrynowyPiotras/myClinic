package com.clinicapp.connector;
import com.clinicapp.classes.*;
import com.clinicapp.config.GetInputFile;
import oracle.jdbc.pool.OracleDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;


/**
 * DatabaseConnector class is used to establish and close
 * a connection with the database.
 * All classes handling database operations inherit from it
 * This class consists exclusively of static methods.
 */
public class DatabaseConnector {
    /**
     * Handles connection to database.
     * It's accessed by all inheriting classes.
     */
    protected static Connection conn;

    /**
     * Sets connection to database
     * @throws SQLException
     * @throws IOException
     */
    public static void setConnection() throws SQLException, IOException {
        Properties prop = new Properties();
        GetInputFile temp = new GetInputFile();
        InputStream dbconfig = temp.getFile("connection.properties");
        prop.load(dbconfig);
        dbconfig.close();
        String host = prop.getProperty("jdbc.host");
        String username = prop.getProperty("jdbc.username");
        String password = prop.getProperty("jdbc.password");
        String port = prop.getProperty("jdbc.port");
        String serviceName = prop.getProperty("jdbc.service.name");

        String connString = String.format("jdbc:oracle:thin:%s/%s@//%s:%s/%s", username, password, host, port, serviceName);
        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setURL(connString);
        conn = dataSource.getConnection();
    }

    /**
     * Closes connection to database
     * @throws SQLException
     */
    public static void closeConnection() throws SQLException {
        conn.close();
        System.out.println("Disconnected from database");
    }
}
