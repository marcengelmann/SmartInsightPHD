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

public class FloatingActivity extends AppCompatActivity {

    private String artOfQuestion = "";
    private MaterialSpinner spinnerrequest;
    private ArrayList<AnfrageProvider> listSpinRequest  = new ArrayList<AnfrageProvider>();
    SpinnerAnfrageAdapter adapter;
    AnfrageLocalStore anfrageLocalStore;
    UserLocalStore userLocalStore;
    EditText addCommit;
    TextView selectRequest;
    String editor;
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
        Bundle b = getIntent().getExtras();
        ArrayList<AnfrageProvider> listAnfrageProvider = new ArrayList<AnfrageProvider>();
        listAnfrageProvider = (ArrayList<AnfrageProvider>)b.getSerializable("requests");
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
                if(position >= 0) {
                    selectRequest.setVisibility(View.VISIBLE);
                    editor = ((TextView) findViewById(R.id.editor)).getText().toString();
                    endTime = ((TextView) findViewById(R.id.endTime)).getText().toString();
                    question = ((TextView) findViewById(R.id.question)).getText().toString();
                    startTime = ((TextView) findViewById(R.id.startTime)).getText().toString();
                    taskNumber = ((TextView) findViewById(R.id.taskNumber)).getText().toString();
                    taskSubNumber = ((TextView) findViewById(R.id.taskSubNumber)).getText().toString();
                    sitzNumber = ((TextView) findViewById(R.id.sitzNumb)).getText().toString();
                    exam = ((TextView) findViewById(R.id.exam)).getText().toString();
                }
                else{
                    selectRequest.setVisibility(View.GONE);
                }
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
        else {

            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            User user = userLocalStore.getUserLogInUser();
            String[] TO = {user.email};
            emailIntent.setType("text/html");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, TO);

            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Kommentar zur Klausur " + exam + " vom Studenten " + editor);

            String sendText = "Zur folgender Anfrage:\nStudent: " + editor + "\nKlausur: " + exam + "\nAufgabe: " + taskNumber + taskSubNumber + "\nFrage zu: " + question +"\n\nSoll folgender Kommentar hinzugefügt werden:\n" + commit;


            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, sendText);

            try {
                startActivity(Intent.createChooser(emailIntent, "Sende E-Mail..."));
                finish();
            }
            catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(FloatingActivity.this, "Es ist kein E-Mail Client vorhanden. E-Mail kann nicht gesendet werden.", Toast.LENGTH_SHORT).show();
            }
        }

    }

}

