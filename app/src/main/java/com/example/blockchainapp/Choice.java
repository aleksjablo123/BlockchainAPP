package com.example.blockchainapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Choice extends AppCompatActivity implements View.OnClickListener{

    private Button Sorting;
    private Button Carrier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        Sorting = (Button) findViewById(R.id.btnSorting);
        Carrier = (Button) findViewById(R.id.btnCarrier);

        Sorting.setOnClickListener(this);
        Carrier.setOnClickListener(this);

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