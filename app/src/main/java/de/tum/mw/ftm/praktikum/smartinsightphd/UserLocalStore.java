package de.tum.mw.ftm.praktikum.smartinsightphd;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * KLasse die den Aktuellen nutzer lokal speichert
 */
public class UserLocalStore {

    private  static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("name", user.getName());
        spEditor.putString("email", user.getEmail());
        spEditor.putString("password",user.getPassword());
        spEditor.putString("exam",user.getExam());
        spEditor.putString("id",user.getId());
        spEditor.putString("deviceID",user.getDeviceID());
        spEditor.putBoolean("didChange",user.getDidChange());
        spEditor.apply();
    }

    public User getUserLogInUser(){
        String name = userLocalDatabase.getString("name", "");
        String email = userLocalDatabase.getString("email","");
        String exam = userLocalDatabase.getString("exam", "");
        String password = userLocalDatabase.getString("password","");
        String id = userLocalDatabase.getString("id","");
        String deviceID = userLocalDatabase.getString("deviceID","");
        boolean didChange = userLocalDatabase.getBoolean("didChange",false);

        return new User(email, name,password,exam,id,deviceID,didChange);

    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.apply();
    }

    public boolean getUserLoggedIn(){
         return userLocalDatabase.getBoolean("loggedIn", false);
    }

    public boolean getUserStatusProfilPic(){
        return userLocalDatabase.getBoolean("statusProfilPic", false);
    }

    public void setUserStatusProfilPic(boolean status){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("statusProfilPic", status);
        spEditor.apply();
    }

    public Uri getUserProfilPic(){
        String profilPic = userLocalDatabase.getString("profilPic", "");

        return (Uri.parse(profilPic));
    }

    public void setUserProfilPic(Uri bitmap){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("profilPic", bitmap.toString());
        spEditor.apply();
    }


    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.apply();
    }
}
