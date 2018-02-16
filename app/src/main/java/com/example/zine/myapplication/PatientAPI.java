package com.example.zine.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class PatientAPI extends Activity implements View.OnClickListener {
    private final static String TAG = "PatientAPI";
    TextView viewT, viewB;
    Intent intent = null;
    String patient_id, token = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        viewT = (TextView) findViewById(R.id.response1);
        viewB = (TextView) findViewById(R.id.response2);
        intent = getIntent();
        token = intent.getStringExtra("access_token");
        patient_id = "c6ijf5cru3nmnesuuid52gkcvbodek2o";
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this); // calling onClick() method
        Button btn_retrieve = (Button) findViewById(R.id.btnretrieve);
        btn_retrieve.setOnClickListener(this);
        Button btn_update = (Button) findViewById(R.id.btnupdate);
        btn_update.setOnClickListener(this);
        Button btn_delete = (Button) findViewById(R.id.btndelete);
        btn_delete.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn:
                //create new patient
                RequestQueue queue1 = Volley.newRequestQueue(this);
                StringRequest CreatePatient = new StringRequest(Request.Method.POST,
                        "URL",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the response string
                                viewT.setText(response);
                                Log.d(TAG, response);
                                Log.d("token : ", token);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                                viewT.setText("Failed to create new patient !!");
                                Log.d(TAG, error.toString());
                    }})
                {

                    //adding parameters to the request
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("access_token", token);
                        params.put("demographic_uuid", "afbab3d293b643833d35cb972f26642cf77f1db4");
                        return params;
                    }

                };
                // Add the request to the RequestQueue.
                queue1.add(CreatePatient);

                break;

            case R.id.btnretrieve:
                //Retrieve all patients for task_group
                final RequestQueue queue2 = Volley.newRequestQueue(this);
                StringRequest RetrievePatient = new StringRequest(Request.Method.GET,
                        "URL",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the response string.
                                viewB.setText(response);
                                Log.d(TAG, response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                                viewB.setText("Failed to retrieve patient !!");
                                Log.d(TAG, error.toString());
                    }})
                {

                    //adding parameters to the request
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("access_token", token);
                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue2.add(RetrievePatient);
                break;

            case R.id.btnupdate:
                final RequestQueue queue3 = Volley.newRequestQueue(this);
                StringRequest UpdatePatient = new StringRequest(Request.Method.PUT,
                        String.format("URL", patient_id),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the response string.
                                viewB.setText(response);
                                Log.d(TAG, response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                               viewB.setText("Failed to up_date patient !!");
                               Log.d(TAG, error.toString());
                    }})
                {

                    //adding parameters to the request
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("access_token", token);
                        params.put("demographic_uuid", "a");
                        //data ??
                        params.put("data", "");
                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue3.add(UpdatePatient);
                break;

            case R.id.btndelete:
                final RequestQueue queue4 = Volley.newRequestQueue(this);
                StringRequest DeletePatient = new StringRequest(Request.Method.DELETE,
                        String.format("URL", patient_id),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the response string.
                                viewB.setText(response);
                                Log.d(TAG, response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        viewB.setText("Failed to delete patient !!");
                        Log.d(TAG, error.toString());
                    }})
                {

                    //adding parameters to the request
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("access_token", token);
                        //data ??
                        params.put("data", "");
                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue4.add(DeletePatient);
                break;

            default:
                break;


        }

    }
}
