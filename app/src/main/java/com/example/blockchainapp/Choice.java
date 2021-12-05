package com.example.blockchainapp;

import androidx.appcompat.app.AppCompatActivity;
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

    private Button Sorting;
    private Button Carrier;
    private ConstraintLayout mConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        Sorting = (Button) findViewById(R.id.btnSorting);
        Carrier = (Button) findViewById(R.id.btnCarrier);
        mConstraintLayout = (ConstraintLayout)findViewById(R.id.constraintLayout);

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
            mConstraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        }
        else{
            mConstraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
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