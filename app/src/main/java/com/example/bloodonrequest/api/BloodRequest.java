package com.example.bloodonrequest.api;

import com.example.bloodonrequest.model.RequestBlood;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BloodRequest {
    @POST("requests/requestBlood")
    Call<RequestBlood> requestBlood(@Header("Authorization") String token, @Body RequestBlood requests);

    @GET("requests/requestBlood")
    Call<List<RequestBlood>> getAllBloodDonations();


    @GET("requests/{bloodGroup}/search")
    Call<List<RequestBlood>> searchBlood(@Header("Authorization") String token, @Path("bloodGroup") String bloodGroup);

    @DELETE("requests/deleteRequest/{id}")
    Call<RequestBlood> deleteRequest(@Header("Authorization") String token, @Path("id") String id);
}
