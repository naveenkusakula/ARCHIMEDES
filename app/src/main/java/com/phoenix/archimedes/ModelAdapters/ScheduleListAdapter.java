package com.phoenix.archimedes.ModelAdapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonParser;
import com.phoenix.archimedes.MainActivity;
import com.phoenix.archimedes.Models.ScheduleList;
import com.phoenix.archimedes.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleListAdapter  extends RecyclerView.Adapter<ScheduleListAdapter.MyViewHolder>{
    private List<ScheduleList> scheduleList;

    private String location;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView activityName, startTime, duration,location,scheduleId,tvActivityName;
        public RelativeLayout relativeLayout;
        public Button btnAccept,btnReject;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.scheduleList);
            activityName = (TextView) view.findViewById(R.id.activityName);
            startTime = (TextView) view.findViewById(R.id.startTime);
            duration = (TextView) view.findViewById(R.id.duration);
            location =  (TextView) view.findViewById(R.id.location);
            btnAccept = (Button)view.findViewById(R.id.btnAccept);
            btnReject = (Button) view.findViewById(R.id.btnReject);
            scheduleId = (TextView) view.findViewById(R.id.scheduleId);
            tvActivityName = (TextView) view.findViewById(R.id.tvActivityName);
            imageView = (ImageView)view.findViewById(R.id.activity_image);
            mContext = view.getContext();
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        final String ipAddress = mContext.getResources().getString(R.string.ipAddress);
                        // System.out.println("--------clicked" + scheduleList.get(getAdapterPosition()).getScheduleId());
                        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                        JSONObject sl = new JSONObject();
                        sl.put("id", scheduleList.get(getAdapterPosition()).getScheduleId());
                        sl.put("isSchedule", true);

                        sl.put("activityName", scheduleList.get(getAdapterPosition()).getActivityName().toString());
                        sl.put("duration", 1);

                        sl.put("location", scheduleList.get(getAdapterPosition()).getLocation().toString());
                        sl.put("activityTime", scheduleList.get(getAdapterPosition()).getTimeSlot().toString());
                        sl.put("user", 1);
                        sl.put("activityType",scheduleList.get(getAdapterPosition()).getActivityType().toString());
                        JsonObjectRequest objectRequest = new JsonObjectRequest(
                                Request.Method.POST,
                                "http://"+ipAddress+":9000/addSchedule",
                                sl,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            Context context = mContext;
                                            Activity activity = (Activity) context;
                                            activity.finish();
                                            context.startActivity(new Intent(context, MainActivity.class));
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


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String ipAddress = mContext.getResources().getString(R.string.ipAddress);
                    RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                    JsonObjectRequest objectRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            "http://"+ipAddress+":9000/deleteSchedule/"+scheduleList.get(getAdapterPosition()).getScheduleId(),
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {


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
                    Context context = view.getContext();
                    Activity activity = (Activity) context;
                    activity.finish();
                    context.startActivity(new Intent(context, MainActivity.class));
                }
            });


            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ScheduleList sl = scheduleList.get(position);
                    Context context = view.getContext();
                    sl.getLocation();
//
//                    locationCoordinates.put("Henry Hicks","44.6362, -63.5931");
//                    locationCoordinates.put("Dalplex","44.6340, -63.5913");
//                    locationCoordinates.put("Rowe Management","44.6370, -63.5882");
//                    locationCoordinates.put("Sexton Campus","44.6425, -63.5723");




                    RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                    JsonArrayRequest objectRequest = new JsonArrayRequest(
                            Request.Method.GET,
                            "https://nominatim.openstreetmap.org/search?q="+ sl.getLocation()+",CA&format=json",
                            null,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
//                                        JSONArray jsonArray = new JSONArray(response);
//                                        JsonParser parser = new JsonParser();
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject = (JSONObject) response.get(0);
                                        Log.i("json response",jsonObject.get("lon").toString());
                                        Map<String,String> locationCoordinates = new HashMap<String,String>();

                                        locationCoordinates.put("coordinates",String.valueOf(jsonObject.get("lat").toString())+","+String.valueOf(jsonObject.get("lon").toString()));
                                      Uri gmmIntentUri = Uri.parse("google.navigation:q="+locationCoordinates.get("coordinates").toString());
                                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                        mapIntent.setPackage("com.google.android.apps.maps");

                                        List<String>locations = Arrays.asList(mContext.getResources().getStringArray(R.array.location));
                                        Activity mActivity = (Activity) mContext;
                                        mActivity.finish();
                                        mContext.startActivity(mapIntent);
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
            });
        }
    }


    public ScheduleListAdapter(List<ScheduleList> scheduleList) {
        this.scheduleList = scheduleList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ScheduleList schedule = scheduleList.get(position);
        holder.activityName.setText(schedule.getActivityName());
        String timeslot= schedule.getTimeSlot();
        String temp = timeslot.substring(0,2);
        timeslot = temp +":00-" + timeslot.substring(2,4) +":00";
        holder.startTime.setText(timeslot);
        holder.duration.setText(String.valueOf(schedule.getDuration()) +" hour");
        holder.location.setText(schedule.getLocation());
        holder.tvActivityName.setText(schedule.getActivityName());
        String colourName = schedule.getActivityType() +"type";
        // holder.relativeLayout.getBackground().setColorFilter(mContext.getResources().getColor(R.color.badges_color), PorterDuff.Mode.DARKEN);
        if(schedule.isSchedule()){
            holder.btnReject.setVisibility(View.GONE);
            holder.btnAccept.setVisibility(View.GONE);
        }else{
            holder.btnReject.setVisibility(View.VISIBLE);
            holder.btnAccept.setVisibility(View.VISIBLE);
        }

        switch (schedule.getActivityType()){
            case "Class" :
                holder.imageView.setImageResource(R.drawable.ic_class);
                break;
            case "Gym" :
                holder.imageView.setImageResource(R.drawable.ic_gym);
                break;
            case "Music" :
                holder.imageView.setImageResource(R.drawable.ic_music);
                break;
            case "Drama" :
                holder.imageView.setImageResource(R.drawable.ic_drama);
                break;
            case "Dancing" :
                holder.imageView.setImageResource(R.drawable.ic_dance);
                break;
            default:
                holder.imageView.setImageResource(R.drawable.ic_profile_24dp);
                break;

        }


    }


    @Override
    public int getItemCount() {
        return scheduleList.size();
    }
}
