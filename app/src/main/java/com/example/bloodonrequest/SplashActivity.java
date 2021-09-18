package com.example.bloodonrequest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Animation topAnim, bottomAnim;

    private ImageView imageView;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to remove the title feature from window
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //to hide the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        //to make window fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);


        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_splash);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_splash);

        imageView = findViewById(R.id.splash_image);
        linearLayout = findViewById(R.id.splash_linear);
        progressBar = findViewById(R.id.progressBar);

        //amination
        imageView.setAnimation(topAnim);
        linearLayout.setAnimation(bottomAnim);

        //thread begins
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("token", Context.MODE_PRIVATE);

            if (sharedPreferences.getString("myToken", "").equals("")) {
                Intent intent = new Intent(SplashActivity.this, OTPActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }

        }, 3500);
    }
}