package de.tum.mw.ftm.praktikum.smartinsightphd;

/**
 * Speichern der daten vom PHD
 */
public class User {
    String name, email, password,exam,id,deviceID;
    boolean didChange;

    public User (String email, String name, String password,String exam,String id,String deviceID,boolean didChange){
        this.email = email;
        this.name = name;
        this.password = password;
        this.exam = exam;
        this.id = id;
        this.deviceID = deviceID;
        this.didChange = didChange;
    }
}
