package com.example.facebar_android;

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
            @Field("password") String password
    );
    @GET("user/{id}")
    Call<Void> getUser(@Path("id") int id);

    @PATCH("user/{id}")
    Call<Void> updateUser(@Path("id") int id);

    @DELETE("user/{id}")
    Call<Void> deleteUser(@Path("id") int id);

    @GET("user/{id}/friends")
    Call<Void> getFriends(@Path("id") int id);

    @POST("user/{id}/friends")
    Call<Void> pendingFriend(@Path("id") int id, @Body int fid);

    @PATCH("user/{id}/friends/{fid}")
    Call<Void> acceptFriend(@Path("id") int id);

    @DELETE("user/{id}/friends/{fid}")
    Call<Void> rejectFriend(@Path("id") int id);
}
