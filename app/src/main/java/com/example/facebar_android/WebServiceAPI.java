package com.example.facebar_android;


import androidx.room.Delete;
import androidx.room.Update;

import com.example.facebar_android.Posts.Post;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServiceAPI {
    @GET("posts")
    Call<List<Post>> getPosts();
    @GET("users/id/posts")
    Call<List<Post>> getUserPosts(@Path("id") String id);
    @POST("users/id/posts")
    Call<Void> createPost(@Path("id") String id, @Body Post post);
    @DELETE("users/{id}/posts/{pid}")
    Call<Void> deletePost(@Path("id") String id, @Path("pid") int pid);
    @PUT("users/{id}/posts/{pid}")
    Call<Void> updatePost(@Path("id") String id, @Path("pid") int pid, @Body Post post);
}