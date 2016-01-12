package de.tum.mw.ftm.praktikum.smartinsightphd;

/**
 * Created by Rebecca on 13.12.2015.
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
