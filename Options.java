package com.example.distresstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Options extends AppCompatActivity {
    public String  CovidStatus;

    String myResponse="";
    public void SendRequest(String sName, String sPassword, String phpfile){

        //if this doesn't work, remember to sync build.gradle
        OkHttpClient client= new OkHttpClient();

        String url= "https://lamp.ms.wits.ac.za/~s2437872/" + phpfile;

        RequestBody requestBody= new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("USER_NAME",sName)
                .addFormDataPart("USER_PASSWORD",sPassword)
                .build();

        Request request=new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    myResponse = response.body().string();
                    ;
                }
            }
        });

    };

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                        Options.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        TextView text = (TextView) findViewById(R.id.TVCovidConfirm);
        TextView CheckInText = (TextView) findViewById(R.id.CheckInText);
        Button btnCvdStatus = (Button) findViewById(R.id.btncvdstatus);
        Button CkButton = (Button) findViewById(R.id.btnCheckIn);
        Button btnLocationTables = (Button) findViewById(R.id.btnLocationTables);
        Button btnCheckRisk = (Button) findViewById(R.id.btnCheckRisk);

        String username;
        String Symptoms;
        Intent intentReceived = getIntent();
        Bundle data = intentReceived.getExtras();

        if(data != null){
            username = data.getString("message");
            Symptoms = data.getString("Symptoms");

        }
        else{
            username ="";
            Symptoms ="";
        }
        TextView textRisk=(TextView) findViewById(R.id.textRisk);
        if (Symptoms.equals("true")){
            textRisk.setVisibility(View.VISIBLE);
            textRisk.setText("We advise you go for a covid test! (According to Questionnaire Response)");
        }

        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.CovidStatus, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner2.setAdapter(adapter);

        spinner2.setSelection(0, true);
        View v = spinner2.getSelectedView();
        ((TextView)v).setTextColor(Color.WHITE);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Change the selected item's text color
                ((TextView) view).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        btnCvdStatus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String result = spinner2.getSelectedItem().toString();

                if (result.equals("Negative")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(Options.this);

                    builder.setMessage("Are you sure?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    CovidStatus = "Negative";
                                    text.setText("Status Successfully Updated to Negative");
                                    text.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(result.equals("Positive")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(Options.this);

                    builder.setMessage("Are you sure?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    CovidStatus = "Positive";
                                    SendRequest(username,"","ChangeCovidStatus.php");
                                    text.setText("Status Successfully Updated to Positive");
                                    text.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else {
                    text.setText("Please update your Covid-19 status");
                    text.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }
        });

        CkButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                List<String> options = new ArrayList<String>() {{
                    add("Positive");
                    add("Negative");
                }};
                if(options.contains(CovidStatus)) {
                    Intent intent = new Intent(Options.this, CheckIntoLocation.class);
                    Bundle data1 = new Bundle();
                    Bundle data2 = new Bundle();
                    data1.putString("cvd", CovidStatus);
                    data2.putString("message", username);
                    intent.putExtras(data1);
                    intent.putExtras(data2);
                    startActivity(intent);
                }
                else{
                    CheckInText.setText("Check into a Location (Please update your Covid-19 status first, Make sure you CONFIRM)");
                    CheckInText.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }
        });

        btnLocationTables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Options.this, LocationsTable.class);
                startActivity(intent);
            }
        });

        btnCheckRisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Options.this, CheckRisk.class);
                Bundle data3 = new Bundle();
                data3.putString("message", username);
                intent.putExtras(data3);
                startActivity(intent);
            }
        });

    }

}