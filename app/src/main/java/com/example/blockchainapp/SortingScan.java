package com.example.blockchainapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SortingScan extends AppCompatActivity {
    String[] elements;

    {
        elements = new String[]{"Warszawa, WAW1", "Kraków, KRK2", "Poznań, PZN3", "Gdańsk, GDY4", "Rzeszów, RZE5"};
    }

    private Button Scan;
    private TextView UUIDv4;
    private TextView Choose;
    private Button Save;
    private Spinner Spinner;
    private TextView OrganizationShow;
    private TextView tvuidv4label;
    private String Sorting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting_scan);

        Scan = findViewById(R.id.btScan);
        UUIDv4 = findViewById(R.id.tvUuidv4);
        Choose = findViewById(R.id.tvChoose);
        Save = findViewById(R.id.btnSave);
        Spinner = findViewById(R.id.sprChooseNextStep);
        OrganizationShow = findViewById(R.id.tvOrganizationShow);
        tvuidv4label = findViewById(R.id.tvuidv4label);
        tvuidv4label.setVisibility(View.INVISIBLE);
        UUIDv4.setVisibility(View.INVISIBLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, elements);
        Spinner.setAdapter(adapter);
        UUIDv4.setText("");
        Choose.setVisibility(View.INVISIBLE);
        Save.setVisibility(View.INVISIBLE);
        Spinner.setVisibility(View.INVISIBLE);

        if(Login.Organization=="Blue"){
            OrganizationShow.setText("Niebieska organizacja!");
            OrganizationShow.setTextColor(ContextCompat.getColor(this, R.color.blue));
        }
        else{
            OrganizationShow.setText("Czerwona organizacja!");
            OrganizationShow.setTextColor(ContextCompat.getColor(this, R.color.red));
        }
        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(SortingScan.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(SortingScan.this);
            builder.setTitle("Zeskanowany kod to:");
            builder.setMessage(intentResult.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
            UUIDv4.setText(intentResult.getContents());
            ShowNextPointOption();

        }else{
            Toast.makeText(getApplicationContext(), "Ponów próbę skanowania", Toast.LENGTH_SHORT).show();
        }
    }


    protected void ShowNextPointOption(){
        Choose.setVisibility(View.VISIBLE);
        Save.setVisibility(View.VISIBLE);
        Spinner.setVisibility(View.VISIBLE);

        UUIDv4.setVisibility(View.VISIBLE);
        tvuidv4label.setVisibility(View.VISIBLE);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sorting = Spinner.getSelectedItem().toString();
                SendToBlockchain(Sorting,UUIDv4.getText().toString());
            }
        });
    }


    protected void SendToBlockchain(String Keeper, String Id){
        String url;
        if(Login.Organization=="Blue"){
            url = Login.BlueURL;
        }
        else{
            url = Login.RedURL;
        }
        url = url + "/parcel/sort?id=" + Id;

        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest JsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //JSONObject jsonOb = response;
                        Toast.makeText(getApplicationContext(), "Informacje o paczce zostały poprawnie zaktualizowane", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SortingScan.this, Choice.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Informacje o paczce nie zostały poprawnie zaktualizowane. Spróbuj ponownie później", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SortingScan.this, Choice.class);
                startActivity(intent);
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String base64EncodedCredentials = Base64.encodeToString(Login.Authorization.getBytes(), Base64.NO_WRAP);
                params.put("Authorization", "Basic " + base64EncodedCredentials);
                return params;
            }

            //Pass Your Parameters here
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("keeper_label", Sorting);
                    //params.put("Pass", userPassword);
                    return params;
                }
        };

        queue.add(JsonObjectRequest);


    }



}