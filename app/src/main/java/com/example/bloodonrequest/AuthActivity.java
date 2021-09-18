package com.example.bloodonrequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    private AppCompatButton btnAuthLogin;
    private AppCompatButton btnAuthRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //to remove the title feature from window
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //to hide the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        //to make window fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_auth);

        btnAuthLogin = findViewById(R.id.btnAuthLogin);
        btnAuthRegister = findViewById(R.id.btnAuthRegister);

        btnAuthRegister.setOnClickListener(v -> {
            Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(registerIntent);
            return;
        });
        btnAuthLogin.setOnClickListener(v -> {
            Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(loginIntent);
            return;
        });

    }
}