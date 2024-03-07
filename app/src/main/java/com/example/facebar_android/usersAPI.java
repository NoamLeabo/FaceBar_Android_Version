package com.example.facebar_android;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.facebar_android.Posts.Post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class usersAPI {
    Retrofit retrofit;
    UserAPI userAPI;
    public usersAPI() {
        retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:12345/").addConverterFactory(GsonConverterFactory.create())
                .build();
        userAPI = retrofit.create(UserAPI.class);
    }
    public void addUser(String fName, String lName, String username, String password, String image, final AddUserCallback callback) {
        Call<Void> call = userAPI.newUser(fName, lName, username, password, image);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 400) {
                    try {
                        String errorMessage = response.errorBody().string();
                        callback.onError(errorMessage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    callback.onSuccess();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });

    }

    public void getUser(String userName, String password, final AddUserCallback callback) {
        Call<ActiveUser> call = userAPI.getUser(userName);
        call.enqueue(new Callback<ActiveUser>() {
            @Override
            public void onResponse(Call<ActiveUser> call, Response<ActiveUser> response) {
                if (response.code() == 200) {
                    ActiveUser user = response.body();
                    if (user.getLikedPosts() == null)
                        user.setLikedPosts(new ArrayList<>());
                    if (user.getFriends() == null)
                        user.setFriends(new ArrayList<>());
                    if (user.getPosts() == null)
                        user.setPosts(new ArrayList<>());
                    if (user.getReq() == null)
                        user.setReq(new ArrayList<>());
                    ActiveUser.updateInstance(user);
                    if (password.equals(user.getPassword()))
                        callback.onSuccess();
                    else callback.onError("password not correct");
                } else {
                    String errorMessage = null;
                    try {
                        errorMessage = response.errorBody().string();
                        callback.onError(errorMessage);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<ActiveUser> call, Throwable t) {
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });

    }
    public interface AddUserCallback {
        void onSuccess();
        void onError(String message);
    }

}
