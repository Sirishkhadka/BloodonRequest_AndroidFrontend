package com.example.bloodonrequest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.bloodonrequest.api.UserApi;
import com.example.bloodonrequest.backendUrl.BackendUrl;
import com.example.bloodonrequest.model.User;
import com.example.bloodonrequest.response.ImageResponse;
import com.example.bloodonrequest.response.RegisterResponse;
import com.example.bloodonrequest.strictMode.StrictModeClass;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

public class RegisterActivity extends AppCompatActivity {

    private ImageView ivProfilePhoto;
    private TextView tvCountry;
    private EditText etName, etPhone, etEmail, etPassword, etConfirmPassword;
    private RadioGroup rgGender, rgBloodGroup;
    private RadioButton rbMale, rbFemale, rbOthers, rbOp, rbOn, rbAp, rbAn, rbBp, rbBn, rbAbp, rbAbn;
    private AutoCompleteTextView actvAddress;
    private Button btnRegister, btnLogin;
    private Spinner spinCountry;

    private String imageName = "";
    private String imagePath;


    private String emailValidator = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private String[] address = {"Kathmandu", "Pokhara", "Lumbini", "Lalitpur", "Bhaktapur",
            "Bharatpur", "Bharatpur", "Janakpur", "Ghorahi", "Tulsipur", "Hetauda",
            "Nepalgunj", "Dharan", "Itahari", "Jitpur Simara", "Ilam", "Gulmi", "Kavre", "Kakadvitta",
            "Baitadi", "Rajbiraj", "Bhojpur", "Humla", "Damauli", "Nawalparasi",
            "Butwal", "Birgunj", "Lamjung", "Chitwan", "Jhapa", "Palpa", "Jumla", "Dhading"};
    //Alert
    private AlertDialog.Builder builder;

    String[] country = {"Nepal", "India", "USA", "UK", "China", "Bhutan", "Pakistan", "Australia",
            "Japan", "France", "Mexico", "Qatar", "Vietnam"};
    int images[] = {R.drawable.nepal, R.drawable.india, R.drawable.us, R.drawable.uk,
            R.drawable.china, R.drawable.bhutan, R.drawable.pakistan, R.drawable.australia,
            R.drawable.japan, R.drawable.france, R.drawable.mexico,
            R.drawable.qatar, R.drawable.vietnam
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to remove the title feature from window
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //to hide the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        //to make window fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);

        ivProfilePhoto = findViewById(R.id.iv_add_a_photo);
        tvCountry = findViewById(R.id.tvRegisterCountry);
        etName = findViewById(R.id.etRegisterName);
        etPhone = findViewById(R.id.etRegisterPhone);
        etEmail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterPassword);
        etConfirmPassword = findViewById(R.id.etRegisterConfirmPassword);
        rgGender = findViewById(R.id.rgRegisterGender);
        rgBloodGroup = findViewById(R.id.rgBloodGroup);
        rbMale = findViewById(R.id.rbRegisterMale);
        rbFemale = findViewById(R.id.rbRegisterFemale);
        rbOthers = findViewById(R.id.rbRegisterOthers);
        rbOp = findViewById(R.id.rbRegisterOP);
        rbOn = findViewById(R.id.rbRegisterON);
        rbAp = findViewById(R.id.rbRegisterAP);
        rbAn = findViewById(R.id.rbRegisterAN);
        rbBp = findViewById(R.id.rbRegisterBP);
        rbBn = findViewById(R.id.rbRegisterBN);
        rbAbp = findViewById(R.id.rbRegisterABP);
        rbAbn = findViewById(R.id.rbRegisterABN);
        spinCountry = findViewById(R.id.spinCountry);
        actvAddress = findViewById(R.id.actvRegisterAddress);
        btnLogin = findViewById(R.id.btnRegisterLogin);
        btnRegister = findViewById(R.id.btnRegisterRegister);

        btnRegister.setOnClickListener(v -> {

            if (etName.getText().toString().trim().isEmpty()) {
                etName.setError("Please enter full name");
                etName.requestFocus();
                return;
            }

            if (etPhone.getText().toString().trim().length() < 10) {
                etPhone.setError("The number should be at least 10 numbers");
                etPhone.requestFocus();
                return;
            }

            if (!etEmail.getText().toString().trim().matches(emailValidator)) {
                etEmail.setError("Please enter a valid email address");
                etEmail.requestFocus();
                return;
            }
            if (etPassword.getText().toString().trim().length() < 6) {
                etPassword.setError("Please enter a password of at least 6 characters");
                etPassword.requestFocus();
                return;
            }
            if (etConfirmPassword.getText().toString().trim().length() < 6) {
                etConfirmPassword.setError("Please enter a password of at least 6 characters");
                etConfirmPassword.requestFocus();
                return;
            }

            if (!etConfirmPassword.getText().toString().trim().equals(etPassword.getText().toString().trim())) {
                Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            SaveProfileImage();
            registerUser();


        });


        //for address autocomplete
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.select_dialog_item, address
        );

        actvAddress.setAdapter(stringArrayAdapter);
        actvAddress.setThreshold(1);

        //for country spinner
        SpinnerAdapter customAdapter = new com.example.bloodonrequest.adapter.SpinnerAdapter(getApplicationContext(), images, country);
        spinCountry.setAdapter(customAdapter);

        spinCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCountry = country[position];
                tvCountry.setText(selectedCountry);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(RegisterActivity.this, "No country has been selected.", Toast.LENGTH_LONG).show();
            }
        });

        ivProfilePhoto.setOnClickListener(v -> {
            BrowseProfileImage();
        });

        builder = new AlertDialog.Builder(this);
        btnLogin.setOnClickListener(v -> {
            builder.setMessage("Registration Completed??")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(loginIntent);
                        finish();
                        return;
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.cancel();
                        Toast.makeText(this, "Please Complete your registration.", Toast.LENGTH_SHORT).show();
                    });
            AlertDialog alert = builder.create();
            alert.setTitle(" Registration Confirmation ");
            alert.show();

        });

    }

    private void registerUser() {
        String fullName = etName.getText().toString().trim();
        String contactNumber = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String emailId = etEmail.getText().toString().trim();

        SpinnerAdapter customAdapter = new com.example.bloodonrequest.adapter.SpinnerAdapter(getApplicationContext(), images, country);
        spinCountry.setAdapter(customAdapter);


        String country = tvCountry.getText().toString().trim();
        String address = actvAddress.getText().toString().trim();

        int selectedGender = rgGender.getCheckedRadioButtonId();
        RadioButton radioGender = (RadioButton) findViewById(selectedGender);
        String gender = radioGender.getText().toString();

        int selectedBloodGroup = rgBloodGroup.getCheckedRadioButtonId();
        RadioButton radioBloodGroup = (RadioButton) findViewById(selectedBloodGroup);
        String bloodGroup = radioBloodGroup.getText().toString();

        User users = new User(fullName, gender, bloodGroup, country, contactNumber, address, emailId, password, imageName);

        UserApi usersAPI = BackendUrl.getInstance().create(UserApi.class);
        Call<RegisterResponse> signUpResponseCall = usersAPI.registerUser(users);

        signUpResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (!response.isSuccessful()) {
                    if (response.code()==403){
                        Toast.makeText(RegisterActivity.this, "User with "+ emailId+ " already exist", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
               Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void SaveProfileImage() {
        File file = new File(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile",
                file.getName(), requestBody);

        UserApi usersAPI = BackendUrl.getInstance().create(UserApi.class);
        Call<ImageResponse> responseBodyCall = usersAPI.uploadImage(body);

        //  synchronized call
        StrictModeClass.StrictMode();
        try {
            Response<ImageResponse> imageResponseResponse = responseBodyCall.execute();
            imageName = imageResponseResponse.body().getFilename();
            Toast.makeText(this, "Image inserted" + imageName, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void BrowseProfileImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {

            Toast.makeText(this, "Operation Cancelled by user.", Toast.LENGTH_LONG).show();
            return;
        }

        if (resultCode == RESULT_OK) {
            if (data == null) {

                Toast.makeText(this, "Image not selected. Please select an image.", Toast.LENGTH_LONG).show();
                return;
            }

            Uri imageUri = data.getData();
            ivProfilePhoto.setImageURI(imageUri);
            imagePath = getImageRealPath(imageUri);
        }
    }

    public String getImageRealPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(),
                uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }
}