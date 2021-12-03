package com.example.blockchainapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private Switch Blue;
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
            int port = 8080;
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