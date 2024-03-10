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
    @POST("api/users")
    Call<Void> newUser(
            @Field("fName") String fName,
            @Field("lName") String lName,
            @Field("username") String username,
            @Field("password") String password,
            @Field("profileImg") String img
    );
    @GET("api/users/{id}")
    Call<ActiveUser> getUser(@Path("id") String id);
    @GET("api/users/{id}")
    Call<ProfileUser> getProfileUser(@Path("id") String id);

    @FormUrlEncoded
    @PATCH("api/users/{id}")
    Call<Void> updateUser(@Path("id") String id, @Field("password") String password,
                          @Field("profileImg") String img);

    @DELETE("api/users/{id}")
    Call<Void> deleteUser(@Path("id") String id);

    @GET("api/users/{id}/friends")
    Call<List<String>> getFriends(@Path("id") String id);

    @POST("api/users/{id}/friends")
    Call<Void> pendingFriend(@Path("id") String id, @Field("fid") String fid);

    @PATCH("api/users/{id}/friends/{fid}")
    Call<Void> acceptFriend(@Path("id") String id , @Path("fid") String fid);

    @DELETE("api/users/{id}/friends/{fid}")
    Call<Void> rejectFriend(@Path("id") String id, @Path("fid") String fid);
}
