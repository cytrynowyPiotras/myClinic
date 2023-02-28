package com.clinicapp.gui.setAppointment;

import com.clinicapp.classes.Appointment;
import com.clinicapp.classes.DataChecker;
import com.clinicapp.classes.Doctor;
import com.clinicapp.classes.Specialization;
import com.clinicapp.connector.dbAppointment.AssignAppointment;
import com.clinicapp.connector.dbAppointment.GetAppointmentByDateSpec;
import com.clinicapp.connector.dbAppointment.GetEarliestAppointments;
import com.clinicapp.connector.dbAppointment.RemovePatientFromVisit;
import com.clinicapp.connector.dbDoctor.GetDoctorbyId;
import com.clinicapp.connector.dbSpecialization.GetAllSpec;
import com.clinicapp.connector.dbSpecialization.GetSpecById;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.rmi.server.ExportException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class setAppointmentFrame {
    private JTextField dateTextField;
    private JPanel medSpecCheckBoxesJPanel;
    private JButton searchByDateButton;
    private JComboBox appComboBox;
    private JPanel setAppJPanel;
    private JButton setButton;
    private JButton findEarliestButton;
    private JButton earliestButton;
    private String peselOfPatient;

    private ArrayList<Specialization> mySpec;

    private ArrayList<JCheckBox> myCheckBoxes;
    private ArrayList<Appointment> myAppointments;

    private final static int numberOfVisits = 10;

    public setAppointmentFrame(JButton creator, String pesel) {
        peselOfPatient = pesel;
        addMedSpecCheckBoxes();
        JFrame frame = new JFrame("Ustaw wizytę dla: " + pesel);
        frame.setContentPane(setAppJPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(300, 300, 650, 400);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                creator.setEnabled(true);
                frame.dispose();
            }
        });
        ImageIcon icon = new ImageIcon("resources/icons/addDoc.png");
        frame.setIconImage(icon.getImage());
        frame.setVisible(true);
        searchByDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkCheckBoxes() == false) {
                    JOptionPane.showMessageDialog(null, "Wybierz jedną specjalizację", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String specId = getSelectedSpecId();
                String date = dateTextField.getText();
                setComboBox(specId, date);
            }
        });
        findEarliestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkCheckBoxes() == false) {
                    JOptionPane.showMessageDialog(null, "Wybierz jedną specjalizację", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String specId = getSelectedSpecId();
                setEarliestComboBox(specId);
            }
        });

        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIdx = appComboBox.getSelectedIndex() - 1;
                if (selectedIdx == -1) {
                    JOptionPane.showMessageDialog(null, "Musisz wybrać wizytę!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Appointment newApp;
                try {
                    newApp = myAppointments.get(selectedIdx);
                } catch (Exception ex2) { JOptionPane.showMessageDialog(null, "Musisz wybrać wizytę!", "Error!", JOptionPane.ERROR_MESSAGE); return; }
                try {
                    AssignAppointment.assign(newApp.getDateId(), pesel);
                } catch (Exception ex1) {  JOptionPane.showMessageDialog(null, "Błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE); return; }
                    JOptionPane.showMessageDialog(null, "Ustawiłeś wizytę!", "Sukces!", JOptionPane.INFORMATION_MESSAGE);
                creator.setEnabled(true);
                frame.dispose();
            }
        });


    }

    private void setComboBox(String specId, String date) {
        appComboBox.removeAllItems();
        appComboBox.addItem("Wybierz wizytę");
        Specialization spec;
        try {
            spec = GetSpecById.get(specId).get(0);
        } catch (Exception ex2) { JOptionPane.showMessageDialog(null, "Błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE); return; }
        if (!DataChecker.checkDate(date)) {
            JOptionPane.showMessageDialog(null, "Niepoprawna data", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ArrayList<Appointment> myVisits = GetAppointmentByDateSpec.getVisitsByDateSpec(date, spec.getName());
        myAppointments = myVisits;
        for (int i = 0; i < myVisits.size(); i++) {
            Appointment app = myVisits.get(i);
            Doctor doc;
            try {
                doc = GetDoctorbyId.get(app.getDoctorId()).get(0);
            } catch (Exception ex) {  JOptionPane.showMessageDialog(null, "Błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE); return; }
                String desc = app.getTime().toString() + " " + doc.getName() + " " + doc.getSurname();
            appComboBox.addItem(desc);
        }

    }

    private void setEarliestComboBox(String specId) {
        appComboBox.removeAllItems();
        appComboBox.addItem("Wybierz wizytę");
        Specialization spec;
        try {
            spec = GetSpecById.get(specId).get(0);
        } catch (Exception ex) { JOptionPane.showMessageDialog(null, "Błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE); return; }
        ArrayList<Appointment> myVisits = GetEarliestAppointments.get(spec.getName(), numberOfVisits);
        myAppointments = myVisits;
        for (int i = 0; i < myVisits.size(); i++) {
            Appointment app = myVisits.get(i);
            Doctor doc;
            try {
                doc = GetDoctorbyId.get(app.getDoctorId()).get(0);
            } catch (Exception ex1) { JOptionPane.showMessageDialog(null, "Błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE); return; }
            String desc = app.getDay().toString() + " " + app.getTime().toString() + " " + doc.getName() + " " + doc.getSurname();
            appComboBox.addItem(desc);
        }
    }


    private void addMedSpecCheckBoxes() {
        medSpecCheckBoxesJPanel.setLayout(new GridLayout(3, 0));
        ArrayList<Specialization> mySpec;
        try {
            mySpec = GetAllSpec.specializations();
        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE); return; }
        this.mySpec = mySpec;
        ArrayList<JCheckBox> myCheckBoxesLoc = new ArrayList<>();
        for (int i = 0; i < mySpec.size(); i++) {
            Specialization spec = mySpec.get(i);
            String specName = spec.getName();
            JCheckBox myCB = new JCheckBox(specName);
            myCheckBoxesLoc.add(myCB);
            medSpecCheckBoxesJPanel.add(myCB);
        }
        this.myCheckBoxes = myCheckBoxesLoc;
    }

    private String getSelectedSpecId() {
        int selectedIdx = -1;
        for (int i = 0; i < myCheckBoxes.size(); i++) {
            JCheckBox myBox = myCheckBoxes.get(i);
            if (myBox.isSelected()) {
                selectedIdx = i;
                break;
            }
        }
        JCheckBox selected = myCheckBoxes.get(selectedIdx);
        String specName = selected.getText();
        for (int k = 0; k < mySpec.size(); k++) {
            Specialization spec = mySpec.get(k);
            if (spec.getName() == specName) {
                return spec.getSpecializationId();
            }
        }
        return "";
    }

    public static void main(String args[]) {
        return;
    }

    private boolean checkCheckBoxes() {
        int counter = 0;
        for (int i = 0; i < myCheckBoxes.size(); i++) {
            JCheckBox myBox = myCheckBoxes.get(i);
            if (myBox.isSelected()) {
                counter++;
            }
        }
        if (counter != 1) {
            return false;
        }
        return true;
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
        setAppJPanel = new JPanel();
        setAppJPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        setAppJPanel.add(spacer1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        setAppJPanel.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(0);
        label1.setText("Wybierz specjalizację:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        medSpecCheckBoxesJPanel = new JPanel();
        medSpecCheckBoxesJPanel.setLayout(new GridBagLayout());
        panel2.add(medSpecCheckBoxesJPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        medSpecCheckBoxesJPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label2 = new JLabel();
        label2.setText(" Wybierz dostępną wizytę: ");
        panel3.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        appComboBox = new JComboBox();
        panel3.add(appComboBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        setButton = new JButton();
        setButton.setText("Ustaw wizytę");
        panel3.add(setButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label3 = new JLabel();
        label3.setText(" Wpisz datę (yyyy-mm-dd):");
        panel4.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dateTextField = new JTextField();
        panel4.add(dateTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        searchByDateButton = new JButton();
        searchByDateButton.setText("Szukaj");
        panel4.add(searchByDateButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        findEarliestButton = new JButton();
        findEarliestButton.setText("Znajdź najszybszą");
        panel4.add(findEarliestButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        setAppJPanel.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return setAppJPanel;
    }

}
