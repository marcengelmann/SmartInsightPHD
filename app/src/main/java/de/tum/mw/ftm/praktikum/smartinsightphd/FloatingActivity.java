package de.tum.mw.ftm.praktikum.smartinsightphd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class FloatingActivity extends AppCompatActivity {

    private String artOfQuestion = "";
    private Spinner spinnerrequest;
    private ArrayList<AnfrageProvider> listSpinRequest  = new ArrayList<AnfrageProvider>();
    SpinnerAnfrageAdapter adapter;
    AnfrageLocalStore anfrageLocalStore;
    UserLocalStore userLocalStore;
    EditText addCommit;
    String editor;
    String endTime;
    String question;
    String startTime;
    String taskNumber;
    String taskSubNumber;
    String sitzNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle b = getIntent().getExtras();
        ArrayList<AnfrageProvider> listAnfrageProvider = new ArrayList<AnfrageProvider>();
        listAnfrageProvider = (ArrayList<AnfrageProvider>)b.getSerializable("requests");
        setTitle(R.string.caption_editTask);
        addCommit = (EditText) findViewById(R.id.addCommit);
        spinnerrequest = (Spinner) findViewById(R.id.spinnerRequest);

        // Create custom adapter object ( see below CustomAdapter.java )
        adapter = new SpinnerAnfrageAdapter(this, R.layout.spinner_list_item, listAnfrageProvider);
        // Set adapter to spinner
        spinnerrequest.setAdapter(adapter);

        // Listener called when spinner item selected
        final ArrayList<AnfrageProvider> finalListAnfrageProvider = listAnfrageProvider;
        spinnerrequest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                editor = ((TextView) findViewById(R.id.editor)).getText().toString();;
                endTime = ((TextView) findViewById(R.id.endTime)).getText().toString();;
                question = ((TextView) findViewById(R.id.question)).getText().toString();;
                startTime = ((TextView) findViewById(R.id.startTime)).getText().toString();;
                taskNumber = ((TextView) findViewById(R.id.taskNumber)).getText().toString();;
                taskSubNumber = ((TextView) findViewById(R.id.taskSubNumber)).getText().toString();;
                sitzNumber = ((TextView) findViewById(R.id.sitzNumb)).getText().toString();;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        anfrageLocalStore = new AnfrageLocalStore(this);
        userLocalStore = new UserLocalStore(this);


        anfrageLocalStore.setStatusAnfrageClient(false);
        anfrageLocalStore.clearDataAnfrageClient();
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
        artOfQuestion = String.valueOf(addCommit.getText());
        finalDialog("Kommentar per E-Mail schicken?","Anfrage um " + startTime + " Uhr vom Student " + editor + " soll folgender Kommentar '" +artOfQuestion+"' hinzugeügt werden?").show();
    }
    private Dialog finalDialog(String title,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setPositiveButton("Senden", new DialogInterface.OnClickListener
                () {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                //Todo sende erstelle anfrage zum server und bearbeite diese
                User user = userLocalStore.getUserLogInUser();

                Anfrage anfrage = new Anfrage("0",editor, taskNumber, taskSubNumber, user.name, startTime, endTime,artOfQuestion);
                anfrageLocalStore.storeAnfrageData(anfrage);
                anfrageLocalStore.setStatusAnfrageClient(true);
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

