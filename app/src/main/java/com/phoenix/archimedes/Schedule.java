package com.phoenix.archimedes;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Schedule extends AppCompatActivity {
    String category, timeslot, activityname, activitylocation;
    Button submit;
    EditText name, location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        submit = (Button) findViewById(R.id.submit);
        name = (EditText) findViewById(R.id.name);
        location = (EditText) findViewById(R.id.location);

//        setUpNavigationDrawer();
//
//        getFragmentManager().addOnBackStackChangedListener(backStackListener);


        Spinner cspinner = (Spinner) findViewById(R.id.cspinner);
        ArrayAdapter<String> cadapter = new ArrayAdapter<String>(Schedule.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.categories));

        cadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cspinner.setAdapter(cadapter);

        cspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                category = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        Spinner tspinner = (Spinner) findViewById(R.id.tspinner);
        ArrayAdapter<String> tadapter = new ArrayAdapter<String>(Schedule.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.timeslots));

        tadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tspinner.setAdapter(tadapter);

        tspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                timeslot = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    activityname = name.getText().toString();
                    activitylocation = location.getText().toString();
                    String tp = timeslot.substring(0,2);
                    String tp1 = timeslot.substring(8,10);
                    String time = tp + tp1;
                    System.out.print("Request Send");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("activityName", activityname);
                    jsonObject.put("location", activitylocation);
                    jsonObject.put("user",1);
                    jsonObject.put("activityType",category);
                    jsonObject.put("isSchedule",true);
                    jsonObject.put("activityTime",time);
                    jsonObject.put("duration",1);
                    InsertSV(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                Log.i(category, "Choosen Category!!");
//                Log.i(time, "TimeSlot:");
//                Log.i(activityname, "ActivityName:");
//                Log.i(activitylocation, "ActivityLocation:");


            }
        });
    }

    private void InsertSV(JSONObject jsonObject) {
        try {

            final String ipAddress = getResources().getString(R.string.ipAddress);
            String URL_POST = "http://"+ipAddress+":9000/addSchedule";
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL_POST, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response.toString());
                    Intent main = new Intent(Schedule.this, MainActivity.class);
                    finish();
                    startActivity(main);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();

                    return headers;
                }
            };


            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }catch(Exception e){
            e.printStackTrace();
        }
    }


}

