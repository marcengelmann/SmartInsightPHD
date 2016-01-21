package de.tum.mw.ftm.praktikum.smartinsightphd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;
/*
* Diese Activity wird von der Main Activity geöffnet, wenn der Floating Action Button gedrückt wurde
* Hier soll der PHD eine Anfrage auswählen können und diese Kommentieren.
* Der Kommentar plus die Angaben zu der Anfrage, kann er sich dann per E-Mail zuschicken
* */
public class FloatingActivity extends AppCompatActivity {
    // Spinner zur Auswahl der Anfrage
    private MaterialSpinner spinnerrequest;
    // Eigener Adapter, der die Struktur der Anfrage übernehmen kann
    SpinnerAnfrageAdapter adapter;
    // Variable, die den aktuellen USER daten speichert
    UserLocalStore userLocalStore;
    // Textfeld, welches den Kommentar beinahtlet
    EditText addCommit;
    TextView selectRequest;

    //Variablen die die Daten ovn der aktuellen anfrage speichern
    String student;
    String endTime;
    String question;
    String startTime;
    String taskNumber;
    String taskSubNumber;
    String sitzNumber;
    String exam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Über das Bundle wurde die aktuelle Anfrageliste mitgesendet, die über den Spinner angezeigt
        //werden soll
        Bundle b = getIntent().getExtras();
        ArrayList<AnfrageProvider> listAnfrageProvider = new ArrayList<AnfrageProvider>();
        // in dieser variable wird ei aktuelle Anfrageliste von dem PHD gespeichert
        listAnfrageProvider = (ArrayList<AnfrageProvider>)b.getSerializable(String.valueOf(R.string.bundleRequests));

        // Hier wird der titel von der Action bar gesetzt
        setTitle(R.string.caption_editTask);

        addCommit = (EditText) findViewById(R.id.addCommit);
        spinnerrequest = (MaterialSpinner) findViewById(R.id.spinnerRequest);
        selectRequest = (TextView)findViewById(R.id.rowDesc);

        // Create custom adapter object ( see below CustomAdapter.java )
        adapter = new SpinnerAnfrageAdapter(this, R.layout.fragment_list_item, listAnfrageProvider);
        // Set adapter to spinner
        spinnerrequest.setAdapter(adapter);
        // Listener called when spinner item selected
        spinnerrequest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                // wenn der spinner einen negativen wert auswählt, wurde noch keine Anfrage aus-
                //gewählt
                if (position >= 0) {
                    // Hier wird eine Überschrift über den Spinner gesetzt, dass eine Anfrage
                    // ausgewählt wurde
                    selectRequest.setVisibility(View.VISIBLE);
                    // holen der ausgewählten daten und speichern dieser Daten in variablen
                    student = ((TextView) findViewById(R.id.editor)).getText().toString();
                    endTime = ((TextView) findViewById(R.id.endTime)).getText().toString();
                    question = ((TextView) findViewById(R.id.question)).getText().toString();
                    startTime = ((TextView) findViewById(R.id.startTime)).getText().toString();
                    taskNumber = ((TextView) findViewById(R.id.taskNumber)).getText().toString();
                    taskSubNumber = ((TextView) findViewById(R.id.taskSubNumber)).getText().toString();
                    sitzNumber = ((TextView) findViewById(R.id.sitzNumb)).getText().toString();
                    exam = ((TextView) findViewById(R.id.exam)).getText().toString();
                } else {
                    // wenn keine anfrage ausgewählt wurde, zeigt der Spinner an, dass man eine
                    // Anfrage auswählen soll, daher wird keine Überschrift über dem Spinner benötigt
                    selectRequest.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        // get the user local store data
        userLocalStore = new UserLocalStore(this);

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

    /*
    * Diese Funktion wird aufgerufen, wenn der Button gedrückt wird um eine kommentierte Anfrage
    * per mail zu schikcen
    * */
    public void onButtonSendQuestion(View view){
        // holen des kommentares
        String commit = String.valueOf(addCommit.getText());
        //Reset erros
        spinnerrequest.setError(null);
        addCommit.setError(null);
        // Check for a valid password, if the user entered one.
        if (spinnerrequest.getSelectedItemPosition() <= 0) {
            spinnerrequest.setError("Bitte wähle eine Anfrage aus!");
            spinnerrequest.requestFocus();
        }
        else  if(TextUtils.isEmpty(commit)) {
            addCommit.setError(getString(R.string.error_field_required));
            addCommit.requestFocus();
        }
        else { // hier kommt man rein, wenn kein error vorhanden ist
            // hier werden die Daten für die E-Mailübertragung gesetzt
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            User user = userLocalStore.getUserLogInUser();
            //Empfänger speichern
            String[] TO = {user.email};
            // setzen des text types
            emailIntent.setType("text/html");
            // setzen an wen die E-mail gehen soll
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, TO);
            // Betreffe hinzufügen
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Kommentar zur Klausur " + exam + " vom Studenten " + student);
            // text erstellen und speichern
            String sendText = "Zur folgender Anfrage:\nStudent: " + student
                    + "\nKlausur: " + exam + "\nAufgabe: " + taskNumber + taskSubNumber
                    + "\nFrage zu: " + question +"\n\nSoll folgender Kommentar hinzugefügt werden:\n" + commit;
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, sendText);
            // start die E-Mail activity, wo sie sidn e-mail provider auswählen können
            try {
                startActivity(Intent.createChooser(emailIntent, "Sende E-Mail..."));
                finish();
            }
            catch (android.content.ActivityNotFoundException ex) {
                //information, dass kein e-mail client vorhanden ist und daher keine e-Mail als kommentar gesendet ewrden kann
                Toast.makeText(FloatingActivity.this, "Es ist kein E-Mail Client vorhanden. E-Mail kann nicht gesendet werden.", Toast.LENGTH_SHORT).show();
            }
        }

    }

}

