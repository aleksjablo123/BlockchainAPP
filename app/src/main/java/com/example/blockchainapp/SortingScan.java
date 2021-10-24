package com.example.blockchainapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class SortingScan extends AppCompatActivity {
    String[] elements;

    {
        elements = new String[]{"Warszawa, ul. Dolna 1", "Kraków, ul. Fajna 2", "Poznań, Smutna 3", "Gdańsk, Ciekawa 4", "Rzeszów, Markotna 5"};
    }

    private Button Scan;
    private TextView UUIDv4;
    private TextView Choose;
    private Button Save;
    private Spinner Spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting_scan);

        Scan = findViewById(R.id.btScan);
        UUIDv4 = findViewById(R.id.tvUuidv4);
        Choose = findViewById(R.id.tvChoose);
        Save = findViewById(R.id.btnSave);
        Spinner = findViewById(R.id.sprChooseNextStep);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, elements);
        Spinner.setAdapter(adapter);
        UUIDv4.setText("");
        Choose.setVisibility(View.INVISIBLE);
        Save.setVisibility(View.INVISIBLE);
        Spinner.setVisibility(View.INVISIBLE);

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
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //@TODO
                //wysyłaj dane do blockchain
                Intent intent = new Intent(SortingScan.this, Choice.class);
                startActivity(intent);
            }
        });
    }



}