package com.clinicapp.connector.dbAppointment;

import com.clinicapp.classes.Appointment;
import com.clinicapp.classes.Doctor;
import com.clinicapp.connector.DatabaseConnector;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class AddAppointmentsToDoc extends DatabaseConnector {
        private static String addZeroToTime(String time) {
            String newTime = time;
            if (newTime.length() == 4) {
                newTime = "0" + newTime;
            }
            return newTime;
        }

        private static void addByDay(Doctor doc, ArrayList<String> startEndHours, LocalDate currDate, int visitDuration, int startIdx, int endIdx) {
            String startTimePreParsed = startEndHours.get(startIdx);
            String endTimePreParsed = startEndHours.get(endIdx);
            if (startTimePreParsed.isEmpty() || endTimePreParsed.isEmpty()) { return; }
            String startTimeStr = addZeroToTime(startTimePreParsed);
            String endTimeStr = addZeroToTime(endTimePreParsed);
            LocalTime startTime = LocalTime.parse(startTimeStr);
            LocalTime endTime = LocalTime.parse(endTimeStr);
            // old way of getting docId
//            String docId = getMaxDoctorId();
//            if (docId.isEmpty()) {
//                throw new Exception("No doctor found");
//            }
            System.out.println(doc.getId());
            if (doc.getId().isEmpty() || doc.getId() == null) {
                String newDocId = getDocId(doc);
                doc.setId(newDocId);
            }
            if (checkIfHasAppointment(doc, currDate)) { return; }
            while (startTime.isBefore(endTime)) {
                try {
                    Appointment app = new Appointment(currDate.toString(), startTime.toString(), doc.getId(), "");
                    String sql = "INSERT INTO terminy (id_terminu, data_godzina, id_lekarza) VALUES (term_id.nextval, '"+app.getDay().toString()+" "+app.getTime().toString()+"', " +app.getDoctorId()+")";
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                } catch (Exception e) { e.printStackTrace(); }
//                System.out.print(currDate.toString() + ' ');
//                System.out.println(startTime);
                startTime = startTime.plusMinutes(visitDuration);
            }
        }

        private static boolean checkIfHasAppointment(Doctor doc, LocalDate currDate){
            boolean hasAppointment = false;
            String count = "0";
            String tempStartDate = currDate.toString() + " 00:00";
            String tempEndDate = currDate.toString() + " 23:59";
            try {
                String sql = "SELECT count(id_terminu) as c FROM terminy WHERE id_lekarza = " + doc.getId() + "AND data_godzina > '" + tempStartDate + "' AND data_godzina < '" + tempEndDate+"'";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    count = rs.getString("c");
                }
                rs.close();
                preparedStatement.close();
            } catch (Exception e) { e.printStackTrace(); }
            if (Integer.parseInt(count) > 0) {
                hasAppointment = true;
            }
            return hasAppointment;
        }
        private static String getMaxDoctorId() {
            String docId = "";
            try {

                String sql = "select max(id_lekarza) as m from lekarze";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    docId = rs.getString("m");
                }
                rs.close();
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return docId;
        }

        private static String getDocId(Doctor doc) {
            String docId = "";
            try {
                String sql = "select id_lekarza from lekarze where imie like '" + doc.getName() + "' AND nazwisko like '" + doc.getSurname() + "' and nr_telefonu like '" + doc.getPhoneNumber() + "' and id_specjalizacji like " + doc.getSpecializationId() + " ORDER BY id_lekarza DESC FETCH FIRST ROW ONLY";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    docId = rs.getString("id_lekarza");
                }
                preparedStatement.close();
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return docId;
        }

        public static void add(Doctor doc, ArrayList<String> startEndHours, int visitDuration, int duration) {
            // automatically add new free appointments based on given data
        LocalDate currDate = LocalDate.now().plusDays(1);
        try {
            String setter = "alter session set nls_date_format='YYYY-MM-DD HH24:MI'";
            PreparedStatement preparedStatement1 = conn.prepareStatement(setter);
            preparedStatement1.executeUpdate();
            for (int i = 0; i < duration; i++) {
                try {
                    switch (currDate.getDayOfWeek()) {
                        case SATURDAY:
                        case SUNDAY:
                            break;
                        case MONDAY:
                            addByDay(doc, startEndHours, currDate, visitDuration, 0, 1);
                            break;
                        case TUESDAY:
                            addByDay(doc, startEndHours, currDate, visitDuration, 2, 3);
                            break;
                        case WEDNESDAY:
                            addByDay(doc, startEndHours, currDate, visitDuration, 4, 5);
                            break;
                        case THURSDAY:
                            addByDay(doc, startEndHours, currDate, visitDuration, 6, 7);
                            break;
                        case FRIDAY:
                            addByDay(doc, startEndHours, currDate, visitDuration, 8, 9);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                currDate = currDate.plusDays(1);
            }
            preparedStatement1.close();
        } catch (Exception e) { e.printStackTrace(); }

    }

}
