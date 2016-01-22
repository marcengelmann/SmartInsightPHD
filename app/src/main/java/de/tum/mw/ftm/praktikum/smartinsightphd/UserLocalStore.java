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
        spEditor.putString(String.valueOf(R.string.name), user.getName());
        spEditor.putString(String.valueOf(R.string.email), user.getEmail());
        spEditor.putString(String.valueOf(R.string.password),user.getPassword());
        spEditor.putString(String.valueOf(R.string.exam),user.getExam());
        spEditor.putString(String.valueOf(R.string.id),user.getId());
        spEditor.putString(String.valueOf(R.string.deviceID),user.getDeviceID());
        spEditor.putBoolean(String.valueOf(R.string.didChange),user.getDidChange());
        spEditor.apply();
    }

    public User getUserLogInUser(){
        String name = userLocalDatabase.getString(String.valueOf(R.string.name), "");
        String email = userLocalDatabase.getString(String.valueOf(R.string.email),"");
        String exam = userLocalDatabase.getString(String.valueOf(R.string.exam), "");
        String password = userLocalDatabase.getString(String.valueOf(R.string.password),"");
        String id = userLocalDatabase.getString(String.valueOf(R.string.id),"");
        String deviceID = userLocalDatabase.getString(String.valueOf(R.string.deviceID),"");
        boolean didChange = userLocalDatabase.getBoolean(String.valueOf(R.string.didChange),false);

        return new User(email, name,password,exam,id,deviceID,didChange);

    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean(String.valueOf(R.string.loggedIn), loggedIn);
                spEditor.apply();
    }

    public boolean getUserLoggedIn(){
         return userLocalDatabase.getBoolean(String.valueOf(R.string.loggedIn), false);
    }

    public boolean getUserStatusProfilPic(){
        return userLocalDatabase.getBoolean(String.valueOf(R.string.statusProfilPic), false);
    }

    public void setUserStatusProfilPic(boolean status){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean(String.valueOf(R.string.statusProfilPic), status);
                spEditor.apply();
    }

    public Uri getUserProfilPic(){
        String profilPic = userLocalDatabase.getString(String.valueOf(R.string.profilPic), "");

        return (Uri.parse(profilPic));
    }

    public void setUserProfilPic(Uri bitmap){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString(String.valueOf(R.string.profilPic), bitmap.toString());
                spEditor.apply();
    }


    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.apply();
    }
}
