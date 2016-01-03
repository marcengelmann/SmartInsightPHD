package de.tum.mw.ftm.praktikum.smartinsight;

import android.graphics.Bitmap;

/**
 * Created by Rebecca on 13.12.2015.
 */
public class User {
    String name, email, exam, matrikelnummer;

    public User (String email, String exam, String name, String matrikelnummer){
        this.email = email;
        this.exam = exam;
        this.matrikelnummer = matrikelnummer;
        this.name = name;
    }
}
