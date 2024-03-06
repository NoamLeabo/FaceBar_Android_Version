package com.example.facebar_android;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class usersAPI {

    Retrofit retrofit;
    UserAPI userAPI;

    public usersAPI(){
        retrofit=new Retrofit.Builder().baseUrl("http://10.0.2.2:12345/").addConverterFactory(GsonConverterFactory.create())
                .build();
        userAPI=retrofit.create(UserAPI.class);
    }
    
}
