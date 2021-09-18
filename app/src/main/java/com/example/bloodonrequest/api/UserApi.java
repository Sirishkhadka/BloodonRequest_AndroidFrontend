package com.example.bloodonrequest.api;


import com.example.bloodonrequest.model.User;
import com.example.bloodonrequest.response.ImageResponse;
import com.example.bloodonrequest.response.LoginResponse;
import com.example.bloodonrequest.response.RegisterResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserApi {
    @FormUrlEncoded
    @POST("users/login")
    Call<LoginResponse> checkUser(@Field("emailId") String emailId, @Field("password") String password);

    @Multipart
    @POST("upload")
    Call<ImageResponse> uploadImage(@Part MultipartBody.Part img);

    @POST("users/signup")
    Call<RegisterResponse> registerUser(@Body User user);

    @GET("users/me")
    Call<User> getUserDetails(@Header("Authorization") String token);

    @PUT("users/{id}")
    Call<User> updateProfile(@Header("Authorization") String token, @Body User user, @Path("id") String id);
}
