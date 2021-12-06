package com.example.blockchainapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
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


public class CarrierScan extends AppCompatActivity {

    private Button Scan;
    private TextView UUIDv4;
    private TextView UUIDv4Read;
    private Button Save;
    private Switch Delivered;
    private Switch Picked;
    private TableLayout Data;
    public boolean PickedBoolean;
    public boolean DeliveredBoolean;
    private TextView OrganizationShow1;

    private TextView OrganizationRead;
    private TextView StatusRead;
    private TextView KeeperRead;
    private TextView KeeperOrganizationRead;
    private TextView KeeperLabelRead;
    private TextView Pickup_addressRead;
    private TextView Delivery_addressRead;
    private TextView WeightRead;
    private TextView HeightRead;
    private TextView WidthRead;
    private TextView LengthRead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrier_scan);

        Scan = findViewById(R.id.btScan);
        UUIDv4Read = findViewById(R.id.tvUuidv4);
        UUIDv4Read = findViewById(R.id.tvUuidv4Read);
        Save = findViewById(R.id.btnSave);
        Data = findViewById(R.id.tlData);
        OrganizationShow1 = findViewById(R.id.tvOrganizationShow1);
        Delivered = findViewById(R.id.swDelivered);
        Delivered.setVisibility(View.INVISIBLE);
        Picked = findViewById(R.id.swPicked);
        Picked.setVisibility(View.INVISIBLE);
        Data.setVisibility(View.INVISIBLE);
        UUIDv4Read.setText("");
        Save.setVisibility(View.INVISIBLE);

        //inicjalizacja wierszy z tablicy
        OrganizationRead = findViewById(R.id.tvOrganizationRead);
        StatusRead = findViewById(R.id.tvStatusRead);
        KeeperRead = findViewById(R.id.tvKeeperRead);
        KeeperOrganizationRead = findViewById(R.id.tvKeeperOrganizationRead);
        Pickup_addressRead = findViewById(R.id.tvPickup_addressRead);
        Delivery_addressRead = findViewById(R.id.tvDelivery_addressRead);
        WeightRead = findViewById(R.id.tvWeightRead);
        HeightRead = findViewById(R.id.tvHeightRead);
        WidthRead = findViewById(R.id.tvWidthRead);
        LengthRead = findViewById(R.id.tvLengthRead);
        KeeperLabelRead = findViewById(R.id.tvKeeperLabelRead);

        if(Login.Organization=="Blue"){
            OrganizationShow1.setText("Niebieska organizacja!");
            OrganizationShow1.setTextColor(ContextCompat.getColor(this, R.color.blue));
        }
        else{
            OrganizationShow1.setText("Czerwona organizacja!");
            OrganizationShow1.setTextColor(ContextCompat.getColor(this, R.color.red));
        }

        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(CarrierScan.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(CarrierScan.this);
            builder.setTitle("Zeskanowany kod to:");
            builder.setMessage(intentResult.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
            UUIDv4Read.setText(intentResult.getContents());
            ShowNextPointOption();
            ReadFromBlockchain(UUIDv4Read.getText().toString());

        }else{
            Toast.makeText(getApplicationContext(), "Ponów próbę skanowania", Toast.LENGTH_SHORT).show();
        }
    }


    protected void ShowNextPointOption(){
        Save.setEnabled(false);
        Save.setVisibility(View.VISIBLE);
        Data.setVisibility(View.VISIBLE);
        Delivered.setVisibility(View.VISIBLE);
        Picked.setVisibility(View.VISIBLE);






        Delivered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Save.setEnabled(true);
                    DeliveredBoolean = true;
                    Picked.setEnabled(false);
                }else{
                    Save.setEnabled(false);
                    DeliveredBoolean = false;
                    Picked.setEnabled(true);
                }
            }
        });

        Picked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Save.setEnabled(true);
                    PickedBoolean = true;
                    Delivered.setEnabled(false);
                }else{
                    Save.setEnabled(false);
                    PickedBoolean = false;
                    Delivered.setEnabled(true);
                }
            }

        });


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PickedBoolean){
                    SendToBlockchain("Picked", UUIDv4Read.getText().toString());
                }else{
                    SendToBlockchain("Delivered", UUIDv4Read.getText().toString());
                }

            }
        });
    }
    protected void SendToBlockchain(String status, String Id){
        String url;
        if(Login.Organization=="Blue"){
            url = Login.BlueURL;
        }
        else{
            url = Login.RedURL;
        }
        if (status == "Picked"){
            url = url + "/parcel/pickup?id=" + Id;
        }
        else if(status == "Delivered"){
            url = url + "/parcel/deliver?id=" + Id;
        }
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest JsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //JSONObject jsonOb = response;
                        //dobry kod
                        Intent intent = new Intent(CarrierScan.this, Choice.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent = new Intent(CarrierScan.this, Choice.class);
                startActivity(intent);
//                Intent intent = new Intent(CarrierScan.this, Choice.class);
//                startActivity(intent);
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
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("id", Id);
//                    //params.put("Pass", userPassword);
//                    return params;
//                }
        };

        queue.add(JsonObjectRequest);

    }
    protected void ReadFromBlockchain(String Id){
        String url;
        if(Login.Organization=="Blue"){
            url = Login.BlueURL;
        }
        else{
            url = Login.RedURL;
        }
        url = url + "/parcel?id=" + Id;

        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        JsonObjectRequest JsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonOb = response;
                        //parsowanie danych
                        try {
                            String empty="---";
                            String Organization = jsonOb.getString("organization");
                            if(Organization.isEmpty()){
                                OrganizationRead.setText(empty);
                            }
                            else{
                                OrganizationRead.setText(Organization);
                            }
                            String Status = jsonOb.getString("status");
                            if(Status.isEmpty()){
                                StatusRead.setText(empty);
                            }
                            else{
                                StatusRead.setText(Status);
                            }
                            String Keeper = jsonOb.getString("keeper");
                            if(Keeper.isEmpty()){
                                KeeperRead.setText(empty);
                            }
                            else{
                                KeeperRead.setText(Keeper);
                            }
                            String KeeperOrganization = jsonOb.getString("keeper_organization");
                            if(KeeperOrganization.isEmpty()){
                                KeeperOrganizationRead.setText(empty);
                            }
                            else{
                                KeeperOrganizationRead.setText(KeeperOrganization);
                            }
                            String KeeperLabel = jsonOb.getString("keeper_label");
                            if(KeeperLabel.isEmpty()){
                                KeeperLabelRead.setText(empty);
                            }
                            else{
                                KeeperLabelRead.setText(KeeperLabel);
                            }
                            String Pickup_address = jsonOb.getString("pickup_address");
                            if(Pickup_address.isEmpty()){
                                Pickup_addressRead.setText(empty);
                            }
                            else{
                                Pickup_addressRead.setText(Pickup_address);
                            }
                            String Delivery_address = jsonOb.getString("delivery_address");
                            if(Delivery_address.isEmpty()){
                                Delivery_addressRead.setText(empty);
                            }
                            else{
                                Delivery_addressRead.setText(Delivery_address);
                            }
                            String Weight = jsonOb.getString("weight");
                            if(Weight.isEmpty()){
                                WeightRead.setText(empty);
                            }
                            else{
                                WeightRead.setText(Weight);
                            }
                            String Height = jsonOb.getString("height");
                            if(Height.isEmpty()){
                                HeightRead.setText(empty);
                            }
                            else{
                                HeightRead.setText(Height);
                            }
                            String Width = jsonOb.getString("width");
                            if(Width.isEmpty()){
                                WidthRead.setText(empty);
                            }
                            else{
                                WidthRead.setText(Width);
                            }
                            String Length = jsonOb.getString("length");
                            if(Length.isEmpty()){
                                LengthRead.setText(empty);
                            }
                            else{
                                LengthRead.setText(Length);
                            }
                        }
                        catch (Exception w){

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent = new Intent(CarrierScan.this, Choice.class);
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
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("id", "7f0839c8-1725-4d81-977b-cd291ac834f4");
//                    //params.put("Pass", userPassword);
//                    return params;
//                }
        };

        queue.add(JsonObjectRequest);




    }

}