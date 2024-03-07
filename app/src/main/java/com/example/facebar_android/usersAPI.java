package com.example.facebar_android;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import com.example.facebar_android.Screens.SubscribeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    public void addUser(String fName, String lName, String username,String password){
        Call<Void> call= userAPI.newUser(fName,lName,username,password);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "onResponse: added");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: failed");
            }
        });
    }

}
