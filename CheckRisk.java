package com.example.distresstracker;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.*;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import com.airbnb.lottie.LottieAnimationView;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.util.Objects;

//show if you at risk on specific day and covid cases for each specific location and show tables
//only if you at risk with smeone else from a specific day




public class CheckRisk extends AppCompatActivity {

    private TextView LocationView;
    private GridView gridView;
    private LinearLayout container;
    private LinearLayout container2;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_risk);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        TextView TextRisk = (TextView) findViewById(R.id.TextRisk);
        TextView TextInfo = (TextView) findViewById(R.id.PageInfo);
        TextView TitleLocation = (TextView) findViewById(R.id.location_header);
        TextView TitleDate = (TextView) findViewById(R.id.date_header);
        lottieAnimationView = findViewById(R.id.lottie);

        String username;
        Intent intentReceived = getIntent();
        Bundle data = intentReceived.getExtras();
        if(data != null){
            username = data.getString("message");

        }
        else{
            username ="";
        }


        container = findViewById(R.id.location_layout);
        container2 = findViewById(R.id.date_layout);
        container.setBackgroundColor(Color.parseColor("#20FFFFFF"));
        container2.setBackgroundColor(Color.parseColor("#20FFFFFF"));


        OkHttpClient user = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/~s2437872/location.php";

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("USER_NAME", username)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/~s2437872/location.php")
                .post(requestBody)
                .build();

        user.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String listOfLocation = response.body().string();



                    CheckRisk.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONArray array = new JSONArray(listOfLocation);
                                for(int i = 0; i < array.length(); i++){
                                    JSONObject object = array.getJSONObject(i);
                                    String location_id = object.getString("LOCATION_ID");

                                    if(location_id.equals("BFV")){
                                        location_id = "BedfordView";
                                    }

                                    else if(location_id.equals("BRM")){
                                        location_id = "Braamfontein";
                                    }


                                    else if(location_id.equals("BYS")){
                                        location_id = "Bryanston";
                                    }

                                    else if(location_id.equals("EDV")){
                                        location_id = "Edenvale";
                                    }

                                    else if(location_id.equals("FRW")){
                                        location_id = "Fourways";
                                    }

                                    else if(location_id.equals("MDR")){
                                        location_id = "Midrand";
                                    }

                                    else if(location_id.equals("PKH")){
                                        location_id = "Parkhurst";
                                    }

                                    else if(location_id.equals("RDB")){
                                        location_id = "Randburg";
                                    }

                                    else if(location_id.equals("STD")){
                                        location_id = "Sandton";
                                    }

                                    String check_in_date = object.getString("CHECK_IN_DATE");

                                    TextView t = new TextView(CheckRisk.this);
                                    TextView k = new TextView(CheckRisk.this);

                                    t.setTextSize(18);
                                    k.setTextSize(18);

                                    t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    k.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                                    container.setBackgroundColor(Color.LTGRAY);
                                    container2.setBackgroundColor(Color.LTGRAY);

                                    t.setText(location_id);
                                    t.setTextColor(Color.parseColor("#FFFFFF"));
                                    k.setText(check_in_date);
                                    k.setTextColor(Color.parseColor("#FFFFFF"));
                                    container.addView(t);
                                    container2.addView(k);
                                }
                                if (array.length() > 0){
                                    TextRisk.setText("You and someone with Covid-19 checked in at the same Location on the following dates:");
                                    TextRisk.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            container.setBackgroundColor(Color.parseColor("#20FFFFFF"));
                            container2.setBackgroundColor(Color.parseColor("#20FFFFFF"));

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    lottieAnimationView.setVisibility(View.GONE);
                                    TextInfo.setVisibility(View.VISIBLE);
                                    TextRisk.setVisibility(View.VISIBLE);
                                    TitleLocation.setVisibility(View.VISIBLE);
                                    TitleDate.setVisibility(View.VISIBLE);
                                    container.setVisibility(View.VISIBLE);
                                    container2.setVisibility(View.VISIBLE);
                                }
                            },2000);
                        }

                    });

                }
            }


        });

        Button btnBack2= (Button)findViewById(R.id.btnBack2);

        btnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CheckRisk.this, Options.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        /*final Handler handler = new Handler();
        lottieAnimationView.animate();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lottieAnimationView.setVisibility(View.GONE);
                TextInfo.setVisibility(View.VISIBLE);
                TextRisk.setVisibility(View.VISIBLE);
                TitleLocation.setVisibility(View.VISIBLE);
                TitleDate.setVisibility(View.VISIBLE);
                container.setVisibility(View.VISIBLE);
                container2.setVisibility(View.VISIBLE);
            }
        },4400);*/
    }
}


//if (mysqli_num_rows($result) > 0) {
//    // output data of each row
//    while($row = mysqli_fetch_assoc($result)) {
//        echo json_encode ("LOCATION ID: " . $row["LOCATION_ID"]. "| LOCATION_NAME: " . $row["LOCATION_NAME"].  "\n");
//        echo json_encode(" ------------------------------------------------------------------------------------------" "\n");
//    }
//} else {
//    echo "0 results";
//}
//mysqli_close($link);