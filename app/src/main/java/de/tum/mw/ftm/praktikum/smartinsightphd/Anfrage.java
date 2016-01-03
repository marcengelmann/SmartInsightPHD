package de.tum.mw.ftm.praktikum.smartinsightphd;

/**
 * Created by Rebecca on 13.12.2015.
 */
public class Anfrage {
    public String linked_student, linked_task, linked_subtask, linked_phd,id;
    public String endTime;
    public String startTime;
    public String editRequest;
    public Anfrage(String id,String student, String task, String subtask, String phd, String startTime, String endTime, String editRequest) {
        this.linked_student = student;
        this.linked_task = task;
        this.id = id;
        this.linked_subtask = subtask;
        this.linked_phd = phd;
        this.endTime = endTime;
        this.startTime = startTime;
        this.editRequest = editRequest;
    }

    @Override
    public String toString() {
        return "ANFRAGE: ID: "+id+" Student:" + linked_student+" Task:" + linked_task+" Subtask:"+ linked_subtask+" Doktorand:"+ linked_phd+" StartZeit:"+ startTime+" EndZeit:"+ endTime+" Bearbeitung:"+ editRequest;
    }
}

