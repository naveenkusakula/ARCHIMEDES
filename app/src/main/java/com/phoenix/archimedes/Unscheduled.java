package com.phoenix.archimedes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.android.volley.toolbox.Volley;
import com.phoenix.archimedes.Models.ScheduleList;
import com.phoenix.archimedes.Scheduling.SchedulingAlgorithm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Unscheduled extends AppCompatActivity {

    String category,activityname,activitylocation, activityduration;
    Button submit;
    EditText name, location, duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unscheduled);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        submit = (Button) findViewById(R.id.submit);
        name = (EditText)findViewById(R.id.name);
        location = (EditText)findViewById(R.id.location);
        duration = (EditText)findViewById(R.id.duration);


        Spinner cspinner = (Spinner) findViewById(R.id.cspinner);
        ArrayAdapter<String> cadapter = new ArrayAdapter<String>(Unscheduled.this,
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();

                RequestQueue requestQueue =  Volley.newRequestQueue(Unscheduled.this);
                try{
                    activityname = name.getText().toString();
                    activitylocation = location.getText().toString();
                    activityduration = duration.getText().toString();
                    jsonObject.put("activityName", activityname);
                    jsonObject.put("location", activitylocation);
                    jsonObject.put("duration", activityduration);
                    jsonObject.put("user",1);
                    jsonObject.put("activityType",category);
                    jsonObject.put("isSchedule",false);

                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                final String ipAddress = getResources().getString(R.string.ipAddress);
//            String requestUrl = "http://134.190.158.43:9000/user/1";
                String requestUrl = "http://"+ipAddress+":9000/user/1";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://"+ipAddress+":9000/addSchedule", jsonObject, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.print("Response called");
                        getData();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(jsonObjectRequest);

            }
        });

    }

    public JSONObject  getData(){
        JSONObject jsonObject = new JSONObject();
        try {

            final String ipAddress = getResources().getString(R.string.ipAddress);
//            String requestUrl = "http://134.190.158.43:9000/user/1";
            //   String activityType ="";
            String requestUrl = "http://"+ipAddress+":9000/user/1";
            final RequestQueue requestQueue = Volley.newRequestQueue(this);
            final JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    requestUrl,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                ScheduleList sl = new ScheduleList();
                                JSONObject jsonObject = new JSONObject();
                                JSONObject obj = new JSONObject(response.toString());
                                JSONObject data = obj.getJSONObject("data");
                                JSONArray schedule = data.getJSONArray("scheduleList");
                                String userid = data.get("id").toString();
                                String activityType ="";
                                String location = "";
                                Map<String, Integer> activites = new HashMap<String, Integer>();
                                ArrayList<Integer> classschedule = new ArrayList<Integer>();
                                for (int i = 0; i < schedule.length(); i++) {
                                    JSONObject object = schedule.getJSONObject(i);

                                    int duration = (int) object.get("duration");
                                    Log.i("isschedule,",object.get("schedule").toString());
                                    boolean isSchedule = (boolean)object.get("schedule");

                                    if (isSchedule) {
                                        String times = object.getString("activityTime");
                                        if(times != "null"){
                                            int timeslot = Integer.parseInt(times);
                                            classschedule.add(timeslot);
                                        }

                                    } else {
                                        String activityName = object.get("activityName").toString();
                                        activityType = object.get("activityType").toString();
                                        location = object.get("location").toString();
                                        activites.put(activityName, duration);
                                    }
                                    if(object.get("activityTime").toString().equals("null")) {
                                        JsonObjectRequest objectRequest = new JsonObjectRequest(
                                                Request.Method.GET,
                                                "http://"+ipAddress+":9000/deleteSchedule/" + object.getLong("id"),
                                                null,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        try {
                                                            Log.i("after scheduling", "successful");

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


                                }

                                SchedulingAlgorithm sc = new SchedulingAlgorithm();
                                Map<Integer, String> res = new HashMap<Integer, String>();
                                res = sc.schedule(classschedule, activites);

                                Set<Map.Entry<Integer, String>> st = res.entrySet();

                                for (Map.Entry<Integer, String> me : st) {
                                    Log.d("final slots: ", me.getKey().toString() + ":" + me.getValue());

                                }
                                for (Map.Entry<Integer, String> me : st) {
                                    if (!(me.getValue().equals("schedulded"))) {
                                        if (!(me.getValue().equals("free"))) {
                                            Log.d("final update slots: ", me.getKey().toString() + ":" + me.getValue());
                                            JSONObject schedule1 = new JSONObject();
                                            schedule1.put("id", null);
                                            schedule1.put("activityName", me.getValue().toString());
                                            schedule1.put("duration", 1);
                                            schedule1.put("isSchedule", false);
                                            schedule1.put("location", location);
                                            schedule1.put("activityTime", me.getKey().toString());
                                            schedule1.put("user", 1);
                                            schedule1.put("activityType",activityType);

                                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://"+ipAddress+":9000/addSchedule", schedule1, new Response.Listener<JSONObject>() {

                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    finish();
                                                    startActivity(new Intent(Unscheduled.this,MainActivity.class));

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



                                            requestQueue.add(jsonObjectRequest);


                                        }
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            Log.e("Rest Response", response.toString());
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
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Unscheduled.this,MainActivity.class);
        finish();
        startActivity(setIntent);
    }

}
