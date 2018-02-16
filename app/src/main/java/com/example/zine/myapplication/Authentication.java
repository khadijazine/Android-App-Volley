package com.example.zine.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//Volley library
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Authentication extends AppCompatActivity {
    private final static String TAG = "Authentication";
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
        final TextView ResponseToken = (TextView) findViewById(R.id.areaToken);



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
                                ResponseToken.setText("Successfully Access !! \n"+ "access_token : "+token+"\n" +
                                        "expires_in : "+expires_in+"\nscope : "+scope+"\ntoken_type : "+token_type);

                                //Test authenticated url
                                StringRequest GetRequest = new StringRequest(Request.Method.GET,
                                        "URL"+token,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(final String response) {
                                                Toast.makeText(Authentication.this, response, Toast.LENGTH_LONG).show();
                                                if(token != null){
                                                final Intent intent = new Intent(Authentication.this, PatientAPI.class);
                                                //send data with intent to another activity
                                                 intent.putExtra("access_token", token);
                                                 startActivity(intent);
                                                }

                                                                                          }
                                                                     },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d(TAG, "That didn't work!");
                                            }
                                        }) ;
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
                        Toast.makeText(Authentication.this, "Network error !!", Toast.LENGTH_SHORT).show();
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




