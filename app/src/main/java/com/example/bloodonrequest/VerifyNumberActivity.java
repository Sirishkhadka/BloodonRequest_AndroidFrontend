package com.example.bloodonrequest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifyNumberActivity extends AppCompatActivity {


    private TextView mobileNumber;
    private EditText code1, code2, code3, code4, code5, code6;
    private TextView resendCode;
    private AppCompatButton btnVerifyCode;
    private ProgressBar progressBarNumber;
    private String verificationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //to remove the title feature from window
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //to hide the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        //to make window fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_number);
        mobileNumber = findViewById(R.id.tvMobileNumber);
        code1 = findViewById(R.id.code1);
        code2 = findViewById(R.id.code2);
        code3 = findViewById(R.id.code3);
        code4 = findViewById(R.id.code4);
        code5 = findViewById(R.id.code5);
        code6 = findViewById(R.id.code6);
        resendCode = findViewById(R.id.tvResendOTP);
        btnVerifyCode = findViewById(R.id.btnVerifyCode);
        progressBarNumber = findViewById(R.id.progressBarNumber);

        progressBarNumber.setVisibility(View.INVISIBLE);
        code1.requestFocus();
        setUpOtpInputs();

        verificationId = getIntent().getExtras().getString("verificationId");
        String myNumber = getIntent().getExtras().getString("myNumber");
        mobileNumber.setText(myNumber);

        resendCode.setOnClickListener(v -> {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    myNumber,
                    60,
                    TimeUnit.SECONDS,   //once the code is send user cannot send otp for 60s
                    VerifyNumberActivity.this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {

                        }

                        @Override
                        public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {

                            Toast.makeText(VerifyNumberActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull @NotNull String newVerificationId, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(newVerificationId, forceResendingToken);
                            verificationId = newVerificationId;
                            Toast.makeText(VerifyNumberActivity.this, "OTP code send", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        });

        btnVerifyCode.setOnClickListener(v -> {

            if (code1.getText().toString().trim().isEmpty()) {
                code1.setError("Please enter the first digit of the code..");
                code1.requestFocus();
                return;
            }
            if (code2.getText().toString().trim().isEmpty()) {
                code2.setError("Please enter the second digit of the code..");
                code2.requestFocus();
                return;
            }
            if (code3.getText().toString().trim().isEmpty()) {
                code3.setError("Please enter the third digit of the code..");
                code3.requestFocus();
                return;
            }
            if (code4.getText().toString().trim().isEmpty()) {
                code4.setError("Please enter the fourth digit of the code..");
                code4.requestFocus();
                return;
            }
            if (code5.getText().toString().trim().isEmpty()) {
                code5.setError("Please enter the fifth digit of the code..");
                code5.requestFocus();
                return;
            }
            if (code6.getText().toString().trim().isEmpty()) {
                code6.setError("Please enter the sixth digit of the code..");
                code6.requestFocus();
                return;
            }

//            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
            verifyCode();
        });

    }

    private void verifyCode() {
        String code = code1.getText().toString() +
                code2.getText().toString() +
                code3.getText().toString() +
                code4.getText().toString() +
                code5.getText().toString() +
                code6.getText().toString();
        if (verificationId != null) {
            progressBarNumber.setVisibility(View.VISIBLE);
            btnVerifyCode.setVisibility(View.INVISIBLE);
            PhoneAuthCredential phoneAuthProvider = PhoneAuthProvider.getCredential(
                    verificationId,
                    code
            );
            FirebaseAuth.getInstance().signInWithCredential(phoneAuthProvider)
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnCompleteListener(task -> {
                        progressBarNumber.setVisibility(View.INVISIBLE);
                        btnVerifyCode.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "The verification code entered was invalid", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void setUpOtpInputs() {
        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    code2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    code3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    code4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    code5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    code6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}