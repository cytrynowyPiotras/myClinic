package com.clinicapp.classes;

public class Doctor extends Person{

    private String id;
    private String specializationId;
    private String userId;


    public String getId() {return this.id;}
    public String getUserId() {return userId;}
    public String getSpecializationId() {return specializationId;}

    public void setId(String id) {
        this.id = id;
    }


    public Doctor(String name, String surname, String phoneNumber, String id, String userId, String specializationId)  {
        super(name, surname, phoneNumber);
        //if (docId.length() != 4) {throw new Exception("incorrect doctors Id");}
        if (id==null) {this.id = "";}
        else {this.id = id;}
        this.userId = userId;
        this.specializationId = specializationId;
    }
}