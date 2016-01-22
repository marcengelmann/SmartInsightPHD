package de.tum.mw.ftm.praktikum.smartinsightphd;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProfilFragment.OnListFragmentInteractionListener, AnfrageListFragment.OnListFragmentInteractionListener{
    private UserLocalStore userLocalStore;

    // Attribute, welche true ist wenn das Fragment AnfrageListe gelaen ist
    private boolean fragmentAnfrageListActive = false;
    // ist true wenn noch keine User login daten zum ersten mal geladen sind
    private boolean startActFirstTime = true;

    private TextView emailView;
    private TextView nameView;
    private CircleImageView profilPicView;
    private NavigationView navigationView;

    // LIste, die die Anfragen der Studenten speichert
    private ArrayList<RequestsStudent> requests = new ArrayList<>();
    // Liste, die die Klausureinsichtstermine speichert
    private ArrayList<Calendar> requestsCalendar = new ArrayList<>();


    private GetJSONListener uploadResultListener = new GetJSONListener() {
        @Override
        public void onRemoteCallComplete(JSONObject jsonFromNet) {
            try {
                String result = jsonFromNet.getString("result");
                if (result.contains("true")) {
                    System.out.println("Upload successful!");
                    downloadRequests();
                } else {
                    Toast.makeText(MainActivity.this, result,
                            Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
            }
        }

    };

    // methode di eAufgerufen werden soll, wenn das Fragment Anfragelsite geladen werden osll
    private void setFragmentAnfrageliste(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;
        // Wenn die Anfrageliste geladen wird, soll die Liste mit den Anfragen ovn der Studenten
        // Über ein bundle mit geladen werden
        Bundle bundle = new Bundle();
        bundle.putSerializable(String.valueOf(R.string.bundleRequests), requests);
        // In der Toolbar soll der titel geladen werden
        setTitle(R.string.capition_anfrageliste);
        fragment = new AnfrageListFragment();
        fragment.setArguments(bundle);

        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        // true setzen, da das fragment active ist
        fragmentAnfrageListActive = true;
    }

    private GetJSONListener requestResultListener = new GetJSONListener() {
        @Override
        public void onRemoteCallComplete(JSONObject jsonFromNet) {
            try {
                requests.clear();
                JSONArray jsonArray = jsonFromNet.getJSONArray("posts");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String subtask = obj.getString("subtask_name");
                    String task = obj.getString("task_name");
                    String student = obj.getString("linked_student");
                    String id = obj.getString("id");
                    String startTime = obj.getString("start_time");
                    String endTime = obj.getString("end_time");
                    String type_of_question = obj.getString("type_of_question");
                    String exam = obj.getString("linked_exam");
                    String seat = obj.getString("seat");
                    String bare_Bool = obj.getString("done");

                    String cutStart = startTime.substring(11, startTime.length() - 3);
                    String cutEnd = endTime.substring(11, endTime.length() - 3);

                    String done;

                    if(bare_Bool.contains("0")) {
                        done = "false";
                    } else {
                        done = "true";
                    }

                    RequestsStudent anfrage = new RequestsStudent(id,cutStart , cutEnd, task, subtask, type_of_question, student,seat,exam, done);
                    requests.add(anfrage);
                }
            } catch (JSONException e) {
                requests.clear();
                Toast.makeText(MainActivity.this, "Keine Anfragen verfügbar!",
                        Toast.LENGTH_SHORT).show();
            }
            updateListView();
        }

    };

    private GetJSONListener calendarResultListener = new GetJSONListener() {
        @Override
        public void onRemoteCallComplete(JSONObject jsonFromNet) {
            try {
                requestsCalendar.clear();
                JSONArray jsonArray = jsonFromNet.getJSONArray("posts");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    String date = obj.getString("date");
                    String name = obj.getString("name");
                    String room = obj.getString("room");
                    String responsible_person = obj.getString("responsible_person");
                    String number_of_students = obj.getString("number_of_students");
                    String mean_grade = obj.getString("mean_grade");
                    Calendar calendar = new Calendar(date,name,room,number_of_students, responsible_person,mean_grade);

                    requestsCalendar.add(calendar);
                    System.out.println(calendar.toString());
                }
            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "Serververbindung fehlgeschlagen!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // wenn der Floating action button aktiviert wurde, soll die Klasse
                // FloatingActivity geladen werden und abei die aktuelle Anfragen der Studenten
                // mit übertragen werden, damit die in dem Spinner angezeigt werden können
                Intent myIntent = new Intent(v.getContext(), FloatingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(String.valueOf(R.string.bundleRequests), requests);
                myIntent.putExtras(bundle);
                startActivity(myIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerNavigation = navigationView.getHeaderView(0);
        headerNavigation.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // in diesen zwei TextView ist das die E-Mail addresse und der Name des Users,
        // die in dem Naviationdrawer im header angezeigt werden, so wie das Profilbild
        emailView = (TextView) headerNavigation.findViewById(R.id.emailView);
        nameView = (TextView) headerNavigation.findViewById(R.id.nameView);
        profilPicView = (CircleImageView) headerNavigation.findViewById(R.id.imageView);

        userLocalStore = new UserLocalStore(this);
        // Fragment zur Anfrageliste osll immer der startbildschirm sein
        setFragmentAnfrageliste();
        // uplaod das profilbild in navigation drawer header
        onListFragmentUpdateProfilePic();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        // get user
        User user = userLocalStore.getUserLogInUser();

        if(authenticate() && startActFirstTime){

            // wenn der User eingeloggt ist und die App zum ersten mal gestarteter wird,
            // soll über ein Toast der User Willkommen geheißen werden
            Toast.makeText(MainActivity.this,"Willkommen, "+user.getName() + ", Email: "+user.getEmail(),
                    Toast.LENGTH_LONG).show();
            // setzen der email addresse und des namens im header des navigation drawer
            emailView.setText(user.getEmail());
            nameView.setText(user.getName());
            // soll danach nicht mehr geladen werden, daher wird das attribute false gesetzt
            startActFirstTime = false;
            // die  methoden downloaden vom server die aktuelel
            //  Klausureinsichtstermine geladen
            downloadCalendar();
            // setze im navtation drawer, dass die Anfrageliste ausgewählt ist
            navigationView.getMenu().getItem(0).setChecked(true);
            setFragmentAnfrageliste();
            new GcmRegistrationAsyncTask(this).execute();
        }
        else if (startActFirstTime){
            // wenn noch kein nutzer gespeichert ist, wird ein login activity geladen
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            startActFirstTime = true;
        }
        // die methode downlaoded vom server die aktuelle Anfragelsite
        downloadRequests();
        // wenn sich etwas verändert aht, soll die User daten aktualisiert werden
        if(user.getDidChange()) {
            refreshUserData();
        }

    }


    private boolean authenticate(){
        // ToDo überprüfen ob die Nutzerdaten noch aktuell sind und dementsprechens
        // im userLocalstore die UserLoggedIn false setzen
        return userLocalStore.getUserLoggedIn();
    }

    // Methode soll die aktuelle Anfrage liste updaten, wenn sich etwas geändert hat
    private  void updateListView() {
        // hier uwrde noch keien andere möglcihkeit gefunden zu überprüfen, ob das Fragement
        // welches die Anfragen auflistet aktive ist und dementspreichend soll die Liste über
        // die methode updateFragmentListView geupdtet werden
        if (fragmentAnfrageListActive) {
            AnfrageListFragment anfrageListFragment = (AnfrageListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.container);
            if (anfrageListFragment != null) {
                anfrageListFragment.updateFragmentListView(requests);
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;

        fragmentAnfrageListActive = false;

        switch(item.getItemId()) {

            case R.id.nav_calendar:
                Bundle bundle = new Bundle();
                bundle.putSerializable(String.valueOf(R.string.calendar), requestsCalendar);
                fragment = new CalendarFragment();
                fragment.setArguments(bundle);
                setTitle(R.string.caption_klausur);
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                break;
            case R.id.nav_abmelden:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                setTitle(R.string.caption);
                View view;
                view = new View(this);
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(myIntent);
                startActFirstTime = true;
                break;
            case R.id.nav_anfragen:
                setFragmentAnfrageliste();
                break;
            case R.id.nav_profil:
                setTitle(R.string.caption_profil);
                fragment = new ProfilFragment();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                break;
            case R.id.nav_statistic:
                setTitle(R.string.caption_statistic);
                fragment = new StatisticFragment();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void downloadRequests() {
        User user = userLocalStore.getUserLogInUser();
        System.out.println("Trying requests download ...");
        JSONClient client = new JSONClient(this, requestResultListener);
        String url = getString(R.string.website)+"/download.php?intent=request&phd="+user.getId()+"&exam_name=" + user.getExam() + "&email=" + user.getEmail() + "&pw=" + user.getPassword();
        System.out.println(url);
        client.execute(url);
    }

    private void downloadCalendar() {
        User user = userLocalStore.getUserLogInUser();
        System.out.println("Trying calendar download ...");
        JSONClient task_client = new JSONClient(this, calendarResultListener);
        String url = getString(R.string.website)+"/download.php?intent=calendar&phd="+user.getId()+"&exam_name="+user.getExam()+"&email="+user.getEmail()+ "&pw=" + user.getPassword();
        System.out.println(url);
        task_client.execute(url);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void uploadData(RequestsStudent anfrage) {
        User user = userLocalStore.getUserLogInUser();
        System.out.println("Trying data upload ...");
        JSONClient uploader = new JSONClient(this, uploadResultListener);
        String url = getString(R.string.website)+"/upload.php?intent=request_done&exam_name="+user.getExam()+"&email=" + user.getEmail() + "&pw=" + user.getPassword() +"&request_id="+anfrage.getId();
        System.out.println(url);
        uploader.execute(url);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void refreshUserData() {
        User user = userLocalStore.getUserLogInUser();
        System.out.println("Trying user data update ...");
        JSONClient uploader = new JSONClient(this, uploadResultListener);
        String url = getString(R.string.website)+"/upload.php?intent=userdata&phd="+user.getId()+"&exam_name="+user.getExam()+"&email=" + user.getEmail() + "&pw=" + user.getPassword() + "&deviceID=" + user.getDeviceID();
        System.out.println(url);
        uploader.execute(url);
        user.setDidChange(false);
        userLocalStore.storeUserData(user);
    }

    // update das profil bild in dem header de Naviation drawer
    @Override
    public void onListFragmentUpdateProfilePic() {
        if (userLocalStore.getUserStatusProfilPic()){
            //User user = userLocalStore.getUserLogInUser();
            profilPicView.setImageURI(userLocalStore.getUserProfilPic());
        }
        else {
            profilPicView.setImageResource(R.drawable.ic_phd_icon);
        }
    }

    // wird aufgerufen vom Fragment die die Anfrage liste, wennn man eine Anfrage bearbeitet hat über
    // das den Check button
    @Override
    public void onListFragmentRequestFinishedItem(int position, RequestsStudent value) {
        downloadRequests();
        uploadData(requests.get(position));
        Toast.makeText(MainActivity.this, "Anfrage wurde bearbeitet!",
                Toast.LENGTH_SHORT).show();
    }
}
