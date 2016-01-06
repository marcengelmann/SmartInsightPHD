package de.tum.mw.ftm.praktikum.smartinsightphd;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.OnListFragmentInteractionListener{
    UserLocalStore userLocalStore;
    AnfrageLocalStore anfrageLocalStore;
    User user = null;

    private boolean fragmentAnfrageListActive = false;
    private boolean startActFirstTime = true;

    TextView emailView;
    TextView nameView;
    CircleImageView profilPicView;
    NavigationView navigationView;
    ArrayList<AnfrageProvider> requests = new ArrayList<AnfrageProvider>();
    //Todo In dieses Array müsste am Anfang (Nur einmal) alle aktuellen Prüfungstermine geladen werden!
    ArrayList<Calendar> requestsCalendar = new ArrayList<Calendar>();


    private GetJSONListener uploadResultListener = new GetJSONListener(){
        @Override
        public void onRemoteCallComplete(JSONObject jsonFromNet) {
            try {
                String result = jsonFromNet.getString("result");

                if(result.contains("true")) {
                    System.out.println("Upload successful!");
                    downloadRequests();
                }

                //TODO: Receive repsonse from server!

            } catch(JSONException e) {
                System.out.println(e);
            }catch (NullPointerException e) {
                System.out.println(e);
            }
        }

    };

    private void setFragmentAnfrageliste(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;

        Bundle bundle = new Bundle();
        bundle.putSerializable("requests", (Serializable) requests);

        setTitle(R.string.capition_anfrageliste);
        fragment = new AnfrageListFragment();
        fragment.setArguments(bundle);

        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        fragmentAnfrageListActive = true;

    }

    private GetJSONListener requestResultListener = new GetJSONListener(){
        @Override
        public void onRemoteCallComplete(JSONObject jsonFromNet) {
            try {
                requests.clear();
                JSONArray jsonArray = jsonFromNet.getJSONArray("posts");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String student = obj.getString("linked_student");
                    String exam = obj.getString("linked_exam");
                    String subtask = obj.getString("linked_subtask");
                    String task = obj.getString("linked_task");
                    String phd = obj.getString("linked_phd");
                    String id = obj.getString("id");
                    //Todo sitznummer hinzufügen wir Uhrzeit, Art der Frage
                    //Todo statt matirkelnummer Namen des Studenten schicken
                    //Todo Kommentar direkt mit runterladen
                    AnfrageProvider anfrage = new AnfrageProvider(id,"12:00","12:10", task, subtask, "Inhalt und PUnkte", student,"3","Commit");
                    requests.add(anfrage);
                    System.out.println(anfrage.toString());
                }
                updateListView();

            } catch (JSONException e) {
                System.out.println(e);
            }catch (NullPointerException e) {
                System.out.println(e);
            }

        }

    };

    private GetJSONListener examResultListener = new GetJSONListener(){
        @Override
        public void onRemoteCallComplete(JSONObject jsonFromNet) {

            try {
                JSONArray jsonArray = jsonFromNet.getJSONArray("posts");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    String name = obj.getString("name");
                    String linked_exam = obj.getString("linked_exam");
                    String linked_phd = obj.getString("linked_phd");
                    String id = obj.getString("id");
                    String number = obj.getString("number");
                }

            } catch (JSONException e) {
                System.out.println(e);
            }catch (NullPointerException e) {
                System.out.println(e);
            }

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FloatingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("requests", (Serializable) requests);
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
        emailView = (TextView) headerNavigation.findViewById(R.id.emailView);
        nameView = (TextView) headerNavigation.findViewById(R.id.nameView);
        profilPicView = (CircleImageView) headerNavigation.findViewById(R.id.imageView);

        //generate lsitview für anfragen
        // Construct the data source
        ArrayList<AnfrageProvider> arrayOfUsers = new ArrayList<AnfrageProvider>();
        // Create the adapter to convert the array to views

        userLocalStore = new UserLocalStore(this);
        anfrageLocalStore = new AnfrageLocalStore(this);
        anfrageLocalStore.setStatusAnfrageClient(false);

        setFragmentAnfrageliste();

        user = userLocalStore.getUserLogInUser();

        //todo setze dummy daten für die Klausureinsichttermine im Kalendar
       updateCalendarData();
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

    //Todo ist akuteller dummy funktion um die Daten für die Kalender in das Array zu laden
    private void  updateCalendarData(){
        requestsCalendar.clear();
        for (int i = 0; i < 10 ;i++){
            Calendar calendarItem = new Calendar("DAtum " + i , "Klausurname " + i, "Raum " + i);
            requestsCalendar.add(calendarItem);
        }
    }


    @Override
    protected void onStart(){
        super.onStart();

        user = userLocalStore.getUserLogInUser();

        if(authenticate() == true && startActFirstTime){
            Toast.makeText(MainActivity.this,"Willkommen, "+user.name + ", Email: "+user.email,
                    Toast.LENGTH_LONG).show();
            emailView.setText(user.email);
            nameView.setText(user.name);
            onListFragmentUpdateProfilePic();
            startActFirstTime = false;
            downloadRequests();
            downloadExam();
            navigationView.getMenu().getItem(0).setChecked(true);
            setFragmentAnfrageliste();
        }
        else if (startActFirstTime){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            startActFirstTime = true;
        }

        //Hier kommen updates nach dem Floating action button hin
        if(anfrageLocalStore.getStatusAnfrageClient())
        {
            // ToDo KOmmentar des Dozenten per mail schicken oder in einer Historie/Todo liste speichern
            Anfrage anfrage = anfrageLocalStore.getDataAnfrageClient();
            //uploadData(anfrage);
            //updateListView();
        }
        anfrageLocalStore.setStatusAnfrageClient(false);


    }


    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    private  void updateListView() {
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
        int id = item.getItemId();
        fragmentAnfrageListActive = false;

        if (id == R.id.nav_calendar) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("calendar", (Serializable) requestsCalendar);
            fragment = new CalendarFragment();
            fragment.setArguments(bundle);
            setTitle(R.string.caption_klausur);
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
        else if (id == R.id.nav_abmelden) {
            userLocalStore.clearUserData();
            userLocalStore.setUserLoggedIn(false);
            setTitle(R.string.caption);
            View view;
            view = new View(this);
            Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
            startActivity(myIntent);
            startActFirstTime = true;
        }
        else if (id == R.id.nav_anfragen) {
            setFragmentAnfrageliste();
        }
        else if (id == R.id.nav_settings) {
            setTitle(R.string.caption_settings);
            fragment = new SettingsFragment();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
        else if (id == R.id.nav_statistic) {
            setTitle(R.string.caption_statistic);
            fragment = new StatisticFragment();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void downloadRequests() {
        JSONClient client = new JSONClient(this, requestResultListener);
        // TODO: Website so konfigurieren, dass die Anfrage nur mit Passwort ausgegeben wird.
        // Todo: MUss das hier mit E-mail angepasst werden, da es keine Matrikelnummern mehr gibt
        String url = "http://www.marcengelmann.com/smart/download.php?intent=request&matrikelnummer=" + user.email;
        client.execute(url);
    }

    private void downloadExam() {
        JSONClient client = new JSONClient(this, examResultListener);
        // TODO: Website so konfigurieren, dass die Anfrage nur mit Passwort ausgegeben wird.
        // TODO: ACHTUNG KURZNAME!
        String url = "http://marcengelmann.com/smart/download.php?intent=exam&exam_name=AER";
        client.execute(url);
    }

    private void uploadData(Anfrage anfrage) {
        System.out.println("Trying data upload ...");
        System.out.println(anfrage.toString());
        JSONClient uploader = new JSONClient(this, uploadResultListener);
        // TODO: Website so konfigurieren, dass die Anfrage nur mit Passwort ausgegeben wird.
        // Todo: habe hier statt matrikelnummer email hingeschrieben
        String url = "http://www.marcengelmann.com/smart/upload.php?intent=request&matrikelnummer=" + user.email+"&task_id="+anfrage.linked_task+"&subtask_id="+anfrage.linked_subtask;
        uploader.execute(url);
    }

    private void deleteRequest(AnfrageProvider anfrage) {
        System.out.println("Trying data delete ...");
        JSONClient uploader = new JSONClient(this, uploadResultListener);
        // TODO: Website so konfigurieren, dass die Anfrage nur mit Passwort ausgegeben wird.
        String url = "http://www.marcengelmann.com/smart/upload.php?intent=delete_request&request_id="+anfrage.id;
        uploader.execute(url);
    }

    @Override
    public void onListFragmentUpdateProfilePic() {
        if (userLocalStore.getUserStatusProfilPic()){
            User user = userLocalStore.getUserLogInUser();
            profilPicView.setImageURI(userLocalStore.getUserProfilPic());
        }
        else {
            profilPicView.setImageResource(R.mipmap.ic_launcher_fernrohr);
        }
    }
}
