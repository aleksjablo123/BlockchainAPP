package com.example.blockchainapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.awt.font.TextAttribute;


public class Choice extends AppCompatActivity implements View.OnClickListener{

    private CardView Sorting;
    private CardView Carrier;
    private TextView Banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        Sorting = (CardView) findViewById(R.id.btnSorting);
        Carrier = (CardView) findViewById(R.id.btnCarrier);
        Banner = (TextView) findViewById(R.id.tvBanner);

        if(Login.Authorization.equals("user1@blue.parcels.local:password")){
            Carrier.setEnabled(false);
        }
        else if(Login.Authorization.equals("user2@blue.parcels.local:password")){
            Sorting.setEnabled(false);
        }
        else if(Login.Authorization.equals("user1@red.parcels.local:password")){
            Carrier.setEnabled(false);
        }
        else if(Login.Authorization.equals("user2@red.parcels.local:password")){
            Sorting.setEnabled(false);
        }


        Sorting.setOnClickListener(this);
        Carrier.setOnClickListener(this);

        if(Login.Organization=="Blue"){
            Banner.setText("Witaj w niebieskiej organizacji!");
            Banner.setTextColor(ContextCompat.getColor(this, R.color.blue));
        }
        else{
            Banner.setText("Witaj w czerwonej organizacji!");
            Banner.setTextColor(ContextCompat.getColor(this, R.color.red));
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSorting:
                Intent intent = new Intent(Choice.this, SortingScan.class);
                startActivity(intent);
                break;
            case R.id.btnCarrier:
                Intent intent1 = new Intent(Choice.this, CarrierScan.class);
                startActivity(intent1);
                break;
        }
    }
}