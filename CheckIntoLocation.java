package com.example.distresstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CheckIntoLocation extends AppCompatActivity {

    LottieAnimationView LottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_into_location);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Spinner spinner = findViewById(R.id.spinner_locations);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.locations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0, true);
        View v = spinner.getSelectedView();
        ((TextView)v).setTextColor(Color.WHITE);
        LottieAnimationView = findViewById(R.id.lottie);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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
        Button CheckInButton = (Button)findViewById(R.id.CheckInButton);
        Button btnBack = (Button) findViewById(R.id.btnback);
        TextView hintInfo = (TextView) findViewById(R.id.HintInfo);

        String name;
        Intent intentReceived = getIntent();
        Bundle data = intentReceived.getExtras();
        if(data != null){
            name = data.getString("message");

        }
        else{
            name ="";
        }

        String CovidStatus;
        Intent intentReceived2 = getIntent();
        Bundle data2 = intentReceived.getExtras();
        if(data2 != null){
            CovidStatus = data2.getString("cvd");
        }
        else{
            CovidStatus ="";
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckIntoLocation.this, Options.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        CheckInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CheckIntoLocation.this);

                builder.setMessage("Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (!spinner.getSelectedItem().toString().equals("*select*")) {
                                    OkHttpClient client = new OkHttpClient();
                                    String url = "";
                                    if (CovidStatus.equals("Negative")) {
                                        url = "https://lamp.ms.wits.ac.za/~s2437872/CheckInN.php?" + "&username=" + name + "&locationname=" + spinner.getSelectedItem().toString();
                                    } else if (CovidStatus.equals("Positive")) {
                                        url = "https://lamp.ms.wits.ac.za/~s2437872/CheckInP.php?" + "&username=" + name + "&locationname=" + spinner.getSelectedItem().toString();
                                    }
                                    Request request = new Request.Builder()
                                            .url(url)
                                            .build();
                                    Log.d("URL", url);
                                    client.newCall(request).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                            Log.d("ERROR", "!\n!\n!\n!\n!\n!\n!\n!\n!\n");
                                        }

                                        @Override
                                        public void onResponse(Call call, final Response response) throws IOException {

                                            if (response.isSuccessful()) {

                                                final String responseData = response.body().string();

                                                CheckIntoLocation.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (responseData.equals("Data Submit Successfully")) {
                                                            hintInfo.setText("Check in Successful");
                                                            hintInfo.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                                                            LottieAnimationView.setVisibility(View.VISIBLE);
                                                            LottieAnimationView.animate();
                                                            LottieAnimationView.playAnimation();
                                                            final Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    LottieAnimationView.setVisibility(View.GONE);
                                                                }
                                                            },2000);

                                                        } else {
                                                            hintInfo.setText("Unknown error, please try again");
                                                            hintInfo.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                                                        }

                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                                else {
                                    hintInfo.setText("Please select a Location");
                                    hintInfo.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                                }

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
        });
    }
}