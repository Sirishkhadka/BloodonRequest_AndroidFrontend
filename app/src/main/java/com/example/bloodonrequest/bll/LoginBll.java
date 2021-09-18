package com.example.bloodonrequest.bll;

import android.util.Log;
import android.widget.Toast;

import com.example.bloodonrequest.api.UserApi;
import com.example.bloodonrequest.backendUrl.BackendUrl;
import com.example.bloodonrequest.response.LoginResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginBll {


    boolean isSuccess = false;

    public LoginBll() {
    }

    public boolean checkUser(String emailId, String password) {

        UserApi usersAPI = BackendUrl.getInstance().create(UserApi.class);

        Call<LoginResponse> usersCall = usersAPI.checkUser(emailId, password);


        try {
            Response<LoginResponse> loginResponse = usersCall.execute();
            if (loginResponse.isSuccessful() &&
                    loginResponse.body().getStatus().equals("Login success!")) {

                BackendUrl.token += loginResponse.body().getToken();
                // Url.Cookie = imageResponseResponse.headers().get("Set-Cookie");
                isSuccess = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }
}
