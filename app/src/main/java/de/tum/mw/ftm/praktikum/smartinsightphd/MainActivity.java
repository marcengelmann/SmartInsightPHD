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
        implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.OnListFragmentInteractionListener, AnfrageListFragment.OnListFragmentInteractionListener{
    UserLocalStore userLocalStore;
    User user = null;

    private boolean fragmentAnfrageListActive = false;
    private boolean startActFirstTime = true;

    TextView emailView;
    TextView nameView;
    CircleImageView profilPicView;
    NavigationView navigationView;
    ArrayList<AnfrageProvider> requests = new ArrayList<>();
    ArrayList<Calendar> requestsCalendar = new ArrayList<>();


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

    private void setFragmentAnfrageliste(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;

        Bundle bundle = new Bundle();
        bundle.putSerializable("requests", requests);

        setTitle(R.string.capition_anfrageliste);
        fragment = new AnfrageListFragment();
        fragment.setArguments(bundle);

        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
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

                    AnfrageProvider anfrage = new AnfrageProvider(id,cutStart , cutEnd, task, subtask, type_of_question, student,seat,exam, done);
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FloatingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("requests", requests);
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

        userLocalStore = new UserLocalStore(this);

        setFragmentAnfrageliste();

        user = userLocalStore.getUserLogInUser();
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

        user = userLocalStore.getUserLogInUser();

        if(authenticate() && startActFirstTime){
            Toast.makeText(MainActivity.this,"Willkommen, "+user.getName() + ", Email: "+user.getEmail(),
                    Toast.LENGTH_LONG).show();
            emailView.setText(user.getEmail());
            nameView.setText(user.getName());
            startActFirstTime = false;
            downloadRequests();
            downloadCalendar();
            navigationView.getMenu().getItem(0).setChecked(true);
            setFragmentAnfrageliste();
            new GcmRegistrationAsyncTask(this).execute();
        }
        else if (startActFirstTime){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            startActFirstTime = true;
        }

        downloadRequests();

        if(user.getDidChange()) {
            refreshUserData();
        }

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

        fragmentAnfrageListActive = false;

        switch(item.getItemId()) {

            case R.id.nav_calendar:
                Bundle bundle = new Bundle();
                bundle.putSerializable("calendar", requestsCalendar);
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
            case R.id.nav_settings:
                setTitle(R.string.caption_settings);
                fragment = new SettingsFragment();
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

        System.out.println("Trying requests download ...");
        JSONClient client = new JSONClient(this, requestResultListener);
        String url = getString(R.string.website)+"/download.php?intent=request&phd="+user.getId()+"&exam_name=" + user.getExam() + "&email=" + user.getEmail() + "&pw=" + user.getPassword();
        System.out.println(url);
        client.execute(url);
    }

    private void downloadCalendar() {
        System.out.println("Trying calendar download ...");
        JSONClient task_client = new JSONClient(this, calendarResultListener);
        String url = getString(R.string.website)+"/download.php?intent=calendar&phd="+user.getId()+"&exam_name="+user.getExam()+"&email="+user.getEmail()+ "&pw=" + user.getPassword();
        System.out.println(url);
        task_client.execute(url);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void uploadData(AnfrageProvider anfrage) {
        System.out.println("Trying data upload ...");
        JSONClient uploader = new JSONClient(this, uploadResultListener);
        String url = getString(R.string.website)+"/upload.php?intent=request_done&exam_name="+user.getExam()+"&email=" + user.getEmail() + "&pw=" + user.getPassword() +"&request_id="+anfrage.getId();
        System.out.println(url);
        uploader.execute(url);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void refreshUserData() {
        System.out.println("Trying user data update ...");
        JSONClient uploader = new JSONClient(this, uploadResultListener);
        String url = getString(R.string.website)+"/upload.php?intent=userdata&phd="+user.getId()+"&exam_name="+user.getExam()+"&email=" + user.getEmail() + "&pw=" + user.getPassword() + "&deviceID=" + user.getDeviceID();
        System.out.println(url);
        uploader.execute(url);
        user.setDidChange(false);
        userLocalStore.storeUserData(user);
    }

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

    @Override
    public void onListFragmentRequestFinishedItem(int position, AnfrageProvider value) {
        //ToDo hier muss das item das fertig ist mit bearbeiten true gestzt werden!
        downloadRequests();
        uploadData(requests.get(position));
        Toast.makeText(MainActivity.this, "Anfrage wurde bearbeitet!",
                Toast.LENGTH_SHORT).show();
    }
}
