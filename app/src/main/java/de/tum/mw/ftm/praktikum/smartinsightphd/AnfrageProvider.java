package de.tum.mw.ftm.praktikum.smartinsightphd;

import java.io.Serializable;

/**
 * Created by Rebecca on 13.12.2015.
 */
public class AnfrageProvider implements Serializable{
    public String endTime;
    public String startTime;
    public String editor;
    public String question;
    public String taskNumber;
    public String sitzNumber;
    public String taskSubNumber;
    public String id;


    public AnfrageProvider(String id, String startTime, String endTime, String taskNumber, String taskSubNumber, String question, String editor, String sitzNumber) {
        this.endTime = endTime;
        this.startTime = startTime;
        this.editor = editor;
        this.question = question;
        this.taskNumber = taskNumber;
        this.taskSubNumber = taskSubNumber;
        this.id = id;
        this.sitzNumber = sitzNumber;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getTaskSubNumber() {
        return taskSubNumber;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public void setTaskSubNumber(String taskSubNumber) {
        this.taskSubNumber = taskSubNumber;
    }

}
