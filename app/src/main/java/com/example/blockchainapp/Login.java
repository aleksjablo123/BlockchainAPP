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
import android.widget.Switch;
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

public class Login extends AppCompatActivity {
    String custom_response;
    boolean success;
    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private Switch Blue;
    private TextView Test;
    private int counter = 5;

    //@TODO pokazanie że to zmienna globalna
    public String Organization = "Red";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Name = (EditText) findViewById(R.id.etName);
        Password = (EditText) findViewById(R.id.etPassword);
        Info = (TextView) findViewById(R.id.tvInfo);
        Login = (Button) findViewById(R.id.btnLogin);
        Blue = (Switch) findViewById(R.id.swBlue);
        Test = (TextView) findViewById(R.id.tvTest);
        Info.setText("Liczba dostępnych prób logowania: " + String.valueOf(counter));

        Blue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Blue.setText("Organizacja niebieska");
                    Blue.setTextColor(Color.BLUE);
                    Organization="Blue";
                }
                else{
                    Blue.setText("Organizacja czerwona");
                    Blue.setTextColor(Color.RED);
                    Organization="Red";
                }
            }
        });



        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });
    }

    private void validate (String userName, String userPassword){

        if(Organization=="Blue"){
            //logowanie organizacji niebieskiej

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://192.168.0.6:8080/user";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //Test.setText("Response is: "+ response.substring(0,500));
//                            success = true;
//                            if(success){
                            Log.i("Response1", response);
                                Intent intent = new Intent(Login.this, Choice.class);
                                startActivity(intent);
//                            }else{
//                                counter--;
//
//                                Info.setText("Liczba dostępnych prób logowania: " + String.valueOf(counter));
//
//                                if(counter == 0){
//                                    Login.setEnabled(false);
//                                }
//                            }
                            //custom_response = response.substring(0,500);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //@TODO in future - sprawdzenie czy błąd logowania jest po stronie serwera czy ze względu na błędne dane
                    Test.setText(error.toString());
                    if (error instanceof AuthFailureError) {
                        counter--;

                        Info.setText("Liczba dostępnych prób logowania: " + String.valueOf(counter));

                        if (counter == 0) {
                            Login.setEnabled(false);
                        }
                    } else {
                        //bład serwera spórobuj później
                        //możliwe do wykorzystaina error instanceof ServerError i NetworkError
                    }
                }
            }){

                //This is for Headers If You Needed
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

//                    params.put("token", ACCESS_TOKEN);
                    String credentials = userName + ":" + userPassword;
                    // "user0@blue.parcels.local:password"
                    String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    params.put("Authorization", "Basic " + base64EncodedCredentials);
                    //Test.setText("No");
                    return params;
                }

                //Pass Your Parameters here
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("User", userName);
//                    params.put("Pass", userPassword);
//                    return params;
//                }
            };


            // Add the request to the RequestQueue.
            queue.add(stringRequest);
            //SystemClock.sleep(3000);
        //(userName.equals("")) && (userPassword.equals(""))


        }
        else{
            int port = 8081;
            String url = "192.168.0.6/user";
            if((userName.equals("")) && (userPassword.equals(""))){
                Intent intent = new Intent(Login.this, Choice.class);
                startActivity(intent);
            }else{
                counter--;

                Info.setText("Liczba dostępnych prób logowania: " + String.valueOf(counter));

                if(counter == 0){
                    Login.setEnabled(false);
                }
            }
        }

    }

}