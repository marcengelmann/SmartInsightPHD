package de.tum.mw.ftm.praktikum.smartinsightphd;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rebecca on 13.12.2015.
 */
public class AnfrageLocalStore {

    public  static final String SP_NAME = "anfrageDetails";
    SharedPreferences anfrageLocalDatabase;

    public AnfrageLocalStore(Context context){
        anfrageLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }
    //public Anfrage(String id,String student, String task, String subtask, String phd, String startTime, String endTime, String editRequest) {

    public void storeAnfrageData(Anfrage anfrage){
        SharedPreferences.Editor spEditor = anfrageLocalDatabase.edit();
        spEditor.putString("linked_student", anfrage.linked_student);
        spEditor.putString("linked_task",anfrage.linked_task);
        spEditor.putString("linked_subtask",anfrage.linked_subtask);
        spEditor.putString("linked_phd",anfrage.linked_phd);
        spEditor.putString("id",anfrage.id);
        spEditor.putString("startTime", anfrage.startTime);
        spEditor.putString("endTime", anfrage.endTime);
        spEditor.putString("editRequest", anfrage.editRequest);
        spEditor.putString("art_of_question",anfrage.art_of_question);
        spEditor.putString("exam",anfrage.exam);

        spEditor.commit();
    }

    public Anfrage getDataAnfrageClient(){
        String linked_student = anfrageLocalDatabase.getString("linked_student", "");
        String linked_task = anfrageLocalDatabase.getString("linked_task","");
        String linked_subtask = anfrageLocalDatabase.getString("linked_subtask", "");
        String linked_phd = anfrageLocalDatabase.getString("linked_phd", "");
        String id = anfrageLocalDatabase.getString("id", "");
        String startTime = anfrageLocalDatabase.getString("startTime","");
        String endTime = anfrageLocalDatabase.getString("endTime", "");
        String editRequest = anfrageLocalDatabase.getString("editRequest", "");
        String art_of_question = anfrageLocalDatabase.getString("art_of_question","");
        String exam = anfrageLocalDatabase.getString("exam","");

        Anfrage storedAnfrage = new Anfrage(id,linked_student, linked_task, linked_subtask , linked_phd, startTime, endTime, editRequest,art_of_question,exam);

        return storedAnfrage;
    }

    public void setStatusAnfrageClient(boolean setAnfrage){
        SharedPreferences.Editor spEditor = anfrageLocalDatabase.edit();
        spEditor.putBoolean("setAnfrage", setAnfrage);
        spEditor.commit();
    }

    public boolean getStatusAnfrageClient(){
        if(anfrageLocalDatabase.getBoolean("setAnfrage", false) == true){
            return true;
        }
        return false;
    }

    public void clearDataAnfrageClient(){
        SharedPreferences.Editor spEditor = anfrageLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
