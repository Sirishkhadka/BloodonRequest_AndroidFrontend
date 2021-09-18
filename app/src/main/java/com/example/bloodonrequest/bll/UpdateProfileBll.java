package com.example.bloodonrequest.bll;

import com.example.bloodonrequest.api.UserApi;
import com.example.bloodonrequest.backendUrl.BackendUrl;
import com.example.bloodonrequest.model.User;

import retrofit2.Call;
import retrofit2.Response;

public class UpdateProfileBll {
    String fullName;
    String gender;
    String bloodGroup;
    String contactNumber;
    String country;
    String address;
    String emailId;
    boolean success = true;

    public UpdateProfileBll(String fullName, String gender, String bloodGroup, String contactNumber, String country, String address, String emailId) {
        this.fullName = fullName;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.contactNumber = contactNumber;
        this.country = country;
        this.address = address;
        this.emailId = emailId;
    }

    public boolean updateUser(String id) {
        UserApi usersAPI = BackendUrl.getInstance().create(UserApi.class);

        User user = new User(fullName, gender, bloodGroup, country, contactNumber, address, emailId);
        Call<User> userModelCall = usersAPI.updateProfile(BackendUrl.token, user, id);

        try {
            Response<User> userModelResponse = userModelCall.execute();

            if (userModelResponse.isSuccessful()) {
                success = true;

            } else {
                success = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }
}
