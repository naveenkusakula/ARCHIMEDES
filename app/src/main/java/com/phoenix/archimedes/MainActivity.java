package com.phoenix.archimedes;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.phoenix.archimedes.GoogleAuthentication.GPlusFragment;
import com.phoenix.archimedes.ModelAdapters.ScheduleListAdapter;
import com.phoenix.archimedes.Models.ScheduleList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ScheduleListAdapter mAdapter;
    private List<ScheduleList> scheduleList = new ArrayList<>();

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dl = (DrawerLayout) findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab = findViewById(R.id.fab);

        final NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Log.e("********", ""+id +"*****"+R.id.navLogout+"*****"+R.id.navUnscheduled);
                switch (id){
                    case R.id.navLogout:
                        finish();
                        startActivity(new Intent(getApplicationContext(), GPlusFragment.class));
                        break;
                    case R.id.navUnscheduled:
                        Intent unscheduled = new Intent(MainActivity.this, Unscheduled.class);
                        startActivity(unscheduled);
                        break;
                }
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent form = new Intent(MainActivity.this, Schedule.class);
                startActivity(form);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        prepareData();


    }

    private void prepareData() {
        try {

          getScheduleList();
          //  dummyData();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item);
    }


    public void dummyData(){
        ScheduleList sl = new ScheduleList();
        sl.setScheduleId(1);
        sl.setActivityName("Mobile Computing");
        sl.setDuration(1);
        sl.setLocation("Henry Hicks");
        sl.setTimeSlot("19:20");

        sl.setSchedule(true);
        sl.setActivityType("Class");
        scheduleList.add(sl);
        mAdapter = new ScheduleListAdapter(scheduleList);
        //  mAdapter.notifyItemChanged();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    public void getScheduleList() {
        final String ipAddress = getResources().getString(R.string.ipAddress);
        System.out.println(ipAddress);
        String requestUrl = "http://"+ipAddress+":9000/user/1";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                requestUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ScheduleList sl = new ScheduleList();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject = (JSONObject) response.get("data");
                            JSONArray scheduleArray = new JSONArray();
                            scheduleArray = (JSONArray) jsonObject.get("scheduleList");
                            System.out.println("schedule list size"+ ((JSONArray) jsonObject.get("scheduleList")).length());
                            for (int i = 0; i < scheduleArray.length(); i++) {
                                sl = new ScheduleList();
                                JSONObject jsonObj = (JSONObject) scheduleArray.get(i);

                                    sl.setScheduleId(jsonObj.getLong("id"));
                                    sl.setActivityName(jsonObj.get("activityName").toString());
                                    sl.setDuration(Long.parseLong(jsonObj.get("duration").toString()));
                                    sl.setLocation(jsonObj.get("location").toString());
                                    sl.setTimeSlot(jsonObj.getString("activityTime").toString());
                                    sl.setScheduleId(Long.parseLong(jsonObj.get("id").toString()));
                                    sl.setSchedule(jsonObj.getBoolean("schedule"));
                                sl.setActivityType(jsonObj.getString("activityType").toString());
                                    scheduleList.add(sl);

                            }
                            System.out.println("schedule list size"+scheduleList.size());
                            mAdapter = new ScheduleListAdapter(scheduleList);
                          //  mAdapter.notifyItemChanged();
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
//                             Log.e("Rest Response",jsonObject.get("userName").toString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response", error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);

    }



    public void getLocation(){
        Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
    }

}
