package com.clinicapp;


import com.clinicapp.classes.Doctor;
import com.clinicapp.classes.UserStatus;
//import com.clinicapp.config.Settings;
import com.clinicapp.connector.DatabaseConnector;
import com.clinicapp.connector.dbAppointment.AddAppointmentsToDoc;
import com.clinicapp.connector.dbAppointment.DeleteExpiredAppointments;
import com.clinicapp.connector.dbDoctor.AddDoctor;
import com.clinicapp.connector.dbPastAppointment.MakeAppointmentPast;
import com.clinicapp.gui.MainWindow;
import com.clinicapp.gui.log.logFrame;

import java.rmi.ServerError;

public class Main {
    public static void main(String[] args) throws Exception {
        DatabaseConnector.setConnection();
        DeleteExpiredAppointments.delete();
        new logFrame();
    }
}
