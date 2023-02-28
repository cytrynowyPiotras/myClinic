

package com.clinicapp.gui.patientWindow;

import com.clinicapp.classes.Patient;
import com.clinicapp.classes.UserStatus;
import com.clinicapp.connector.DatabaseConnector;
import com.clinicapp.connector.dbPatient.GetPatientByPesel;
import com.clinicapp.connector.dbPatient.GetPatients;
import com.clinicapp.gui.patientadder.patientAdderFrame;
import com.clinicapp.gui.patientsProfile.patientsProfileFrame;
import com.clinicapp.gui.patientsTable.patientsTableFrame;
import com.clinicapp.gui.setAppointment.setAppointmentFrame;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;

public class patientWindow {

    private JTextField searchedPeselTextField;
    private JList peselList;
    private JPanel patientsWindowJPanel;
    private JLabel label1;
    private JButton showProfileButton;
    private JButton addPatientButton;
    private JButton addVisitButton;
    private JButton dataTableButton;
    private String selectedPesel = "";
    DefaultListModel defListModel = new DefaultListModel();
    private JButton creatorButton;
    private UserStatus userStatus;

    public ArrayList getData() {
        ArrayList myData = new ArrayList();
        ArrayList<Patient> myPatients;
        try {
            myPatients = GetPatients.get();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE);
            return myData;
        }
        for (int i = 0; i < myPatients.size(); i++) {
            Patient patient = myPatients.get(i);
            String pesel = patient.getPesel();
            myData.add(pesel);
        }
        return myData;
    }

    public patientWindow(JButton creatorButton, UserStatus user) {
        bindData();
        this.creatorButton = creatorButton;
        this.userStatus = user;

        JFrame frame = new JFrame("Pacjenci");
        frame.setContentPane(patientsWindowJPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(550, 300, 500, 500);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                creatorButton.setEnabled(true);
            }
        });
        searchedPeselTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchFilter(searchedPeselTextField.getText());
            }
        });
        peselList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                try {
                    selectedPesel = peselList.getSelectedValue().toString();
                } catch (Exception lk) {
                    return;
                }
                searchedPeselTextField.setText(selectedPesel);
            }
        });
        addPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user == UserStatus.Doctor) {
                    JOptionPane.showMessageDialog(null, "Brak dostępu!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                openPatientAdderFrame();
            }
        });
        dataTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataTableButton.setEnabled(false);
                ArrayList<Patient> patList;
                try {
                    patList = GetPatients.get();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                new patientsTableFrame(dataTableButton, patList);
            }
        });
        showProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedPesel.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Musisz wybrać pesel!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Patient myPat;
                try {
                    myPat = GetPatientByPesel.get(selectedPesel).get(0);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                openPatientsProfileFrame(myPat);
            }
        });
        addVisitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userStatus == UserStatus.Doctor) {
                    JOptionPane.showMessageDialog(null, "Brak dostępu!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (Objects.equals(selectedPesel, "")) {
                    JOptionPane.showMessageDialog(null, "Wybierz pacjenta", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                new setAppointmentFrame(addVisitButton, selectedPesel);
                addVisitButton.setEnabled(false);
            }
        });
    }

    private void openPatientsProfileFrame(Patient myPat) {
        showProfileButton.setEnabled(false);
        new patientsProfileFrame(myPat, showProfileButton, this, userStatus);
    }

    private void openPatientAdderFrame() {
        addPatientButton.setEnabled(false);
        new patientAdderFrame(addPatientButton, this);
    }

    private void searchFilter(String term) {
        DefaultListModel filteredItems = new DefaultListModel();
        ArrayList data = getData();

        data.stream().forEach((dataPes) -> {
            String pesel = dataPes.toString();
            if (pesel.contains(term)) {
                filteredItems.addElement(dataPes);
            }
        });
        defListModel = filteredItems;
        peselList.setModel(defListModel);

    }

    public void bindData() {
        peselList.removeAll();
        defListModel.removeAllElements();
        getData().stream().forEach((data) -> {
            defListModel.addElement(data);
        });
        peselList.setModel(defListModel);
        peselList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        patientsWindowJPanel = new JPanel();
        patientsWindowJPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        patientsWindowJPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        searchedPeselTextField = new JTextField();
        patientsWindowJPanel.add(searchedPeselTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        label1 = new JLabel();
        label1.setText(" Wpisz szukany pesel: ");
        patientsWindowJPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        patientsWindowJPanel.add(panel1, new GridConstraints(1, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        peselList = new JList();
        panel1.add(peselList, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        patientsWindowJPanel.add(panel2, new GridConstraints(1, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        addVisitButton = new JButton();
        addVisitButton.setFocusable(false);
        addVisitButton.setText("Dodaj wizytę");
        panel2.add(addVisitButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(11, 199), null, 0, false));
        addPatientButton = new JButton();
        addPatientButton.setFocusable(false);
        addPatientButton.setText("Dodaj pacjenta");
        panel2.add(addPatientButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dataTableButton = new JButton();
        dataTableButton.setActionCommand("Pokaż tabelę danych");
        dataTableButton.setFocusable(false);
        dataTableButton.setLabel("Pokaż dane pacjentów");
        dataTableButton.setText("Pokaż dane pacjentów");
        panel2.add(dataTableButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        showProfileButton = new JButton();
        showProfileButton.setFocusable(false);
        showProfileButton.setText("Wyświetl profil");
        panel2.add(showProfileButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return patientsWindowJPanel;
    }

}
