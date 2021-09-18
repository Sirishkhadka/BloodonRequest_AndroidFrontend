package com.example.bloodonrequest.backendUrl;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackendUrl {

    //token generated during login is set here
    public static String token = "Bearer ";


    //emulator backend gateway
    // public static final   String base_url = "http://10.0.2.2:3000/";

    //for local network
   public static String  base_url = "http://192.168.0.104:3000/"; //your local IP

    //image path
    public static String imagePath = base_url + "uploads/";

    public static Retrofit getInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

}
