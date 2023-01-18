package com.example.distresstracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    public String username;
    LottieAnimationView LottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText pass = (EditText) findViewById(R.id.ETRegisterPassword);
        EditText passConfirm = (EditText) findViewById(R.id.ETRegisterConfirmPassword);
        EditText name = (EditText) findViewById(R.id.ETRegisterName);
        EditText email = (EditText) findViewById(R.id.ETRegisterEmail);
        EditText hint = (EditText) findViewById(R.id.ETRegisterHint);
        TextView hintInfo = (TextView) findViewById(R.id.TextHintInfo);
        Button btnBack= (Button)findViewById(R.id.btnBack);
        LottieAnimationView = findViewById(R.id.lottie);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Register.this, MainActivity.class);

                startActivity(intent);

            }
        });

        Button btnConfirmRegistration= (Button)findViewById(R.id.btnConfirmRegistration);

        btnConfirmRegistration.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String strName= name.getText().toString();
                String strPass= pass.getText().toString();
                String strPassConfirm= passConfirm.getText().toString();
                String Hintsp = hint.getText().toString();
                String strHint= Hintsp.replace(" ", "+");

                if (!(strHint.equals(""))&&!(strName.equals(""))
                        &&!(strPass.equals("")) && !(strPassConfirm.equals(""))){
                    if (!strPass.contains(" ") && !strName.contains(" ")&&!strPass.contains("\"")) {
                        if (strPass.equals(strPassConfirm)) {
                            OkHttpClient client = new OkHttpClient();
                            String url = "https://lamp.ms.wits.ac.za/~s2437872/register.php?" + "username=" + name.getText().toString() + "&pass=" + pass.getText().toString() + "&hint=" + hint.getText().toString();
                            Request request = new Request.Builder()
                                    .url(url)
                                    .build();
                            Log.d("URL!!!!!!!!", url);
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, final Response response) throws IOException {

                                    if (response.isSuccessful()) {

                                        final String responseData = response.body().string();

                                        Register.this.runOnUiThread(new Runnable() {
                                            @SuppressLint("SetTextI18n")
                                            @RequiresApi(api = Build.VERSION_CODES.P)
                                            @Override
                                            public void run() {
                                                if (responseData.equals("Error1")) {
                                                    hintInfo.setText("Username taken. Please try different username");
                                                    hintInfo.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                                                } else if (!responseData.equals("New record created successfully")) {
                                                    hintInfo.setText("Unknown error, please try again");
                                                    hintInfo.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                                                } else {
                                                    hintInfo.setText("Registration successful");
                                                    hintInfo.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                                                    LottieAnimationView.setVisibility(View.VISIBLE);
                                                    LottieAnimationView.animate().setDuration(1000).setStartDelay(1000);
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Intent intent = new Intent(Register.this, CovidQuestionnaire.class);
                                                            Bundle data1 = new Bundle();
                                                            username = strName;
                                                            data1.putString("message",username);
                                                            intent.putExtras(data1);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    },1600);
                                                }

                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            hintInfo.setText("Passwords do not match");//change to invsbleTV
                            hintInfo.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                        }
                    }
                    else {
                        if (strPass.contains(" ")||(strPass.contains("\""))){
                            hintInfo.setText("Invalid-Please remove spaces and quotations in password");
                            hintInfo.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                        }
                        else{
                            hintInfo.setText("Invalid-Please remove spaces in username");
                            hintInfo.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                        }
                    }
                }//here
                else{
                    hintInfo.setText("Please fill in all blank fields");
                    hintInfo.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }



            }
        });
    }
}