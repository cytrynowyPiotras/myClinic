package com.clinicapp.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import  java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import com.clinicapp.classes.Appointment;
import com.clinicapp.classes.Doctor;
import com.clinicapp.classes.Patient;
import com.clinicapp.classes.UserStatus;
import com.clinicapp.connector.DatabaseConnector;
import com.clinicapp.connector.dbAppointment.GetAllAppointments;
import com.clinicapp.connector.dbAppointment.GetFiveDays;
import com.clinicapp.connector.dbAppointment.GetPastAppointmentsByDocId;
import com.clinicapp.connector.dbAppointment.GetTakenAppointmentsToday;
import com.clinicapp.connector.dbDoctor.GetDoctorByLogin;
import com.clinicapp.connector.dbDoctor.GetDoctors;
import com.clinicapp.connector.dbPatient.GetPatientByPesel;
import com.clinicapp.gui.addSpec.addSpecFrame;
import com.clinicapp.gui.appTable.appTableFrame;

import com.clinicapp.gui.docProfile.docProfileFrame;
import com.clinicapp.gui.docTable.docTableFrame;

import com.clinicapp.gui.patientWindow.patientWindow;

import com.clinicapp.gui.visitDescirber.visitDescriberFrame;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

public class MainWindow {
    private JPanel panel1;
    private JButton docTableButton;
    private JLabel dateLabel;
    private JLabel dayVisitNumberLabel;
    private JButton docProfileButton;
    private JButton appointmentsTableButton;
    private JButton patientsButton;
    private JButton addSpecButton;
    private JLabel userTextLabel;
    private JPanel upJPanel;
    private JPanel BottomJPanel;
    private JLabel userLabel;
    private JList list1;
    private JList list5;
    private JList list4;
    private JList list3;
    private JList list2;
    private JComboBox docComboBox;
    private JButton describeButton;
    private JList visitsToDescribeJList;
    private JButton updateButton;
    private JPanel bottomJPanel;
    private UserStatus userStatus;
    private Doctor doc = null;
    private int dayNumberVisit = 0;
    private String login;
    private String password;
    private static ArrayList<Appointment> docsPastApp;
    private ArrayList<JList> myLists;
    private static ArrayList<Doctor> myDoctors;
    private Doctor loggedDoc = null;

    /**
     * Class constructor takes logged user status with login and passowrd. It creates MainWindowFrame adjusted for logged user.
     */
    public MainWindow(UserStatus userS, String login, String pass) {
        //save given data
        this.login = login;
        this.password = pass;
        this.userStatus = userS;

        //list of Jlists showing visits for next 5 days
        ArrayList<JList> myLists = new ArrayList<>();
        myLists.add(list1);
        myLists.add(list2);
        myLists.add(list3);
        myLists.add(list4);
        myLists.add(list5);
        this.myLists = myLists;

        //describe window for each doc
        setLabels();
        setUserJPanel();

        //creating frame, packing generated designer code
        JFrame frame = new JFrame("Moja Klinika");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel1);
        frame.pack();
        frame.setBounds(350, 200, 900, 700);
        ImageIcon icon = new ImageIcon("icons/esculapa.png"); //to chyba dla typical windows enjoyerow
        frame.setIconImage(icon.getImage());
        frame.setVisible(true);

        //connection closed when window closed
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    DatabaseConnector.closeConnection();
                } catch (SQLException s) {
                    s.printStackTrace();
                }
            }
        });

        //buttonListeners section
        docTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //try to get connection with database
                ArrayList<Doctor> docs = new ArrayList<>();
                try {
                    docs = GetDoctors.get();
                } catch (Exception rr) {
                    JOptionPane.showMessageDialog(null, "Brak połączenia z bazą", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                docTableButton.setEnabled(false);
                new docTableFrame(docTableButton, docs);
            }
        });

        docProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userStatus == UserStatus.Receptionist) {
                    JOptionPane.showMessageDialog(null, "Nie masz dostępu!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                docProfileButton.setEnabled(false);
                new docProfileFrame(docProfileButton, userStatus, loggedDoc);
            }
        });
        appointmentsTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String[]> app = new ArrayList<>();
                //try to get connection with database
                try {
                    app = GetAllAppointments.get();
                } catch (Exception rr) {
                    JOptionPane.showMessageDialog(null, "Brak połączenia z bazą", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                appointmentsTableButton.setEnabled(false);
                new appTableFrame(appointmentsTableButton, app);
            }
        });

        patientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                patientsButton.setEnabled(false);
                new patientWindow(patientsButton, userStatus);
            }
        });
        addSpecButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //check permission
                if (userStatus != UserStatus.Admin) {
                    JOptionPane.showMessageDialog(null, "Nie masz dostępu!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                addSpecButton.setEnabled(false);
                new addSpecFrame(addSpecButton);
            }
        });
        docComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int selectedDocIdx = docComboBox.getSelectedIndex() - 1;
                //if none selected or window just updated
                if (selectedDocIdx == -1 || selectedDocIdx == -2) {
                    return;
                }
                Doctor selectedDoc = myDoctors.get(selectedDocIdx);
                setDoctorsJPanel(selectedDoc);
            }
        });
        describeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userStatus == UserStatus.Receptionist) {
                    JOptionPane.showMessageDialog(null, "Nie masz uprawnień!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int selectedAppIdx = visitsToDescribeJList.getSelectedIndex();
                //if none selected
                if (selectedAppIdx == -1) {
                    JOptionPane.showMessageDialog(null, "Nie wybrałeś wizyty!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //get selected app
                Appointment selectedApp = docsPastApp.get(selectedAppIdx);
                new visitDescriberFrame(describeButton, selectedApp);
                describeButton.setEnabled(false);
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setUserJPanel();
            }
        });
    }

    /**
     * Checks logged user status
     */
    private void setUserJPanel() {
        if (this.userStatus == UserStatus.Doctor) {
            docComboBox.removeAllItems();
            docComboBox.addItem("Brak uprawnień do przeglądania wizyt");
            Doctor doc = GetDoctorByLogin.get(this.login, this.password);
            this.loggedDoc = doc;
            setDoctorsJPanel(doc);
            return;
        }
        if (this.userStatus == UserStatus.Receptionist) {
            userLabel.setText("Zalogowano jako recepcjonista");
        }
        if (this.userStatus == UserStatus.Admin) {
            userLabel.setText("Zalogowano jako administrator");
        }
        setComboBox();
    }

    /**
     * Method adds existing doctors from database to combobox. Called only if receptionist or admin logged.
     */
    private void setComboBox() {
        docComboBox.removeAllItems();
        docComboBox.addItem("Wybierz lekarza");
        ArrayList<Doctor> myDocs = GetDoctors.get();
        this.myDoctors = myDocs;
        for (int i = 0; i < myDocs.size(); i++) {
            Doctor currDoc = myDocs.get(i);
            String desc = String.valueOf(i + 1) + ". " + currDoc.getName() + " " + currDoc.getSurname();
            docComboBox.addItem(desc);
        }

    }

    /**
     * Adjusts main window to selected doc
     *
     * @param doc selected doctor
     */
    private void setDoctorsJPanel(Doctor doc) {
        setUpperLists(doc);
        setBottomList(doc);

    }

    /**
     * Shows visits for next 5 days for selected doc
     *
     * @param doc selected doctor
     */
    private void setUpperLists(Doctor doc) {
        userLabel.setText("Terminarz lekarza: " + doc.getName() + " " + doc.getSurname());
        //try to get next appointments from database
        ArrayList<ArrayList<Appointment>> weekApp = new ArrayList<>();
        try {
            weekApp = GetFiveDays.get(doc.getId());
        } catch (Exception hf) {
            JOptionPane.showMessageDialog(null, "Brak połączenia!", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ArrayList<String> dates = get5nextDates();
        //prepare each list
        for (int i = 0; i < 5; i++) {
            JList currList = myLists.get(i);
            ArrayList<Appointment> currAppList = weekApp.get(i);
            ArrayList<String> descList = new ArrayList<>();
            descList.add(dates.get(i));
            //preapare description of each visit for each day
            for (int k = 0; k < currAppList.size(); k++) {
                Appointment currApp = currAppList.get(k);
                String desc = currApp.getTime().toString() + " ";
                //if apprintment is free
                if (currApp.getPatientPesel() == null) {
                    desc += "Wolny termin";
                } else {
                    String pesel = currApp.getPatientPesel();
                    Patient pat = GetPatientByPesel.get(pesel).get(0);
                    desc += pat.getName() + " " + pat.getSurname();
                }
                descList.add(desc);
            }
            //set JList
            currList.setListData(descList.toArray());
        }
    }

    /**
     * sets list of past visits to describe
     *
     * @param doc doctor that
     */
    private void setBottomList(Doctor doc) {
        ArrayList<String> pastAppDescList = new ArrayList<>();

        ArrayList<Appointment> pastApp = new ArrayList<>();
        try {
            pastApp = GetPastAppointmentsByDocId.get(doc.getId());
        } catch (Exception fff) {
            JOptionPane.showMessageDialog(null, "Brak połączenia z bazą", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.docsPastApp = pastApp;
        //set description of each appointment to describe
        for (int l = 0; l < pastApp.size(); l++) {
            Appointment currApp = pastApp.get(l);
            String desc = currApp.getDay().toString() + " " + currApp.getTime().toString() + " ";
            Patient pat = GetPatientByPesel.get(currApp.getPatientPesel()).get(0);
            desc += pat.getName() + " " + pat.getSurname();
            pastAppDescList.add(desc);
        }
        //set List
        visitsToDescribeJList.setListData(pastAppDescList.toArray());
    }

    /**
     * Returns ArrayList<String> of 5 next dates in string
     */
    private ArrayList<String> get5nextDates() {
        //get current date
        LocalDate localDate = LocalDate.now();
        ArrayList<String> myDates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        //add to list incremented dates
        for (int i = 0; i < 5; i++) {
            String formattedString = localDate.format(formatter);
            myDates.add(formattedString);
            localDate = localDate.plusDays(1);
        }
        return myDates;
    }

    /**
     * Sets date and today visits labels.
     */
    void setLabels() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateDesc = "Data: " + date.format(formatter);
        this.dateLabel.setText(dateDesc);
        int todayVisitNumber = GetTakenAppointmentsToday.get().size();
        this.dayVisitNumberLabel.setText("Dziś wizyt: " + String.valueOf(todayVisitNumber));
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
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(5, 5));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(-13750738)));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, BorderLayout.WEST);
        docTableButton = new JButton();
        docTableButton.setFocusPainted(false);
        docTableButton.setFocusable(false);
        docTableButton.setText("Pokaż lekarzy");
        panel2.add(docTableButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        docProfileButton = new JButton();
        docProfileButton.setFocusable(false);
        docProfileButton.setText("Lekarze");
        panel2.add(docProfileButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        appointmentsTableButton = new JButton();
        appointmentsTableButton.setFocusable(false);
        appointmentsTableButton.setText("Pokaż terminy");
        panel2.add(appointmentsTableButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        patientsButton = new JButton();
        patientsButton.setFocusable(false);
        patientsButton.setText("Pacjenci");
        panel2.add(patientsButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addSpecButton = new JButton();
        addSpecButton.setFocusable(false);
        addSpecButton.setText("Dodaj specjalizację");
        panel2.add(addSpecButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), 5, 5));
        panel3.setMinimumSize(new Dimension(33, 40));
        panel1.add(panel3, BorderLayout.NORTH);
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.BOTTOM, null, null));
        dateLabel = new JLabel();
        Font dateLabelFont = this.$$$getFont$$$(null, Font.BOLD, 20, dateLabel.getFont());
        if (dateLabelFont != null) dateLabel.setFont(dateLabelFont);
        dateLabel.setHorizontalTextPosition(0);
        dateLabel.setText("Label");
        panel3.add(dateLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dayVisitNumberLabel = new JLabel();
        Font dayVisitNumberLabelFont = this.$$$getFont$$$(null, Font.BOLD, 20, dayVisitNumberLabel.getFont());
        if (dayVisitNumberLabelFont != null) dayVisitNumberLabel.setFont(dayVisitNumberLabelFont);
        dayVisitNumberLabel.setHorizontalTextPosition(0);
        dayVisitNumberLabel.setText("Label");
        panel3.add(dayVisitNumberLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, BorderLayout.CENTER);
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        upJPanel = new JPanel();
        upJPanel.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(upJPanel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        upJPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        list1 = new JList();
        scrollPane1.setViewportView(list1);
        final JScrollPane scrollPane2 = new JScrollPane();
        upJPanel.add(scrollPane2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        list2 = new JList();
        scrollPane2.setViewportView(list2);
        final JScrollPane scrollPane3 = new JScrollPane();
        upJPanel.add(scrollPane3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        list3 = new JList();
        scrollPane3.setViewportView(list3);
        final JScrollPane scrollPane4 = new JScrollPane();
        upJPanel.add(scrollPane4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        list4 = new JList();
        scrollPane4.setViewportView(list4);
        final JScrollPane scrollPane5 = new JScrollPane();
        upJPanel.add(scrollPane5, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        list5 = new JList();
        scrollPane5.setViewportView(list5);
        BottomJPanel = new JPanel();
        BottomJPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(BottomJPanel, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        BottomJPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        describeButton = new JButton();
        describeButton.setText("Opisz");
        BottomJPanel.add(describeButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        visitsToDescribeJList = new JList();
        BottomJPanel.add(visitsToDescribeJList, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        userLabel = new JLabel();
        Font userLabelFont = this.$$$getFont$$$(null, Font.BOLD, 20, userLabel.getFont());
        if (userLabelFont != null) userLabel.setFont(userLabelFont);
        userLabel.setText("Label");
        panel4.add(userLabel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        docComboBox = new JComboBox();
        panel4.add(docComboBox, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        updateButton = new JButton();
        updateButton.setHorizontalAlignment(0);
        updateButton.setText("Aktualizuj");
        panel4.add(updateButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(115, -1), 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
