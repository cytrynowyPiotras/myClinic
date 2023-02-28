package com.clinicapp.gui.appTable;

import com.clinicapp.classes.Appointment;
import com.clinicapp.connector.DatabaseConnector;
import com.clinicapp.connector.dbAppointment.GetAllAppointments;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class appTableFrame {
    JFrame frame;
    JTable table;


    public appTableFrame(JButton creatorButton, ArrayList<String[]> appointments) {
        frame = new JFrame("Lista terminów");

        String[][] myData = new String[appointments.size()][];
        for (int i=0; i<appointments.size();i++) {
            myData[i] = appointments.get(i);
        }
        String[] columNames = {"Lekarz", "Dzień", "Godzina", "Imię", "Nazwisko", "Pesel"};
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
    public static void main(String[] args) {
        new appTableFrame(new JButton(), GetAllAppointments.get());
    }
}
