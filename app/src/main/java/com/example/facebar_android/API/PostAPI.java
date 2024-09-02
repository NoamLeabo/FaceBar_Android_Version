package com.example.facebar_android.API;

import com.example.facebar_android.Posts.Post;

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
 * Interface representing the Post API.
 * Provides methods for interacting with posts.
 */
public interface PostAPI {

    /**
     * Retrieves a list of posts.
     *
     * @param token the authorization token
     * @return a Call object with a list of posts
     */
    @GET("posts")
    Call<List<Post>> getPosts(@Header("authorization") String token);

    /**
     * Retrieves a list of posts for a specific user.
     *
     * @param id the user ID
     * @param token the authorization token
     * @return a Call object with a list of posts
     */
    @GET("users/{id}/posts")
    Call<List<Post>> getUserPosts(@Path("id") String id, @Header("authorization") String token);

    /**
     * Creates a new post for a specific user.
     *
     * @param id the user ID
     * @param content the content of the post
     * @param base the base64 encoded image
     * @param date the publication date
     * @param profilePic the profile picture URL
     * @param token the authorization token
     * @return a Call object
     */
    @FormUrlEncoded
    @POST("users/{id}/posts")
    Call<Void> createPost(@Path("id") String id, @Field("content") String content, @Field("imageView") String base, @Field("published") String date, @Field("profilePic") String profilePic, @Header("authorization") String token);

    /**
     * Deletes a specific post for a user.
     *
     * @param id the user ID
     * @param pid the post ID
     * @param token the authorization token
     * @return a Call object
     */
    @DELETE("users/{id}/posts/{pid}")
    Call<Void> deletePost(@Path("id") String id, @Path("pid") String pid, @Header("authorization") String token);

    /**
     * Updates a specific post for a user.
     *
     * @param id the user ID
     * @param pid the post ID
     * @param content the updated content of the post
     * @param imageView the updated base64 encoded image
     * @param published the updated publication date
     * @param token the authorization token
     * @return a Call object
     */
    @FormUrlEncoded
    @PATCH("users/{id}/posts/{pid}")
    Call<Void> updatePost(@Path("id") String id, @Path("pid") String pid, @Field("content") String content, @Field("imageView") String imageView, @Field("published") String published, @Header("authorization") String token);

    /**
     * Likes a specific post for a user.
     *
     * @param id the user ID
     * @param pid the post ID
     * @param token the authorization token
     * @return a Call object
     */
    @POST("users/{id}/posts/{pid}")
    Call<Void> likePost(@Path("id") String id, @Path("pid") String pid, @Header("authorization") String token);
}