package com.example.blockchainapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    String custom_response;
    boolean success;
    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private TextView Test;
    private int counter = 5;
    private RadioButton BlueRadioButton;
    private RadioButton RedRadioButton;
    public static String Organization;
    public static String Authorization;
    public static String BlueURL="http://192.168.0.6:8080";
    public static String RedURL="http://192.168.0.6:8081";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Name = (EditText) findViewById(R.id.etName);
        Password = (EditText) findViewById(R.id.etPassword);
        Info = (TextView) findViewById(R.id.tvInfo);
        Login = (Button) findViewById(R.id.btnLogin);
        Test = (TextView) findViewById(R.id.tvTest);
        BlueRadioButton = (RadioButton) findViewById(R.id.rbBlue);
        RedRadioButton = (RadioButton) findViewById(R.id.rbRed);
        Test.setVisibility(View.INVISIBLE);
        Info.setText("Liczba dostępnych prób logowania: " + String.valueOf(counter));

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RedRadioButton.isChecked()){
                    Organization="Red";
                    validate(Name.getText().toString(), Password.getText().toString(), RedURL);
                }
                else if(BlueRadioButton.isChecked()){
                    Organization="Blue";
                    validate(Name.getText().toString(), Password.getText().toString(), BlueURL);
                }

            }
        });
    }

    private void validate (String userName, String userPassword, String url){

            RequestQueue queue = Volley.newRequestQueue(this);

            // Request a string response from the provided URL.
            JsonObjectRequest JsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url+"/user", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonOb = response;
                            Intent intent = new Intent(Login.this, Choice.class);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof AuthFailureError) {
                        Test.setVisibility(View.VISIBLE);
                        counter--;
                        Test.setTextColor(Color.RED);
                        Test.setText("Błędny login lub hasło");
                        Info.setText("Liczba dostępnych prób logowania: " + String.valueOf(counter));

                        if (counter == 0) {
                            Login.setEnabled(false);
                        }
                    } else {
                        Test.setVisibility(View.VISIBLE);
                        Test.setText("Błąd serwera, spróbuj zalogować się później");
                        Test.setTextColor(Color.RED);
                    }
                }
            }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    String credentials = userName + ":" + userPassword;
                    Authorization = credentials;
                    String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    params.put("Authorization", "Basic " + base64EncodedCredentials);
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

            queue.add(JsonObjectRequest);


    }

}