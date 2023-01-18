package com.example.distresstracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import com.airbnb.lottie.LottieAnimationView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    public static String globalUsername;
    public String username;
    //Check login function starts here
    //Check login function ends here
    LottieAnimationView LottieAnimationView;
    private TextView textViewRegister;


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.super.onBackPressed();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hides the textViewError and textViewHint
        TextView textViewError=(TextView) findViewById(R.id.textViewError);
        textViewError.setVisibility(View.INVISIBLE);

        TextView textViewHint=(TextView) findViewById(R.id.textHint);
        textViewHint.setVisibility((View.INVISIBLE));
        LottieAnimationView = findViewById(R.id.lottie);
        Button btnLogin= (Button)findViewById(R.id.btnLogin);
        Button btnRegister= (Button)findViewById(R.id.btnRegister);
        EditText editTextName= (EditText)findViewById(R.id.editTextTextPersonName); //put this outside of btnLogin for btnHint
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                EditText editTextName= (EditText)findViewById(R.id.editTextTextPersonName);
                EditText editTextPassword=(EditText)findViewById(R.id.editTextTextPassword);
                TextView textViewRegister=findViewById(R.id.textViewRegister);

                String sName= editTextName.getText().toString();
                String sPassword= editTextPassword.getText().toString();

                if (sName.equals("")|| sPassword.equals("")){
                    textViewError.setText("Please enter a username and password");
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setTextColor(getResources().getColor(android.R.color.holo_red_light));

                    return;
                }

                if (sName.contains(" ") || sPassword.contains(" ")){
                    textViewError.setText("Username and password cannot include a space");
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setTextColor(getResources().getColor(android.R.color.holo_red_light));

                    return;
                }
                if (sName.contains("\"") || sPassword.contains("\"")){
                    textViewError.setText("Username and password cannot include quotation marks");
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setTextColor(getResources().getColor(android.R.color.holo_red_light));

                    return;
                }


                OkHttpClient client= new OkHttpClient();

                String url= "https://lamp.ms.wits.ac.za/~s2437872/login.php";

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
                           final String myResponse = response.body().string();
                           MainActivity.this.runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   if (myResponse.contains("false")){
                                       textViewError.setText("Your login is incorrect");
                                       textViewError.setVisibility(View.VISIBLE);
                                       textViewError.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                                   }
                                   else if (myResponse.contains("true")){
                                       LottieAnimationView.setVisibility(View.VISIBLE);
                                       LottieAnimationView.animate().setDuration(1000).setStartDelay(1000);
                                       textViewHint.setVisibility(View.VISIBLE);
                                       textViewHint.setText("Login Successful");
                                       textViewHint.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                                       new Handler().postDelayed(new Runnable() {
                                           @Override
                                           public void run() {
                                               LocalDate todaysDate= LocalDate.now();
                                               String sToday=todaysDate.toString();
                                               Intent intent;
                                               Bundle data1 = new Bundle();
                                               if(myResponse.contains(sToday)){
                                                   intent = new Intent(MainActivity.this, Options.class);
                                                   data1.putString("Symptoms", "false");
                                               }
                                               else {
                                                   intent = new Intent(MainActivity.this, CovidQuestionnaire.class);
                                               }
                                               username = editTextName.getText().toString();
                                               data1.putString("message", username);
                                               intent.putExtras(data1);
                                               startActivity(intent);
                                               finish();
                                           }
                                       },1600);

                                   }
                                   else if (myResponse.contains("DNE")){
                                       textViewError.setText("The username does not exist");
                                       textViewError.setVisibility(View.VISIBLE);
                                       textViewError.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                                   }
                               }
                           });
                        }
                    }
                });

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Register.class);

                startActivity(intent);

            }
        });
        String Hint;
        Button btnHint= (Button) findViewById(R.id.btnHint);
        btnHint.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String sName = editTextName.getText().toString();
                textViewHint.setVisibility(View.VISIBLE);
                if (sName.equals("")) {
                    textViewHint.setText("Please enter a username before showing hint");
                    textViewHint.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    return;
                } else if (sName.contains(" ")) {
                    textViewHint.setText("Please enter a username without spaces");
                    textViewHint.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    return;
                } else if (sName.contains("\"")) {
                    textViewHint.setText("Please enter a username without quotations");
                    textViewHint.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    return;
                }

                OkHttpClient client= new OkHttpClient();

                String url= "https://lamp.ms.wits.ac.za/~s2437872/SendHint.php";

                RequestBody requestBody= new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("USER_NAME",sName)
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
                            final String myResponse = response.body().string();
                            if (myResponse.equals("DNE")){
                                textViewHint.setText("The username does not exist");
                                textViewHint.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                            } else{
                                textViewHint.setText("Your hint is: "+ myResponse);
                                textViewHint.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                            }

                        }
                    }
                });

            }

        });
    }
}