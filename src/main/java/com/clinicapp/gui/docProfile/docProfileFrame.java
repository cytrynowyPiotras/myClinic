package com.clinicapp.gui.docProfile;

import com.clinicapp.classes.Doctor;
import com.clinicapp.classes.UserStatus;
import com.clinicapp.connector.dbAppointment.GetLastAppointment;
import com.clinicapp.connector.dbDoctor.GetDoctors;
import com.clinicapp.connector.dbDoctor.RemoveDoctor;
import com.clinicapp.connector.dbSpecialization.GetSpecById;
import com.clinicapp.gui.docUpdater.docUpdaterFrame;
import com.clinicapp.gui.doctoradder.DoctorAdderFrame;
import com.clinicapp.gui.setTimetable.setTimetableFrame;
import com.clinicapp.gui.showDocVisits.showDocsVisitsFrame;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class docProfileFrame {
    private JComboBox docComboBox;
    private JButton removeDocButton;
    private JLabel searchedIdLabel;
    private JLabel searchedPhoneLabel;
    private JPanel removePanel;
    private JLabel foundDocPhoneLabel;
    private JLabel foundDocIdLabel;
    private JLabel lastVisitLabel;
    private JLabel medSpecLabel;
    private JButton visitListButton;
    private JButton changeDataButton;
    private JButton dodajLekarzaButton;
    private JButton setTimetableButton;
    private Doctor selectedDoctor = null;
    private ArrayList<Doctor> docArray;
    private static UserStatus userStatus;
    private Doctor loggedDoc = null;


    public docProfileFrame(JButton creatorButton, UserStatus userStatus, Doctor doc) {
        JFrame frame = new JFrame("Lekarze");
        this.userStatus = userStatus;
        loggedDoc = doc;
        selectedDoctor = doc;

        setComboBox();
        docComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int selectedIndex = docComboBox.getSelectedIndex();
                if (selectedIndex == 0 || selectedIndex == -1) {
                    selectedDoctor = null;
                    foundDocPhoneLabel.setText("");
                    foundDocIdLabel.setText("");
                    lastVisitLabel.setText("");
                    medSpecLabel.setText("");
                    return;
                }
                Doctor doc = docArray.get(selectedIndex - 1);
                String phone = doc.getPhoneNumber();
                String id = doc.getId();
                String specId = doc.getSpecializationId();
                String specName;
                try {
                    specName = GetSpecById.get(specId).get(0).getName();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Brak połączenia", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                medSpecLabel.setText(specName + "  ");
                foundDocIdLabel.setText(id + "  ");
                foundDocPhoneLabel.setText(phone + "  ");
                String lastDate;
                try {
                    lastDate = GetLastAppointment.get(doc.getId());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Brak połączenia", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (lastDate.isEmpty()) {
                    lastVisitLabel.setText("Brak terminów  ");
                } else {
                    lastVisitLabel.setText(lastDate + "  ");
                }
                selectedDoctor = doc;
            }
        });
        frame.setContentPane(removePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(500, 300, 650, 250);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                creatorButton.setEnabled(true);
            }
        });
        frame.setVisible(true);
        removeDocButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userStatus != UserStatus.Admin) {
                    JOptionPane.showMessageDialog(null, "Brak dostępu!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (selectedDoctor == null) {
                    JOptionPane.showMessageDialog(null, "Musisz wybrać lekarza!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int decision = JOptionPane.showConfirmDialog(null, "Na pewno?", "Usuwasz doktora!", JOptionPane.YES_NO_OPTION);
                if (decision == 0) {
                    String idToDelete = selectedDoctor.getId();
                    try {
                        RemoveDoctor.remove(idToDelete);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Nie dodano doktora - błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    creatorButton.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "Doktor usunięty z bazy danych!", "Sukces!", JOptionPane.INFORMATION_MESSAGE);
                    setComboBox();
                }
            }
        });
        dodajLekarzaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (userStatus != UserStatus.Admin) {
                    JOptionPane.showMessageDialog(null, "Brak dostępu", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                openAddDocFrame();
            }
        });

        setTimetableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        changeDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userStatus != UserStatus.Admin) {
                    JOptionPane.showMessageDialog(null, "Brak dostępu!", "Erroe!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (selectedDoctor == null) {
                    JOptionPane.showMessageDialog(null, "Musisz wybrać lekarza!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                new docUpdaterFrame(changeDataButton, selectedDoctor);
                changeDataButton.setEnabled(false);
            }
        });
        setTimetableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedDoctor == null) {
                    JOptionPane.showMessageDialog(null, "Musisz wybrać lekarza!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                setTimetableButton.setEnabled(false);
                new setTimetableFrame(setTimetableButton, selectedDoctor);
            }
        });
        visitListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (selectedDoctor == null) {
                    JOptionPane.showMessageDialog(null, "Musisz wybrać lekarza!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                visitListButton.setEnabled(false);
                new showDocsVisitsFrame(visitListButton, selectedDoctor);
            }
        });
    }

    private void openAddDocFrame() {
        new DoctorAdderFrame(dodajLekarzaButton, this);
        dodajLekarzaButton.setEnabled(false);
    }

    public void setComboBox() {
        if (userStatus == UserStatus.Doctor) {
            Doctor doc = loggedDoc;
            String phone = doc.getPhoneNumber();
            String id = doc.getId();
            String specId = doc.getSpecializationId();

            String specName;
            try {
                specName = GetSpecById.get(specId).get(0).getName();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Brak połączenia", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String lastDate;
            try {
                lastDate = GetLastAppointment.get(doc.getId());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Brak połączenia", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (lastDate.isEmpty()) {
                lastVisitLabel.setText("Brak terminów  ");
            } else {
                lastVisitLabel.setText(lastDate + "  ");
            }

            medSpecLabel.setText(specName + "  ");
            foundDocIdLabel.setText(id + "  ");
            foundDocPhoneLabel.setText(phone + "  ");
            docComboBox.addItem("Doktor " + doc.getName() + " " + doc.getSurname());
            return;

        }

        ArrayList<Doctor> docArray = GetDoctors.get();
        this.docArray = docArray;
        docComboBox.removeAllItems();
        docComboBox.addItem("Wybierz doktora");
        for (int i = 0; i < docArray.size(); i++) {
            Doctor doc = docArray.get(i);
            docComboBox.addItem(String.valueOf(i + 1) + ". Dr " + doc.getName() + " " + doc.getSurname());
        }
    }

    public static void main(String[] args) {
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
        removePanel = new JPanel();
        removePanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        removePanel.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        searchedIdLabel = new JLabel();
        searchedIdLabel.setText(" Id doktora:");
        panel3.add(searchedIdLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(150, -1), 0, false));
        foundDocIdLabel = new JLabel();
        foundDocIdLabel.setText("");
        panel3.add(foundDocIdLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 19), null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        searchedPhoneLabel = new JLabel();
        searchedPhoneLabel.setHorizontalAlignment(10);
        searchedPhoneLabel.setText(" Numer telefonu doktora:");
        panel4.add(searchedPhoneLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(150, -1), 0, false));
        foundDocPhoneLabel = new JLabel();
        foundDocPhoneLabel.setText("");
        panel4.add(foundDocPhoneLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 19), null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText(" Terminarz ważny do:");
        panel5.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(150, -1), 0, false));
        lastVisitLabel = new JLabel();
        lastVisitLabel.setText("");
        panel5.add(lastVisitLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel6, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText(" Specjalizacja:");
        panel6.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(150, -1), 0, false));
        medSpecLabel = new JLabel();
        medSpecLabel.setText("");
        panel6.add(medSpecLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(1, -1), null, null, 0, false));
        docComboBox = new JComboBox();
        panel2.add(docComboBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        removeDocButton = new JButton();
        removeDocButton.setFocusable(false);
        removeDocButton.setText("Usuń doktora");
        panel7.add(removeDocButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        visitListButton = new JButton();
        visitListButton.setFocusable(false);
        visitListButton.setText("Pokaż wizyty");
        panel7.add(visitListButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        changeDataButton = new JButton();
        changeDataButton.setFocusable(false);
        changeDataButton.setText("Zmień dane");
        panel7.add(changeDataButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dodajLekarzaButton = new JButton();
        dodajLekarzaButton.setFocusable(false);
        dodajLekarzaButton.setText("Dodaj lekarza");
        panel7.add(dodajLekarzaButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        setTimetableButton = new JButton();
        setTimetableButton.setText("Ustaw terminarz");
        panel7.add(setTimetableButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        removePanel.add(spacer1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        removePanel.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return removePanel;
    }

}