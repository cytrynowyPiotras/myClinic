package com.clinicapp.gui.doctoradder;
import com.clinicapp.classes.DataChecker;
import com.clinicapp.classes.Patient;
import com.clinicapp.classes.Specialization;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;

import com.clinicapp.classes.Doctor;
import com.clinicapp.connector.dbAppointment.AddAppointmentsToDoc;
import com.clinicapp.connector.dbDoctor.AddDoctor;
import com.clinicapp.connector.dbSpecialization.GetAllSpec;
import com.clinicapp.gui.docProfile.docProfileFrame;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

public class DoctorAdderFrame {
    private JPanel panel1;
    private JButton addButton;
    private JLabel lab1;
    private JLabel lab2;
    private JLabel lab3;
    private JTextField textFieldName;
    private JTextField textFieldSurname;
    private JTextField textFieldPhone;
    private JPanel specJPanel;
    private JSpinner durationSpinner;
    private JComboBox comboBoxPonStart;
    private JComboBox comboBoxPonEnd;
    private JComboBox comboBoxWtStart;
    private JComboBox comboBoxWtEnd;
    private JComboBox comboBoxSrStart;
    private JComboBox comboBoxSrEnd;
    private JComboBox comboBoxCzwStart;
    private JComboBox comboBoxCzwEnd;
    private JComboBox comboBoxPtStart;
    private JComboBox comboBoxPtEnd;
    private JTextField loginTextField;
    private JTextField passTextField;
    private JComboBox myComboxes[];
    private ArrayList<JCheckBox> myCheckBoxes = new ArrayList<>();
    private ArrayList<Specialization> mySpec = new ArrayList<>();
    static final long ONE_MINUTE_IN_MILLIS = 60000;
    static final int VisitDuration = 20;

    static docProfileFrame parentFrame;

    private void addMedSpecCheckBoxes() {
        specJPanel.setLayout(new GridLayout(0, 1));
        ArrayList<Specialization> mySpec;
        try {
            mySpec = GetAllSpec.specializations();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.mySpec = mySpec;
        for (int i = 0; i < mySpec.size(); i++) {
            Specialization spec = mySpec.get(i);
            String specName = spec.getName();
            JCheckBox myCB = new JCheckBox(specName);
            this.myCheckBoxes.add(myCB);
            specJPanel.add(myCB);
        }
    }

    public DoctorAdderFrame(JButton creator, docProfileFrame parentFrame) {
        setComboBoxes();
        this.parentFrame = parentFrame;
        addMedSpecCheckBoxes();

        SpinnerNumberModel m_numberSpinnerModel;
        int current = 0;
        int min = 0;
        int max = 365;
        int step = 1;
        m_numberSpinnerModel = new SpinnerNumberModel(current, min, max, step);
        durationSpinner.setModel(m_numberSpinnerModel);

        JFrame frame = new JFrame("Dodaj Lekarza");
        frame.setContentPane(panel1);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(300, 300, 680, 450);
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


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = textFieldName.getText();
                String surname = textFieldSurname.getText();
                String phone = textFieldPhone.getText();
                String login = loginTextField.getText();
                String pass = passTextField.getText();
                boolean correctNameSurPhone = DataChecker.checkNameSurnamePhone(name, surname, phone, login, pass);
                if (!correctNameSurPhone) {
                    JOptionPane.showMessageDialog(null, "Błędne dane doktora", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean correctCheckBoxes = checkCheckBoxes();
                if (!correctCheckBoxes) {
                    JOptionPane.showMessageDialog(null, "Zaznacz jedną specjalizację!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String specId = getSelectedSpecId();
                int spinnerSelectedValue = (Integer) durationSpinner.getValue();
                if (spinnerSelectedValue == 0) {
                    Doctor doc = new Doctor(name, surname, phone, "", "1", specId);
                    try {
                        AddDoctor.add(doc, login, pass);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    JOptionPane.showMessageDialog(null, "Dodano doktora bez terminów", "Sukces!", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    creator.setEnabled(true);
                    parentFrame.setComboBox();
                } else { //if doc data correct and there are visits to add
                    boolean correctHours = checkGivenHours();
                    if (!correctHours) {
                        JOptionPane.showMessageDialog(null, "Błędne godziny przyjęć!", "Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    ArrayList<String> startEndHours = getDocsHours();
                    Doctor doc = new Doctor(name, surname, phone, "", "1", specId);
                    try {
                        AddDoctor.add(doc, login, pass);
                        // możliwości ogarniecia tego - po dodaniu lekarza jakis select max userid albo select max id_lekarza
                    } catch (Exception ex1) {
                        JOptionPane.showMessageDialog(null, "Błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        AddAppointmentsToDoc.add(doc, startEndHours, VisitDuration, spinnerSelectedValue);
                    } catch (Exception ex2) {
                        JOptionPane.showMessageDialog(null, "Błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    JOptionPane.showMessageDialog(null, "Dodano doktora z terminami", "Sukces!", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    creator.setEnabled(true);
                    parentFrame.setComboBox();
                }
            }
        });
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
            if (Objects.equals(spec.getName(), specName)) {
                return spec.getSpecializationId();
            }
        }
        return "i tak znalazlem";
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

    private ArrayList<String> getDocsHours() {
        ArrayList<String> myHours = new ArrayList<String>();
        for (int i = 0; i < 10; i += 2) {
            JComboBox startingComboBox = this.myComboxes[i];
            JComboBox endingComboBox = this.myComboxes[i + 1];
            String startingVal = startingComboBox.getSelectedItem().toString();
            String endingVal = endingComboBox.getSelectedItem().toString();
            if (Objects.equals(startingVal, "Nie przyjmuje") || Objects.equals(endingVal, "Nie przyjmuje")) {
                myHours.add("");
                myHours.add("");
                continue;
            }
            myHours.add(startingVal);
            myHours.add(endingVal);
        }
        return myHours;
    }

    private boolean checkGivenHours() {
        for (int i = 0; i < 10; i += 2) {
            JComboBox startingComboBox = this.myComboxes[i];
            JComboBox endingComboBox = this.myComboxes[i + 1];
            String startingVal = startingComboBox.getSelectedItem().toString();
            String endingVal = endingComboBox.getSelectedItem().toString();
            if (Objects.equals(startingVal, "Nie przyjmuje")) {
                continue;
            }
            if (Objects.equals(endingVal, "Nie przyjmuje")) {
                continue;
            }
            // next cases only if values selected, values are in order
            int firstIdx = startingComboBox.getSelectedIndex();
            int secondIdx = endingComboBox.getSelectedIndex();
            if (secondIdx <= firstIdx) {
                return false;
            }
        }
        return true;

    }

    private void setComboBoxes() {
        int startingHour = 6;
        int endingHour = 22;
        int duration = 15;
        ArrayList<String> hours = null;
        hours = getHoursList(startingHour, endingHour, duration);
        JComboBox myComboxes[] = {
                comboBoxPonStart,
                comboBoxPonEnd,
                comboBoxWtStart,
                comboBoxWtEnd,
                comboBoxSrStart,
                comboBoxSrEnd,
                comboBoxCzwStart,
                comboBoxCzwEnd,
                comboBoxPtStart,
                comboBoxPtEnd
        };
        this.myComboxes = myComboxes;

        for (int i = 0; i < myComboxes.length; i++) {
            JComboBox currJComB = myComboxes[i];
            currJComB.addItem("Nie przyjmuje");
            for (int k = 0; k < hours.size(); k++) {
                currJComB.addItem(hours.get(k));
            }
        }
    }


    public static Date addMinutesToDate(int minutes, Date beforeTime) {
        long curTimeInMs = beforeTime.getTime();
        Date afterAddingMins = new Date(curTimeInMs
                + (minutes * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }

    public static String getHoursMinutes(Date myDate) {
        String hourDesc = "";
        hourDesc += String.valueOf(myDate.getHours());
        hourDesc += ":";
        hourDesc += String.format("%02d", myDate.getMinutes());
        return hourDesc;
    }

    public static ArrayList<String> getHoursList(int minHour, int maxHour, int duration) {
        if (minHour <= maxHour) {
            new Exception("Invalid hours given");
        }
        Date currTime = new Date(1970, 1, 1, minHour, 0);
        Date maxDate = new Date(1970, 1, 1, maxHour, 0);
        ArrayList<String> myHours = new ArrayList<String>();
        myHours.add(getHoursMinutes(currTime));
        while (currTime.getHours() < maxDate.getHours()) {
            myHours.add(getHoursMinutes(currTime));
            currTime = addMinutesToDate(duration, currTime);
        }
        return myHours;
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
        panel1.setLayout(new GridLayoutManager(9, 2, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lab1 = new JLabel();
        lab1.setText("Podaj imię:");
        panel3.add(lab1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(135, -1), null, new Dimension(135, -1), 0, false));
        textFieldName = new JTextField();
        panel3.add(textFieldName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lab2 = new JLabel();
        lab2.setText("Podaj nazwisko: ");
        panel4.add(lab2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(135, -1), null, new Dimension(135, -1), 0, false));
        textFieldSurname = new JTextField();
        panel4.add(textFieldSurname, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lab3 = new JLabel();
        lab3.setText("Podaj numer telefonu: ");
        panel5.add(lab3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(135, -1), null, new Dimension(135, -1), 0, false));
        textFieldPhone = new JTextField();
        panel5.add(textFieldPhone, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel6, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText(" Login:");
        panel6.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loginTextField = new JTextField();
        panel6.add(loginTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText(" Hasło:");
        panel6.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passTextField = new JTextField();
        panel6.add(passTextField, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        specJPanel = new JPanel();
        specJPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(specJPanel, new GridConstraints(4, 1, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        specJPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        addButton = new JButton();
        addButton.setText("Dodaj");
        panel1.add(addButton, new GridConstraints(7, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel7, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel8, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(0);
        label3.setText(" Środa:");
        panel8.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), null, null, 0, false));
        comboBoxSrStart = new JComboBox();
        panel8.add(comboBoxSrStart, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxSrEnd = new JComboBox();
        panel8.add(comboBoxSrEnd, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel9, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setHorizontalAlignment(0);
        label4.setText(" Czwartek:");
        panel9.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), null, null, 0, false));
        comboBoxCzwStart = new JComboBox();
        panel9.add(comboBoxCzwStart, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxCzwEnd = new JComboBox();
        panel9.add(comboBoxCzwEnd, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setHorizontalAlignment(0);
        label5.setText(" Wtorek:");
        panel10.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), null, null, 0, false));
        comboBoxWtStart = new JComboBox();
        panel10.add(comboBoxWtStart, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxWtEnd = new JComboBox();
        panel10.add(comboBoxWtEnd, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setHorizontalAlignment(0);
        label6.setText(" Poniedziałek:");
        panel11.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), null, null, 0, false));
        comboBoxPonStart = new JComboBox();
        panel11.add(comboBoxPonStart, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxPonEnd = new JComboBox();
        panel11.add(comboBoxPonEnd, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel12, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setHorizontalAlignment(0);
        label7.setText(" Piątek:");
        panel12.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), null, null, 0, false));
        comboBoxPtStart = new JComboBox();
        panel12.add(comboBoxPtStart, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxPtEnd = new JComboBox();
        panel12.add(comboBoxPtEnd, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel13, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel13.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label8 = new JLabel();
        label8.setText(" Dany terminarz obowiązuje dni: ");
        panel13.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        durationSpinner = new JSpinner();
        panel13.add(durationSpinner, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setHorizontalAlignment(0);
        label9.setText("Ustaw godziny rozpoczęcia i zakończenia pracy dla każdego dnia. ");
        panel1.add(label9, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Jeśli na ten moment nie chcesz ustawiać grafiku, pozostaw ilość dni na 0.");
        panel1.add(label10, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Jeśli danego dnia nie pracujesz, zaznacz odpowiednią opcję. ");
        panel1.add(label11, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}


