package com.example.blockchainapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Tracking extends AppCompatActivity {

    private TextView Output;
    private String History = "";
    private RadioButton RedRadioButton;
    private RadioButton BlueRadioButton;
    private Button Scan;
    private String url;
    private String credentials;
    private String UUIDv4;
    private Button Login;
    private ScrollView SVHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        Scan = (Button) findViewById(R.id.btScan);
        Output = (TextView) findViewById(R.id.output);
        RedRadioButton = (RadioButton) findViewById(R.id.rbRed);
        BlueRadioButton = (RadioButton) findViewById(R.id.rbBlue);
        Login = (Button) findViewById(R.id.btnLogin);
        SVHistory = (ScrollView) findViewById(R.id.svHistory);
        SVHistory.setVisibility(View.INVISIBLE);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tracking.this, Login.class);
                startActivity(intent);
            }
        });


        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RedRadioButton.isChecked()){
                    url = "http://192.168.0.6:8081/parcel/history?id=";
                    credentials = "user0@red.parcels.local" + ":" + "password";

                }
                else if(BlueRadioButton.isChecked()){
                    url = "http://192.168.0.6:8080/parcel/history?id=";
                    credentials = "user0@blue.parcels.local" + ":" + "password";
                }
                IntentIntegrator intentIntegrator = new IntentIntegrator(Tracking.this);
                intentIntegrator.setPrompt("W celu włączenia latarki wciśnij przycisk zwiększania głośności");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                intentIntegrator.initiateScan();

            }
        });


        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Tracking.this);
            builder.setTitle("Zeskanowany kod to:");
            builder.setMessage(intentResult.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
            UUIDv4 = intentResult.getContents();
            SVHistory.setVisibility(View.VISIBLE);
            QueryPackage();


        }else{
            Toast.makeText(getApplicationContext(), "Ponów próbę skanowania", Toast.LENGTH_SHORT).show();
        }

    }

        void QueryPackage(){

            url = url + UUIDv4;
            RequestQueue queue = Volley.newRequestQueue(this);

            // Request a string response from the provided URL.
            JsonArrayRequest JsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jresponse = response.getJSONObject(i);
                                    if(i==0){
                                        History += "Identyfikator: \n";
                                        History += UUIDv4 + "\n\n";
                                        History += i+1;
                                    }
                                    else{
                                        int j=i+1;
                                        History += "\n\n"+j+".";
                                    }

                                    String empty = "---";
                                    History += "\nCzas: \n";
                                    if(jresponse.getString("timestamp").isEmpty()){
                                        History += empty;
                                    }
                                    else{
                                        long dv = Long.valueOf(jresponse.getString("timestamp"))*1000;// its need to be in milisecond
                                        Date df = new java.util.Date(dv);
                                        String vv = new SimpleDateFormat("EEEE, dd MMM yyyy, HH:mm:ss", new Locale("pl")).format(df);
                                        History += vv;
                                    }
                                    History += "\nStatus: \n";
                                    if(jresponse.getString("status").isEmpty()){
                                        History += empty;
                                    }
                                    else{
                                        History += jresponse.getString("status");
                                    }
                                    History += "\nOpiekun: \n";
                                    if(jresponse.getString("keeper").isEmpty()){
                                        History += empty;
                                    }
                                    else{
                                        History += jresponse.getString("keeper");
                                    }
                                    History += "\nOrganizacja opiekuna: \n";
                                    if(jresponse.getString("keeper_organization").isEmpty()){
                                        History += empty;
                                    }
                                    else{
                                        History += jresponse.getString("keeper_organization");
                                    }
                                    History += "\nEtykieta opiekuna: \n";
                                    if(jresponse.getString("keeper_label").isEmpty()){
                                        History += empty;
                                    }
                                    else{
                                        History += jresponse.getString("keeper_label");
                                    }
                                    Output.setText(History);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Nie można odczytać informacji o paczce. Spróbuj ponownie później", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Tracking.this, Login.class);
                    startActivity(intent);
                }
            }){

                //This is for Headers If You Neededre
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("token", ACCESS_TOKEN);
                    String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    params.put("Authorization", "Basic " + base64EncodedCredentials);
                    //Test.setText("No");
                    return params;
                }

                //Pass Your Parameters here
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("id", f59d9eda-423d-40f7-ac29-2c7821723003);
//                    params.put("Pass", userPassword);
//                    return params;
//                }
            };


            // Add the request to the RequestQueue.
            queue.add(JsonArrayRequest);
            //SystemClock.sleep(3000);
            //(userName.equals("")) && (userPassword.equals(""))


        }


}