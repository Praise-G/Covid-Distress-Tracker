package com.example.distresstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.Objects;

public class CovidQuestionnaire extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_questionnaire);

        String username;

        Intent intentReceived = getIntent();
        Bundle data = intentReceived.getExtras();
        if(data != null){
            username = data.getString("message");

        }
        else{
            username ="";
        }

        Button btnConfirm= (Button)findViewById(R.id.btnConfirm);

        CheckBox chkFeverYes= (CheckBox) findViewById(R.id.chkFeverYes);
        CheckBox chkFeverNo= (CheckBox) findViewById(R.id.chkFeverNo);
        CheckBox chkCoughYes= (CheckBox) findViewById(R.id.chkCoughYes);
        CheckBox chkCoughNo= (CheckBox) findViewById(R.id.chkCoughNo);
        CheckBox chkThroatYes= (CheckBox) findViewById(R.id.chkThroatYes);
        CheckBox chkThroatNo= (CheckBox) findViewById(R.id.chkThroatNo);

        chkFeverYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkFeverNo.isChecked())
                    chkFeverNo.setChecked(false);
            }
        });
        chkFeverNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkFeverYes.isChecked())
                    chkFeverYes.setChecked(false);
            }
        });
        chkCoughYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkCoughNo.isChecked())
                    chkCoughNo.setChecked(false);
            }
        });
        chkCoughNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkCoughYes.isChecked())
                    chkCoughYes.setChecked(false);
            }
        });
        chkThroatYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkThroatNo.isChecked())
                    chkThroatNo.setChecked(false);
            }
        });
        chkThroatNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkThroatYes.isChecked())
                    chkThroatYes.setChecked(false);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((chkFeverYes.isChecked() || chkFeverNo.isChecked())&&(chkCoughYes.isChecked() || chkCoughNo.isChecked())&&(chkThroatYes.isChecked() || chkThroatNo.isChecked())){
                    String Symptoms="false";
                    if(chkFeverYes.isChecked() || chkCoughYes.isChecked() || chkThroatYes.isChecked()){
                        Symptoms="true";

                    }

                    Intent intent = new Intent(CovidQuestionnaire.this, Options.class);
                    Bundle data = new Bundle();
                    data.putString("Symptoms",Symptoms);
                    data.putString("message", username);
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }



            }
        });
    }
}