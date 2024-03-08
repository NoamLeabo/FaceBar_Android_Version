package com.example.facebar_android;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserAPI {
    @FormUrlEncoded
    @POST("user")
    Call<Void> newUser(
            @Field("fName") String fName,
            @Field("lName") String lName,
            @Field("username") String username,
            @Field("password") String password,
            @Field("profileImg") String img
    );
    @GET("user/{id}")
    Call<ActiveUser> getUser(@Path("id") String id);
    @GET("user/{id}")
    Call<ProfileUser> getProfileUser(@Path("id") String id);

    @PATCH("user/{id}")
    Call<Void> updateUser(@Path("id") String id);

    @DELETE("user/{id}")
    Call<Void> deleteUser(@Path("id") String id);

    @GET("user/{id}/friends")
    Call<List> getFriends(@Path("id") String id);

    @POST("user/{id}/friends")
    Call<Void> pendingFriend(@Path("id") String id, @Body String fid);

    @PATCH("user/{id}/friends/{fid}")
    Call<Void> acceptFriend(@Path("id") String id);

    @DELETE("user/{id}/friends/{fid}")
    Call<Void> rejectFriend(@Path("id") String id);
}
