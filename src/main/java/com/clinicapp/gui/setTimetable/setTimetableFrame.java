package com.clinicapp.gui.setTimetable;

import com.clinicapp.classes.Doctor;
import com.clinicapp.connector.dbAppointment.AddAppointmentsToDoc;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public class setTimetableFrame {
    private JComboBox monStCB;
    private JComboBox tuStCB;
    private JComboBox tuEnCB;
    private JComboBox weStCB;
    private JComboBox weEnCB;
    private JComboBox thStCB;
    private JComboBox thEnCB;
    private JComboBox frStCB;
    private JComboBox frEnCB;
    private JLabel peselInfloLabel;
    private JLabel dateInfoLabel;
    private JSpinner spinner1;
    private JButton setButton;
    private JPanel setTimeTableJPanel;
    private JComboBox monEnCB;
    private JComboBox myComboxes[];
    public static int visitDuration = 20;

    public setTimetableFrame(JButton creatorButton, Doctor doc) {
        peselInfloLabel.setText("Ustawiasz terminarz lekarzowi: " + doc.getName() + " " + doc.getSurname());
        SpinnerNumberModel m_numberSpinnerModel;
        int current = 0;
        int min = 0;
        int max = 365;
        int step = 1;
        m_numberSpinnerModel = new SpinnerNumberModel(current, min, max, step);
        spinner1.setModel(m_numberSpinnerModel);
        setComboxes();
        JFrame frame = new JFrame("Pacjenci");
        frame.setContentPane(setTimeTableJPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(150, 150, 600, 300);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                creatorButton.setEnabled(true);
                frame.dispose();
            }
        });
        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int value = (Integer) spinner1.getValue();
                if (value == 0) {
                    JOptionPane.showMessageDialog(null, "Zerowa ilość dni!", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean correctHours = checkGivenHours();
                if (correctHours == false) {
                    JOptionPane.showMessageDialog(null, "Złe godziny!", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ArrayList<String> docsHours = getDocsHours();
                try {
                    AddAppointmentsToDoc.add(doc, docsHours, visitDuration, value);
                } catch (Exception ex) {  JOptionPane.showMessageDialog(null, "Błąd bazy danych", "Error!", JOptionPane.ERROR_MESSAGE); return; }
                JOptionPane.showMessageDialog(null, "Dodano terminarz!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                creatorButton.setEnabled(true);
            }
        });

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
            if (startingVal == "Nie przyjmuje") {
                continue;
            }
            if (endingVal == "Nie przyjmuje") {
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

    private void setComboxes() {
        int startingHour = 6;
        int endingHour = 22;
        int duration = 15;
        ArrayList<String> hours = null;
        try {
            hours = getHoursList(startingHour, endingHour, duration);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JComboBox myComboxes[] = {
                monStCB,
                monEnCB,
                tuStCB,
                tuEnCB,
                weStCB,
                weEnCB,
                thStCB,
                thEnCB,
                frStCB,
                frEnCB
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

    public static ArrayList<String> getHoursList(int minHour, int maxHour, int duration) throws Exception {
        if (minHour >= maxHour) {
            System.out.println(minHour);
            System.out.println(maxHour);
            throw new Exception("Min hour cannot after max hour");
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

    static final long ONE_MINUTE_IN_MILLIS = 60000;

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
        setTimeTableJPanel = new JPanel();
        setTimeTableJPanel.setLayout(new GridLayoutManager(7, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        setTimeTableJPanel.add(panel1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(0);
        label1.setText(" Poniedziałek:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), null, null, 0, false));
        monEnCB = new JComboBox();
        panel2.add(monEnCB, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        monStCB = new JComboBox();
        panel2.add(monStCB, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(0);
        label2.setText("Wtorek:");
        panel3.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), null, null, 0, false));
        tuStCB = new JComboBox();
        panel3.add(tuStCB, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tuEnCB = new JComboBox();
        panel3.add(tuEnCB, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(0);
        label3.setText("Środa:");
        panel4.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), null, null, 0, false));
        weStCB = new JComboBox();
        panel4.add(weStCB, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        weEnCB = new JComboBox();
        panel4.add(weEnCB, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setHorizontalAlignment(0);
        label4.setText("Czwartek:");
        panel5.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), null, null, 0, false));
        thStCB = new JComboBox();
        panel5.add(thStCB, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        thEnCB = new JComboBox();
        panel5.add(thEnCB, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel6, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setHorizontalAlignment(0);
        label5.setText("Piątek:");
        panel6.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), null, null, 0, false));
        frStCB = new JComboBox();
        panel6.add(frStCB, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        frEnCB = new JComboBox();
        panel6.add(frEnCB, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dateInfoLabel = new JLabel();
        dateInfoLabel.setText("Label");
        setTimeTableJPanel.add(dateInfoLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        peselInfloLabel = new JLabel();
        peselInfloLabel.setText("Label");
        setTimeTableJPanel.add(peselInfloLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        setTimeTableJPanel.add(spacer1, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        setTimeTableJPanel.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        setTimeTableJPanel.add(panel7, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText(" Terminarz obowiązuje przez dni:");
        panel7.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinner1 = new JSpinner();
        panel7.add(spinner1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        setButton = new JButton();
        setButton.setText("Ustaw");
        setTimeTableJPanel.add(setButton, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return setTimeTableJPanel;
    }

}
