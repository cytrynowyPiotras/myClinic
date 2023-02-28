package com.clinicapp.gui.patientsTable;

import com.clinicapp.classes.Patient;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class patientsTableFrame {
    JFrame frame;
    JTable jTable;
    String[][] myData;

    public patientsTableFrame(JButton creatorButton, ArrayList<Patient> patientsList)
    {
        frame = new JFrame("Lista pacjentów");

        JButton button;
        button = creatorButton;
        myData = new String[patientsList.size()][];

        for (int i=0; i<patientsList.size(); i++) {
            Patient patient = patientsList.get(i);
            String[] patientsData = {
                    patient.getPesel(),
                    patient.getName(),
                    patient.getSurname(),
                    patient.getPhoneNumber()};
            myData[i] = patientsData;
        }
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                creatorButton.setEnabled(true);
            }
        });


        String[] columnNames = { "Pesel", "Imie", "Nazwisko", "Numer telefonu" };
        jTable = new JTable(myData, columnNames);
        JScrollPane sp = new JScrollPane(jTable);
        frame.add(sp);
        frame.setBounds(500, 300, 800, 300);
        frame.setSize(500, 200);
        frame.setVisible(true);

    }

};
