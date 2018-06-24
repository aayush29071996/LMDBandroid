package com.example.aayush.lmdb;

import android.content.Context;
import android.os.AsyncTask;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class inActivity extends AppCompatActivity {


    private static final String TAG ="ERROR" ;
    Button b1;
    Button b2;
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        final RequestQueue MyRequestQueue = Volley.newRequestQueue(context);






        b1 = (Button)findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final sqlite q1 = new sqlite(context, "/sdcard/Android");


                String text = "First Event";
                String timeStamp = getCurrentTimeStamp();


                long a =   q1.save1(text,timeStamp);

                Log.e(TAG, String.valueOf(a));



                new eventsCheckPost().execute();

            }


        });





        b2 = (Button)findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final sqlite q2 = new sqlite(context, "/sdcard/Android");

                String text = "First Trip";
                String timeStamp = getCurrentTimeStamp();

                long a =    q2.save3(text,timeStamp);

                Log.e(TAG, String.valueOf(a));



             new tripsCheckPost().execute();

            }

        });


    }




    private class eventsCheckPost extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
            final sqlite q3 = new sqlite(context, "/sdcard/Android");
            ArrayList<String> event_list = new ArrayList<String>();
            event_list =  q3.getEvents();
            for(int i=0; i<=event_list.size()-2;i+=3){

                //POST TO THE SERVER


                JSONObject jsonBodyObj = new JSONObject();
                try{
                    jsonBodyObj.put("ID_EVENT", event_list.get(i));

                    jsonBodyObj.put("Event", event_list.get(i+1));

                    jsonBodyObj.put("TimeStamp", event_list.get(i+2));




                }catch (JSONException e){
                    e.printStackTrace();
                }
                final String requestBody = jsonBodyObj.toString();
                String url = "http://172.31.99.244:8083/event";

                JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST,
                        url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("REQUEST_JSON_TO_SERVER", "Success: " + response.toString(4));
                            String  r1 = response.optString("ID_EVENT");
                            Log.e(TAG,r1);

                            q3.save2(r1);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("REQUEST_JSON_TO_SERVER", "Error: " + error);
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }

                    @Override
                    public byte[] getBody() {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                    requestBody, "utf-8");
                            return null;
                        }
                    }

                };

                MyRequestQueue.add(JOPR);

            }

            ArrayList<String> timestamp_list = new ArrayList<String>();
            timestamp_list =  q3.getEventsTimestamp();

            for(int i=0; i<=timestamp_list.size()-1;i++){

                //POST TO THE SERVER


                JSONObject jsonBodyObj = new JSONObject();
                try{


                    jsonBodyObj.put("TimeStamp", timestamp_list.get(i));




                }catch (JSONException e){
                    e.printStackTrace();
                }
                final String requestBody = jsonBodyObj.toString();
                String url = "http://172.31.99.244:8083/eventTimestamp";

                JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST,
                        url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("REQUEST_JSON_TO_SERVER", "Success: " + response.toString(4));
                            String  r1 = response.optString("TIMESTAMP");
                            Log.e(TAG,r1);

                            q3.delete1(r1);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("REQUEST_JSON_TO_SERVER", "Error: " + error);
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }

                    @Override
                    public byte[] getBody() {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                    requestBody, "utf-8");
                            return null;
                        }
                    }

                };

                MyRequestQueue.add(JOPR);

            }


            return null;
        }
    }

    private class tripsCheckPost extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
            final sqlite q4 = new sqlite(context, "/sdcard/Android");
            ArrayList<String> trip_list = new ArrayList<String>();
            trip_list =  q4.getTrips();
            for(int i=0; i<=trip_list.size()-2;i+=3){

                //POST TO THE SERVER


                JSONObject jsonBodyObj = new JSONObject();
                try{
                    jsonBodyObj.put("ID_TRIP", trip_list.get(i));

                    jsonBodyObj.put("Trip", trip_list.get(i+1));

                    jsonBodyObj.put("TimeStamp", trip_list.get(i+2));




                }catch (JSONException e){
                    e.printStackTrace();
                }
                final String requestBody = jsonBodyObj.toString();
                String url = "http://172.31.99.244:8083/trip";

                JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST,
                        url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("REQUEST_JSON_TO_SERVER", "Success: " + response.toString(4));
                            String  r1 = response.optString("ID_TRIP");
                            Log.e(TAG,r1);

                            q4.save4(r1);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("REQUEST_JSON_TO_SERVER", "Error: " + error);
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }

                    @Override
                    public byte[] getBody() {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                    requestBody, "utf-8");
                            return null;
                        }
                    }

                };

                MyRequestQueue.add(JOPR);

            }

            ArrayList<String> timestamp_list = new ArrayList<String>();
            timestamp_list =  q4.getTripsTimestamp();

            for(int i=0; i<=timestamp_list.size()-1;i++){

                //POST TO THE SERVER


                JSONObject jsonBodyObj = new JSONObject();
                try{


                    jsonBodyObj.put("TimeStamp", timestamp_list.get(i));




                }catch (JSONException e){
                    e.printStackTrace();
                }
                final String requestBody = jsonBodyObj.toString();
                String url = "http://172.31.99.244:8083/tripTimestamp";

                JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST,
                        url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("REQUEST_JSON_TO_SERVER", "Success: " + response.toString(4));
                            String  r1 = response.optString("TIMESTAMP");
                            Log.e(TAG,r1);

                            q4.delete2(r1);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("REQUEST_JSON_TO_SERVER", "Error: " + error);
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }

                    @Override
                    public byte[] getBody() {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                    requestBody, "utf-8");
                            return null;
                        }
                    }

                };

                MyRequestQueue.add(JOPR);

            }


            return null;
        }
    }


    // Getting the current time stamp for the file name.
    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }


}
