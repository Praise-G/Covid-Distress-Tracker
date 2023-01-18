package com.example.distresstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    ImageView bgImage;
    LottieAnimationView lottieAnimationView;
    LottieAnimationView lottieAnimationView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreen.this,MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        },4000);


        bgImage = findViewById(R.id.img);
        lottieAnimationView = findViewById(R.id.lottie);
        lottieAnimationView2 = findViewById(R.id.lottie3);

        bgImage.animate().translationY(-2200).setDuration(1000).setStartDelay(3000);
        lottieAnimationView.animate().translationY(1400).setDuration(1000).setStartDelay(3000);
        lottieAnimationView2.animate().translationY(1400).setDuration(1000).setStartDelay(3000);

    }
}