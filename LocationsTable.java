package com.example.distresstracker;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationsTable extends AppCompatActivity {
    LinearLayout containerLocation;
    LinearLayout containerInfection;
    LinearLayout titles;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations_table);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Button b = (Button) findViewById(R.id.btnLocationsBack);
        containerLocation = (LinearLayout) findViewById(R.id.LinearLeftLocation);
        containerInfection = (LinearLayout) findViewById(R.id.LinearRightInfections);
        lottieAnimationView = findViewById(R.id.Radar);
        titles = (LinearLayout) findViewById(R.id.titles);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LocationsTable.this, Options.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/~s2437872/locationTable.php")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Log.d("test", json);

                    LocationsTable.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ProcessJSON(json);
                        }
                    });
                }
            }
        });

        final Handler handler = new Handler();
        lottieAnimationView.animate();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lottieAnimationView.setVisibility(View.GONE);
                titles.setVisibility(View.VISIBLE);
                containerLocation.setVisibility(View.VISIBLE);
                containerInfection.setVisibility(View.VISIBLE);
            }
        },4400);

    }

    public void ProcessJSON(String json){
        try {
            JSONArray ja = new JSONArray(json);
            for (int i=0;i<ja.length();i++){
                JSONObject jo = ja.getJSONObject(i);
                LocationsLayout location=new LocationsLayout(this);
                LocationsLayout infections= new LocationsLayout(this);
                location.populate(jo, "LOCATION_ID");
                location.locationIDtoName();
                infections.populate(jo, "COVID CASES");
                if (i%2==0){
                    location.setBackgroundColor(Color.parseColor("#20FFFFFF"));
                    infections.setBackgroundColor(Color.parseColor("#20FFFFFF"));
                }
                containerLocation.addView(location);
                containerInfection.addView(infections);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.d("ERROR", "PROCESS JSON FAILED");
        }
    }
}

