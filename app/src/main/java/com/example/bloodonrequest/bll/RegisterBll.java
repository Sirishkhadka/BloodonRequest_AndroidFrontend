package com.example.bloodonrequest.bll;

import com.example.bloodonrequest.api.UserApi;
import com.example.bloodonrequest.backendUrl.BackendUrl;
import com.example.bloodonrequest.model.User;
import com.example.bloodonrequest.response.RegisterResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class RegisterBll {
    String image;
    String fullName;
    String gender;
    String bloodGroup;
    String country;
    String contactNumber;
    String address;
    String emailId;
    String password;
    boolean isSuccess = false;

    public RegisterBll() {

    }

    public RegisterBll(String fullName, String gender, String bloodGroup, String country, String contactNumber, String address, String emailId, String password, String image) {
        this.fullName = fullName;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.country = country;
        this.contactNumber = contactNumber;
        this.address = address;
        this.emailId = emailId;
        this.password = password;
        this.image = image;
    }

    public boolean registerUser() {


        UserApi userInterface = BackendUrl.getInstance().create(UserApi.class);
        Call<RegisterResponse> userModelCall = userInterface.registerUser(new User(fullName, gender, bloodGroup, country, contactNumber, address, emailId, password, image));

        try {
            Response<RegisterResponse> userResponse = userModelCall.execute();

            if (!userResponse.isSuccessful()) {
                isSuccess = false;

            } else {
                isSuccess = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }


}
