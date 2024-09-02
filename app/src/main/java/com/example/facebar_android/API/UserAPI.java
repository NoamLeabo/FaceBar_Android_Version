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

/**
 * Interface representing the User API.
 * Provides methods for user-related operations.
 */
public interface UserAPI {

    /**
     * Retrieves a token for the user.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return a Call object with the token as a String
     */
    @FormUrlEncoded
    @POST("tokens")
    Call<String> getToken(@Field("username") String username, @Field("password") String password);

    /**
     * Creates a new user.
     *
     * @param fName the first name of the user
     * @param lName the last name of the user
     * @param username the username of the user
     * @param password the password of the user
     * @param img the profile image URL of the user
     * @return a Call object
     */
    @FormUrlEncoded
    @POST("users")
    Call<Void> newUser(
            @Field("fName") String fName,
            @Field("lName") String lName,
            @Field("username") String username,
            @Field("password") String password,
            @Field("profileImg") String img
    );

    /**
     * Retrieves an active user by ID.
     *
     * @param id the user ID
     * @return a Call object with the ActiveUser
     */
    @GET("users/{id}")
    Call<ActiveUser> getUser(@Path("id") String id);

    /**
     * Retrieves a profile user by ID.
     *
     * @param id the user ID
     * @return a Call object with the ProfileUser
     */
    @GET("users/{id}")
    Call<ProfileUser> getProfileUser(@Path("id") String id);

    /**
     * Updates a user by ID.
     *
     * @param id the user ID
     * @param password the new password of the user
     * @param img the new profile image URL of the user
     * @param token the authorization token
     * @return a Call object
     */
    @FormUrlEncoded
    @PATCH("users/{id}")
    Call<Void> updateUser(@Path("id") String id, @Field("password") String password,
                          @Field("profileImg") String img, @Header("authorization") String token);

    /**
     * Deletes a user by ID.
     *
     * @param id the user ID
     * @param token the authorization token
     * @return a Call object
     */
    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") String id, @Header("authorization") String token);

    /**
     * Retrieves a list of friends for a user by ID.
     *
     * @param id the user ID
     * @param token the authorization token
     * @return a Call object with a list of friend IDs
     */
    @GET("users/{id}/friends")
    Call<List<String>> getFriends(@Path("id") String id, @Header("authorization") String token);

    /**
     * Sends a friend request to another user.
     *
     * @param id the user ID
     * @param fid the friend ID
     * @param token the authorization token
     * @return a Call object
     */
    @FormUrlEncoded
    @POST("users/{id}/friends")
    Call<Void> pendingFriend(@Path("id") String id, @Field("fid") String fid, @Header("authorization") String token);

    /**
     * Accepts a friend request from another user.
     *
     * @param id the user ID
     * @param fid the friend ID
     * @param token the authorization token
     * @return a Call object
     */
    @PATCH("users/{id}/friends/{fid}")
    Call<Void> acceptFriend(@Path("id") String id , @Path("fid") String fid, @Header("authorization") String token);

    /**
     * Rejects a friend request from another user.
     *
     * @param id the user ID
     * @param fid the friend ID
     * @param token the authorization token
     * @return a Call object
     */
    @DELETE("users/{id}/friends/{fid}")
    Call<Void> rejectFriend(@Path("id") String id, @Path("fid") String fid, @Header("authorization") String token);
}