package com.example.facebar_android;
import com.example.facebar_android.Posts.Post;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServiceAPI {
    @GET("api/posts")
    Call<List<Post>> getPosts(@Header("authorization") String token);
    @GET("api/users/{id}/posts")
    Call<List<Post>> getUserPosts(@Path("id") String id, @Header("authorization") String token);
    @FormUrlEncoded
    @POST("api/users/{id}/posts")
    Call<Void> createPost(@Path("id") String id, @Field("content") String content,@Field("imageView") String base,@Field("published") String date, @Field("profilePic") String profilePic  ,@Header("authorization") String token);
    @DELETE("api/users/{id}/posts/{pid}")
    Call<Void> deletePost(@Path("id") String id, @Path("pid") String pid, @Header("authorization") String token);
    @FormUrlEncoded
    @PATCH("api/users/{id}/posts/{pid}")
    Call<Void> updatePost(@Path("id") String id, @Path("pid") String pid, @Field("content") String content, @Field("imageView") String imageView, @Field("published") String published, @Header("authorization") String token);
    @POST("api/users/{id}/posts/{pid}")
    Call<Void> likePost(@Path("id") String id, @Path("pid") String pid, @Header("authorization") String token);
}
