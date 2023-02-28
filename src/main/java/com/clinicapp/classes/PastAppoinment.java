package com.clinicapp.classes;

public class PastAppoinment extends AppTemplate{
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PastAppoinment(String day, String time, String doctorId, String dateId, String description) throws Exception {
        super(day, time, doctorId, dateId);
//        checkFormat(day, time);
        this.description = description;
    }
}
