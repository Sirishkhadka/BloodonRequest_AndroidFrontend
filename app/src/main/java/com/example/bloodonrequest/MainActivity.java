package com.example.bloodonrequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodonrequest.api.UserApi;
import com.example.bloodonrequest.backendUrl.BackendUrl;
import com.example.bloodonrequest.response.LoginResponse;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton btnLoginRegister, btnLoginLogin;
    private EditText etEmail, etPassword;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private TextView tvError;

    private CheckBox remember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to remove the title feature from window
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //to hide the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        //to make window fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        btnLoginRegister = findViewById(R.id.btnLoginRegister);
        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLoginLogin = findViewById(R.id.btnLoginLogin);
        tvError = findViewById(R.id.tvError);
        remember = findViewById(R.id.checkboxLoginRemember);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        etEmail.setText(sharedPreferences.getString("myEmail", null));
        etPassword.setText(sharedPreferences.getString("myPassword", null));


        etEmail.requestFocus();
        tvError.setVisibility(View.INVISIBLE);


        btnLoginLogin.setOnClickListener(v -> {

            if (!etEmail.getText().toString().trim().matches(emailPattern)) {
                etEmail.setError("Please enter a valid email address");
                etEmail.requestFocus();
            } else if (etPassword.getText().toString().trim().length() < 6) {
                etPassword.setError("The password should be at least 6 characters");
                etPassword.requestFocus();
            } else {
                signIn();
            }


        });

        btnLoginRegister.setOnClickListener(v -> {
            Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(registerIntent);
            finish();
            return;
        });
    }

    private void signIn() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        UserApi userApi = BackendUrl.getInstance().create(UserApi.class);
        Call<LoginResponse> loginResponseCall = userApi.checkUser(email, password);

        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {


                if (response.code() == 404) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Email not found");
                    return;
                }

                if (response.code() == 401) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Password do not match");
                    return;
                }

                if (response.code() == 200) {
                    tvError.setVisibility(View.INVISIBLE);
                    BackendUrl.token += response.body().getToken();

                    if (remember.isChecked()) {
                        saveTokenToDevice();
                        saveEmailAndPassword();
                    }

                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveEmailAndPassword() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("myEmail", etEmail.getText().toString().trim());
        editor.putString("myPassword", etPassword.getText().toString().trim());
        editor.commit();
    }

    private void saveTokenToDevice() {
        SharedPreferences sharedPreferences = getSharedPreferences("token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("myToken", BackendUrl.token);
        editor.commit();
    }
}