package com.clinicapp.gui.docTable;

import com.clinicapp.classes.Doctor;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class docTableFrame {
    JFrame frame;
    JTable jTable;
    String[][] myData;

    public docTableFrame(JButton creatorButton, ArrayList<Doctor> docList)
    {
        frame = new JFrame("Lista doktorów");
        myData = new String[docList.size()][];

        for (int i=0; i<docList.size(); i++) {
            Doctor doc = docList.get(i);
            String[] docData = {
                    doc.getId(),
                    doc.getName(),
                    doc.getSurname(),
                    doc.getPhoneNumber()};
            myData[i] = docData;
        }

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                creatorButton.setEnabled(true);
            }
        });

        String[] columnNames = { "numer Id", "Imie", "Nazwisko", "Numer telefonu" };
        jTable = new JTable(myData, columnNames);
        JScrollPane sp = new JScrollPane(jTable);
        frame.add(sp);
        frame.setBounds(500, 300, 800, 300);
        frame.setVisible(true);

    }

};
