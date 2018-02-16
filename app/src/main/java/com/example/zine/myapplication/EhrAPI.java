package com.example.zine.myapplication;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//Volley library
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;

public class EhrAPI extends Activity{
    private final static String TAG = "EhrAPI";
    private EditText client_id, client_secret, grant_type, username, password, task_group;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        submit = (Button) findViewById(R.id.btn);
        client_id = (EditText)findViewById(R.id.edittext1);
        client_secret = (EditText)findViewById(R.id.edittext2);
        grant_type = (EditText)findViewById(R.id.edittext3);
        username = (EditText)findViewById(R.id.edittext4);
        password = (EditText)findViewById(R.id.edittext5);
        task_group = (EditText)findViewById(R.id.edittext6);
        final RequestQueue  requestqueue = Volley.newRequestQueue(this);
        final RequestQueue queue = Volley.newRequestQueue(this);
        final RequestQueue queue1 = Volley.newRequestQueue(this);
        final RequestQueue queue2 = Volley.newRequestQueue(this);
        final RequestQueue queue3 = Volley.newRequestQueue(this);
        final RequestQueue queue4 = Volley.newRequestQueue(this);
        final TextView ResponseToken = (TextView) findViewById(R.id.areaToken);
        final String patient_id = "c6ijf5cru3nmnesuuid52gkcvbodek2o";


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //the app makes a 'POST' request to the server with the data
               
                StringRequest req = new StringRequest(Request.Method.POST, "URL",
                        new Response.Listener<String>(){

                            @Override
                            public void onResponse(final String response) {
                                //The server sends back a kind of response to let the android application know that it got the data
                                if (response != null){
                                    try{
                                        //Parse data
                                        JSONObject obj = new JSONObject(response);
                                        final String token = obj.getString("access_token");
                                        final int expires_in = obj.getInt("expires_in");
                                        final String scope = obj.getString("scope");
                                        final String token_type = obj.getString("token_type");
                                        ResponseToken.setText("Successfully Access !! \n"+ "access_token : "+token+"\nexpires_in : "+expires_in+"\nscope : "+scope+"\ntoken_type : "+token_type);
                                        //Get EHR DBService status
                                        StringRequest GetRequest = new StringRequest(Request.Method.GET, "URL", new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(final String response) {
                                                Log.d(TAG, response);
                                                //Create patient record
                                                StringRequest sr = new StringRequest(Request.Method.POST,"URL", new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.d(TAG, response);
                                                        //Create the EHR for the patient
                                                        StringRequest sr1 = new StringRequest(Request.Method.POST,"URL", new    Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                Log.d(TAG, response);
                                                                //Add a record to the EHR
                                                                // POST parameters: should be json request
                                                                JSONObject json = new JSONObject();
                                                                try {
                                                                    JSONObject jsonobject = new JSONObject();
                                                                    jsonobject.put("at0001", "val1");
                                                                    jsonobject.put("at0002", "val2");
                                                                    JSONObject jsonObject = new JSONObject();
                                                                    jsonobject.put("archetype_class", "openEHR.TEST-EVALUATION.v1");
                                                                    jsonobject.put("archetype_details", jsonobject);
                                                                    json.put("", jsonobject);

                                                                }catch (JSONException error){
                                                                    error.printStackTrace();
                                                                }
                                                                JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST,String.format("URL", patient_id), json, new Response.Listener<JSONObject>()
                                                                        {
                                                                            @Override
                                                                            public void onResponse(JSONObject response)
                                                                            {
                                                                                Log.d(TAG, String.valueOf(response));

                                                                                //Check new EHR entry for current medical records patient
                                                                                StringRequest  newEntry = new StringRequest(Request.Method.GET, String.format("URL", patient_id),
                                                                                        new Response.Listener<String>() {
                                                                                    @Override
                                                                                    public void onResponse(String response)
                                                                                    {
                                                                                        //return the number of records, how ??
                                                                                        Log.d(TAG, "number of records: %s\n"+ response);


                                                                                    }
                                                                                },
                                                                                        new Response.ErrorListener()
                                                                                        {
                                                                                            @Override
                                                                                            public void onErrorResponse(VolleyError error)
                                                                                            {
                                                                                                Log.d(TAG, error.toString());
                                                                                            }
                                                                                        }){
                                                                                    @Override
                                                                                    protected Map<String,String> getParams() {
                                                                                        Map<String, String> params = new HashMap<>();
                                                                                        params.put("access_token", token);
                                                                                        return params;
                                                                                    }
                                                                                };

                                                                                // Add the request to the RequestQueue.
                                                                                queue4.add(newEntry);



                                                                            }
                                                                        },
                                                                                new Response.ErrorListener()
                                                                                {
                                                                                    @Override
                                                                                    public void onErrorResponse(VolleyError error)
                                                                                    {
                                                                                        Log.d(TAG, error.toString());
                                                                                    }
                                                                                }){
                                                                    @Override
                                                                    protected Map<String,String> getParams() {
                                                                        Map<String, String> params = new HashMap<>();
                                                                        params.put("access_token", token);
                                                                        return params;
                                                                    }
                                                                    };

                                                                // Add the request to the RequestQueue.
                                                                queue3.add(jsonObjRequest);
                                                            }
                                                        }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                Log.d(TAG, error.toString());
                                                            }
                                                        }){
                                                            @Override
                                                            protected Map<String,String> getParams(){
                                                                Map<String,String> params = new HashMap<>();
                                                                params.put("access_token", token);
                                                                return params;
                                                            }

                                                            @Override
                                                            public Map<String, String> getHeaders() throws AuthFailureError {
                                                                Map<String,String> header = new HashMap<>();
                                                                header.put("Content-Type","application/json");
                                                                return header;
                                                            }
                                                        };
                                                        queue2.add(sr1);

                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.d(TAG, error.toString());

                                                    }
                                                }){
                                                    @Override
                                                    protected Map<String,String> getParams(){
                                                        Map<String,String> params = new HashMap<>();
                                                        params.put("access_token", token);
                                                        params.put("demographic_uuid", "abc001");
                                                        return params;
                                                    }

                                                    @Override
                                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                                        Map<String,String> header = new HashMap<>();
                                                        header.put("Content-Type","application/json");
                                                        return header;
                                                    }
                                                };
                                                queue1.add(sr);



                                            }

                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d(TAG, error.toString());
                                            }
                                        })
                                        {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError{
                                                Map<String, String> param = new HashMap<>();
                                                param.put("access_token", token);
                                                return param;
                                            }
                                            @Override
                                            public Map<String, String> getHeaders() throws AuthFailureError {
                                                HashMap<String, String> header = new HashMap<>();
                                                header.put("Content-Type", "application/json");
                                                return header;
                                            }
                                        };
                                        // Add the request to the RequestQueue.
                                        queue.add(GetRequest);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }


                            }}, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Do what you want to do on error
                        Log.d(TAG, "Network error !!");
                        Log.d(TAG, error.toString());

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                    /*Post data*/
                        Map<String, String> argument = new HashMap<>();
                        argument.put("client_id", "8c96bf8cea26fa555fa8");
                        argument.put("client_secret", "4fd1f508b7b03fba6509da4c193157d7a2b20838");
                        argument.put("grant_type", "password");
                        argument.put("username", username.getText().toString()); //"admin"
                        argument.put("password", password.getText().toString()); // "admin"
                        argument.put("taskgroup", "5dw2x3jfkftxue5a5izw6yiplbbn4dlo");
                        return argument;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> header = new HashMap<>();
                        header.put("Content-Type", "application/json");
                        return header;
                    }

                };

                //Add request to queue
                requestqueue.add(req);


            }
        });
    }

}
