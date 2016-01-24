package de.tum.mw.ftm.praktikum.smartinsightphd;

import java.io.Serializable;

/**
 * Klasse die die Anfragen von den Studenten speichert mit Zeit, Name, Sitzplatz nummer,
 * Klausur und ob die Anfrage vom PHD schon bearbeitet wrude
 */
public class RequestsStudent  implements Serializable {
    private String endTime;
    private String startTime;
    private String student;
    private String question;
    private String taskNumber;
    private String sitzNumber;
    private String taskSubNumber;
    private String id;
    private String exam;
    private String requestFinished;

    public RequestsStudent(String id, String startTime, String endTime, String taskNumber, String taskSubNumber, String question, String student, String sitzNumber, String exam, String requestFinished) {
        this.endTime = endTime;
        this.startTime = startTime;
        this.student = student;
        this.question = question;
        this.taskNumber = taskNumber;
        this.taskSubNumber = taskSubNumber;
        this.id = id;
        this.sitzNumber = sitzNumber;
        this.exam = exam;
        this.requestFinished = requestFinished;
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

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
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

    public String getSitzNumber() {
        return sitzNumber;
    }

    public void setSitzNumber(String sitzNumber) {
        this.sitzNumber = sitzNumber;
    }

    public String getTaskSubNumber() {
        return taskSubNumber;
    }

    public void setTaskSubNumber(String taskSubNumber) {
        this.taskSubNumber = taskSubNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getRequestFinished() {
        return requestFinished;
    }

    public void setRequestFinished(String requestFinished) {
        this.requestFinished = requestFinished;
    }
}
