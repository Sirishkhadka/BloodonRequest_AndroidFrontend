package com.example.bloodonrequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    private TextInputEditText countryCode, phoneNumber;
    private ProgressBar progressBarOtp;
    private AppCompatButton btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to remove the title feature from window
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //to hide the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        //to make window fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otpactivity);

        //bind the views
        countryCode = findViewById(R.id.etCountryCode);
        phoneNumber = findViewById(R.id.etPhoneNumber);
        btnVerify = findViewById(R.id.btnOptVerify);
        progressBarOtp = findViewById(R.id.progressBarOTP);

        btnVerify.setOnClickListener(v -> {
            //getting values from edit text
            String code = Objects.requireNonNull(countryCode.getText()).toString().trim();
            String number = Objects.requireNonNull(phoneNumber.getText()).toString().trim();

            //validation of phone number
            if (number.isEmpty() || number.length() < 10) {
                phoneNumber.setError("Valid number is required");
                phoneNumber.requestFocus();
                return;
            }
            if (code.isEmpty()) {
                countryCode.setError("Valid country code is required");
                countryCode.requestFocus();
                return;
            }

            progressBarOtp.setVisibility(View.VISIBLE);
            btnVerify.setVisibility(View.INVISIBLE);


            String myNumber = code + number;
            verifyNumber(myNumber);


//            Intent intent = new Intent(OTPActivity.this, VerifyNumberActivity.class);
//            intent.putExtra("myNumber", myNumber);
//            intent.putExtra("verificationId", "154545545");
//            startActivity(intent);


        });
    }

    private void verifyNumber(String myNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                myNumber,
                60,
                TimeUnit.SECONDS,   //once the code is send user cannot send otp for 60s
                OTPActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                        progressBarOtp.setVisibility(View.INVISIBLE);
                        btnVerify.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                        System.out.println(e.getMessage());
                        progressBarOtp.setVisibility(View.INVISIBLE);
                        btnVerify.setVisibility(View.VISIBLE);
                        Toast.makeText(OTPActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull @NotNull String verificationId, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);

                        progressBarOtp.setVisibility(View.INVISIBLE);
                        btnVerify.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(OTPActivity.this, VerifyNumberActivity.class);
                        intent.putExtra("myNumber", myNumber);
                        intent.putExtra("verificationId", verificationId);
                        startActivity(intent);
                    }
                }
        );
    }


}