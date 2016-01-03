package de.tum.mw.ftm.praktikum.smartinsightphd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class FloatingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.caption_editTask);
          }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onButtonSendQuestion(View view){


        finalDialog("Anfrage senden","Zur Aufgabe: " + " hast du eine Frage zu: " ).show();
    }
    private Dialog finalDialog(String title,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setPositiveButton("Senden", new DialogInterface.OnClickListener
                () {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {

                finish();
            }
        });
        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
// Canel was clicked
            }
        });

        builder.setCancelable(false);
        return builder.create();    }
}

