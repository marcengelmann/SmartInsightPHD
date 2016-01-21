package de.tum.mw.ftm.praktikum.smartinsightphd;

/**
 * Speichern der daten vom PHD
 */
public class User {
    private String name, email, password,exam,id,deviceID;
    private boolean didChange;

    public User (String email, String name, String password,String exam,String id,String deviceID,boolean didChange){
        this.email = email;
        this.name = name;
        this.password = password;
        this.exam = exam;
        this.id = id;
        this.deviceID = deviceID;
        this.didChange = didChange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public boolean getDidChange() {
        return didChange;
    }

    public void setDidChange(boolean didChange) {
        this.didChange = didChange;
    }
}
