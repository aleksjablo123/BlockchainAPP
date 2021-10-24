package com.example.blockchainapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class CarrierScan extends AppCompatActivity {

    private Button Scan;
    private TextView UUIDv4;
    private Button Save;
    private Switch Delivered;
    private TableLayout Data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrier_scan);

        Scan = findViewById(R.id.btScan);
        UUIDv4 = findViewById(R.id.tvUuidv4);
        Save = findViewById(R.id.btnSave);
        Data = findViewById(R.id.tlData);
        Delivered = findViewById(R.id.swDelivered);
        Delivered.setVisibility(View.INVISIBLE);
        Data.setVisibility(View.INVISIBLE);
        UUIDv4.setText("");
        Save.setVisibility(View.INVISIBLE);

        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(CarrierScan.this);
                intentIntegrator.setPrompt("For flash use volume up key");
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
            UUIDv4.setText(intentResult.getContents());
            ShowNextPointOption();

        }else{
            Toast.makeText(getApplicationContext(), "You did not scan anything", Toast.LENGTH_SHORT).show();
        }
    }


    protected void ShowNextPointOption(){
        Save.setEnabled(false);
        Save.setVisibility(View.VISIBLE);

        Data.setVisibility(View.VISIBLE);
        Delivered.setVisibility(View.VISIBLE);

        Delivered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Save.setEnabled(true);
                }else{
                    Save.setEnabled(false);
                }
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //@TODO
                //wysyłaj dane do blockchain
                Intent intent = new Intent(CarrierScan.this, Choice.class);
                startActivity(intent);
            }
        });
    }



}