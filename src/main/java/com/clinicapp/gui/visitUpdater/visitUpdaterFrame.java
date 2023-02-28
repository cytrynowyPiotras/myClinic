package com.clinicapp.gui.visitUpdater;

import com.clinicapp.classes.Appointment;
import com.clinicapp.classes.DataChecker;
import com.clinicapp.classes.Doctor;
import com.clinicapp.classes.Specialization;
import com.clinicapp.connector.dbAppointment.AssignAppointment;
import com.clinicapp.connector.dbAppointment.GetAppointmentByDateSpec;
import com.clinicapp.connector.dbAppointment.GetEarliestAppointments;
import com.clinicapp.connector.dbAppointment.RemovePatientFromVisit;
import com.clinicapp.connector.dbDoctor.GetDoctorbyId;
import com.clinicapp.connector.dbSpecialization.GetSpecById;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class visitUpdaterFrame {
    private JTextField dateTextField;
    private JButton searchButton;
    private JComboBox myComboBox;
    private JButton changeButton;
    private JPanel visitJPanel;
    private JButton earliestVisitsButton;
    private String specName;
    private ArrayList<Appointment> myAppointment;
    private static final int numberOfVisits = 10;

    public visitUpdaterFrame(JButton creatorButton, String pesel, String specId, String visitId) {
        Specialization spec = GetSpecById.get(specId).get(0);
        specName = spec.getName();
        JFrame frame = new JFrame("Zmieniasz wizytę " + pesel + " do " + spec.getName());
        frame.setContentPane(visitJPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(370, 250, 700, 150);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                creatorButton.setEnabled(true);
                frame.dispose();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = dateTextField.getText();
                if (!DataChecker.checkDate(date)) {
                    JOptionPane.showMessageDialog(null, "Podaj poprawną datę!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                setSearchedComoboBox(date);
            }
        });
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIdx = myComboBox.getSelectedIndex() - 1;
                if (selectedIdx == -1) {
                    JOptionPane.showMessageDialog(null, "Musisz wybrać wizytę!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Appointment newApp = myAppointment.get(selectedIdx);
                try {
                    RemovePatientFromVisit.remove(visitId);
                    AssignAppointment.assign(newApp.getDateId(), pesel);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(null, "Zmieniłeś wizytę!", "Sukces!", JOptionPane.INFORMATION_MESSAGE);
                creatorButton.setEnabled(true);
                frame.dispose();
            }
        });
        earliestVisitsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myComboBox.removeAllItems();
                ArrayList<Appointment> myApp = GetEarliestAppointments.get(specName, numberOfVisits);
                myAppointment = myApp;
                myComboBox.addItem("Wybierz wizytę");
                for (int i = 0; i < myApp.size(); i++) {
                    Appointment app = myApp.get(i);
                    Doctor doc;
                    try {
                        doc = GetDoctorbyId.get(app.getDoctorId()).get(0);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String desc = app.getDay().toString() + " " + app.getTime().toString() + " " + doc.getName() + " " + doc.getSurname();
                    myComboBox.addItem(desc);
                }
            }
        });

    }

    private void setSearchedComoboBox(String date) {
        myComboBox.removeAllItems();
        ArrayList<Appointment> myApp = GetAppointmentByDateSpec.getVisitsByDateSpec(date, specName);
        myAppointment = myApp;
        myComboBox.addItem("Wybierz wizytę");
        for (int i = 0; i < myApp.size(); i++) {
            Appointment app = myApp.get(i);
            Doctor doc;
            try {
                doc = GetDoctorbyId.get(app.getDoctorId()).get(0);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String desc = app.getTime().toString() + " " + doc.getName() + " " + doc.getSurname();
            myComboBox.addItem(desc);
        }
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        visitJPanel = new JPanel();
        visitJPanel.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        visitJPanel.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText(" Wpisz datę(yyyy-mm-dd): ");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dateTextField = new JTextField();
        panel1.add(dateTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        searchButton = new JButton();
        searchButton.setText("Szukaj");
        panel1.add(searchButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        earliestVisitsButton = new JButton();
        earliestVisitsButton.setText("Znajdź najszybsze");
        panel1.add(earliestVisitsButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        visitJPanel.add(spacer1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        myComboBox = new JComboBox();
        visitJPanel.add(myComboBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        changeButton = new JButton();
        changeButton.setText("Zmień");
        visitJPanel.add(changeButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        visitJPanel.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return visitJPanel;
    }

}
