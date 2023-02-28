package com.clinicapp.gui.pastVisitTable;

import com.clinicapp.classes.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.clinicapp.connector.DatabaseConnector;
import com.clinicapp.connector.dbAppointment.GetAllAppointments;
import com.clinicapp.connector.dbDoctor.GetDoctorbyId;
import com.clinicapp.connector.dbPastAppointment.GetPatientsPastAppointment;
import com.clinicapp.connector.dbPrescription.GetPrescriptionByPastAppointment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class pastVisitTableFrame {
    JFrame frame;
    JTable table;
    public pastVisitTableFrame (Patient pat, JButton creatorButton){

        frame = new JFrame("Lista terminów");

        ArrayList<String[]> appointments = prepareList(pat);



        String[][] myData = new String[appointments.size()][];
        for (int i=0; i<appointments.size();i++) {
            myData[i] = appointments.get(i);
        }
        String[] columNames = {"Data", "Godzina", "Lekarz", "Opis", "Leki", "Użycie"};
        table = new JTable(myData, columNames);
        JScrollPane sp = new JScrollPane(table);
        frame.add(sp);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                creatorButton.setEnabled(true);
                frame.dispose();
            }
        });
        ImageIcon icon = new ImageIcon("icons/calendarIcon.png");
        frame.setIconImage(icon.getImage());


        frame.setBounds(500, 300, 800, 300);
        frame.setVisible(true);
    }

    private ArrayList<String[]> prepareList(Patient pat) {
        ArrayList<String[]> myPastAppString = new ArrayList<>();
        ArrayList<PastAppoinment> myPastApp =GetPatientsPastAppointment.get(pat.getPesel());

        for (int i=0;i<myPastApp.size();i++) {
            PastAppoinment pastApp = myPastApp.get(i);
            String date = pastApp.getDay().toString();
            String hour = pastApp.getTime().toString();
            String docName = "Brak doktora w bazie";
            if (pastApp.getDoctorId() != null) {
                Doctor doc = GetDoctorbyId.get(pastApp.getDoctorId()).get(0);
                docName = doc.getName() + " " + doc.getSurname();
            }
            String visitDesc = pastApp.getDescription();
            String drugs = "Brak leków";
            String drugsUsage = "Brak leków";
            try {
                Prescription per = GetPrescriptionByPastAppointment.get(pastApp.getDateId());
                drugs = per.getDrugs();
                drugsUsage = per.getDescription();
            }
            catch (Exception m) {}
            String myRow[] = {date, hour, docName, visitDesc, drugs, drugsUsage};
            myPastAppString.add(myRow);

        }
        return myPastAppString;


    }
}
