package com.example.facebar_android.API;

import com.example.facebar_android.Users.ActiveUser;
import com.example.facebar_android.Users.ProfileUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserAPI {
    @FormUrlEncoded
    @POST("tokens")
    Call<String> getToken(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("users")
    Call<Void> newUser(
            @Field("fName") String fName,
            @Field("lName") String lName,
            @Field("username") String username,
            @Field("password") String password,
            @Field("profileImg") String img
    );
    @GET("users/{id}")
    Call<ActiveUser> getUser(@Path("id") String id);
    @GET("users/{id}")
    Call<ProfileUser> getProfileUser(@Path("id") String id);

    @FormUrlEncoded
    @PATCH("users/{id}")
    Call<Void> updateUser(@Path("id") String id, @Field("password") String password,
                          @Field("profileImg") String img, @Header("authorization") String token);

    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") String id, @Header("authorization") String token);

    @GET("users/{id}/friends")
    Call<List<String>> getFriends(@Path("id") String id, @Header("authorization") String token);
    @FormUrlEncoded
    @POST("users/{id}/friends")
    Call<Void> pendingFriend(@Path("id") String id, @Field("fid") String fid, @Header("authorization") String token);

    @PATCH("users/{id}/friends/{fid}")
    Call<Void> acceptFriend(@Path("id") String id , @Path("fid") String fid, @Header("authorization") String token);

    @DELETE("users/{id}/friends/{fid}")
    Call<Void> rejectFriend(@Path("id") String id, @Path("fid") String fid, @Header("authorization") String token);
}
