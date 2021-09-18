package com.example.bloodonrequest.ui.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bloodonrequest.AuthActivity;
import com.example.bloodonrequest.R;
import com.example.bloodonrequest.api.UserApi;
import com.example.bloodonrequest.backendUrl.BackendUrl;
import com.example.bloodonrequest.model.User;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    private EditText etFullName, etGender, etBloodGroup, etCountry, etContact, etAddressCity, etEmail;
    private Button btnUpdate;

    private String pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
    private Matcher m;

    private String emailValidator = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private String id;
    String token;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.profile_fragment, container, false);

        etFullName = root.findViewById(R.id.editUpdateFullName);
        etGender = root.findViewById(R.id.editUpdateGender);
        etBloodGroup = root.findViewById(R.id.editUpdateBloodGroup);
        etCountry = root.findViewById(R.id.editUpdateCountry);
        etContact = root.findViewById(R.id.editUpdateContactNumber);
        etAddressCity = root.findViewById(R.id.editUpdateCity);
        etEmail = root.findViewById(R.id.editUpdateEmail);
        btnUpdate = root.findViewById(R.id.btnUpdateProfile);

        etFullName.requestFocus();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);

        if (sharedPreferences.getString("myToken", "").equals("")) {
            token = BackendUrl.token;
        } else {
            token = sharedPreferences.getString("myToken", "");
        }
        getUserProfile();


        btnUpdate.setOnClickListener(v -> {
            if (etFullName.getText().toString().trim().isEmpty()) {
                etFullName.setError("Please enter full name");
                etFullName.requestFocus();
                return;
            }
            if (etGender.getText().toString().trim().isEmpty()) {
                etGender.setError("Please enter gender");
                etGender.requestFocus();
                return;
            }

            if (etBloodGroup.getText().toString().trim().isEmpty()) {
                etBloodGroup.setError("Please enter blood group");
                etBloodGroup.requestFocus();
                return;
            }

            if (etCountry.getText().toString().trim().isEmpty()) {
                etCountry.setError("Please enter country");
                etCountry.requestFocus();
                return;
            }

            Pattern r = Pattern.compile(pattern);
            if (!etContact.getText().toString().trim().isEmpty()) {
                m = r.matcher(etContact.getText().toString().trim());
            } else {
                etContact.setError("Please enter your mobile number");
                etContact.requestFocus();
                return;
            }
            if (!m.find()) {
                etContact.setError("Please enter a valid mobile number");
                etContact.requestFocus();
                return;
            }
            if (etAddressCity.getText().toString().trim().isEmpty()) {
                etAddressCity.setError("Please enter city");
                etAddressCity.requestFocus();
                return;
            }
            if (!etEmail.getText().toString().trim().matches(emailValidator)) {
                etEmail.setError("Please enter a valid email address");
                etEmail.requestFocus();
                return;
            }

            updateProfile();

        });
        return root;
    }

    private void updateProfile() {

        User user = new User(
                etFullName.getText().toString().trim(),
                etGender.getText().toString().trim(),
                etBloodGroup.getText().toString().trim(),
                etCountry.getText().toString().trim(),
                etContact.getText().toString().trim(),
                etAddressCity.getText().toString().trim(),
                etEmail.getText().toString().trim()
        );

        UserApi userApi = BackendUrl.getInstance().create(UserApi.class);
        Call<User> updateProfile = userApi.updateProfile(token, user, id);
        updateProfile.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Profile is Updated Successfully");
                builder1.setCancelable(true);
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    private void getUserProfile() {


        UserApi usersAPI = BackendUrl.getInstance().create(UserApi.class);
        Call<User> userCall = usersAPI.getUserDetails(token);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 401 || response.code() == 402) {
                    Intent intent = new Intent(getActivity(), AuthActivity.class);
                    startActivity(intent);
                    return;
                }
                etFullName.setText(response.body().getFullName());
                etGender.setText(response.body().getGender());
                etBloodGroup.setText(response.body().getBloodGroup());
                etCountry.setText(response.body().getCountry());
                etContact.setText(response.body().getContactNumber());
                etAddressCity.setText(response.body().getAddress());
                etEmail.setText(response.body().getEmailId());
                id = response.body().get_id();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}