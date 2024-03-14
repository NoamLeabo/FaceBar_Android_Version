package com.example.facebar_android.API;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.facebar_android.APP_Utilities.MyApplication;
import com.example.facebar_android.Posts.Post;
import com.example.facebar_android.Posts.PostDao;
import com.example.facebar_android.R;
import com.example.facebar_android.Users.ActiveUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostsAPI {
    private MutableLiveData<List<Post>> postListData;
    private PostDao dao;
    Retrofit retrofit;
    PostAPI postAPI;

    public PostsAPI(MutableLiveData<List<Post>> postListData, PostDao dao) {
        this.postListData = postListData;
        this.dao = dao;

        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        postAPI = retrofit.create(PostAPI.class);
    }

    public void get() {
        String token = "bearer " + JWT.getInstance().getToken();
        Call<List<Post>> call = postAPI.getPosts(token);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    if (response.body().get(i).getCommentsInt() == null)
                        response.body().get(i).setCommentsInt(new ArrayList<>());
                    if (response.body().get(i).getImageView() != null)
                        response.body().get(i).setContainsPostPic(true);
                }
                new Thread(() -> {
                    dao.clear();
                    dao.insertList(response.body());
                    postListData.postValue(dao.index());
                }).start();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                System.out.println("onFailure");

            }
        });
    }

    public void add(Post post, String username) {
        ActiveUser activeUser = ActiveUser.getInstance();
        String token = "bearer " + JWT.getInstance().getToken();
        Log.d(TAG, "add: " + post.getContent());
        Log.d(TAG, "add: " + post.getImageView());
        Call<Void> call = postAPI.createPost(post.getAuthor(), post.getContent(), post.getImageView(), post.getPublished(), activeUser.getProfileImage(), token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Assuming successful addition, fetch updated data
                if (response.isSuccessful()) {
                    if (Objects.equals(username, ""))
                        get();
                    else
                        getUserPost(username);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("onFailure");
            }
        });
    }

    public void delete(Post post, String username) {
        String token = "bearer " + JWT.getInstance().getToken();
        Call<Void> call = postAPI.deletePost(post.getAuthor(), post.get_id(), token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Assuming successful deletion, fetch updated data
                if (response.isSuccessful()) {
                    if (Objects.equals(username, ""))
                        get();
                    else
                        getUserPost(username);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("onFailure");
            }
        });
    }

    public void update(Post post, String username) {
        String token = "bearer " + JWT.getInstance().getToken();
        Call<Void> call = postAPI.updatePost(post.getAuthor(), post.get_id(), post.getContent(), post.getImageView(), post.getPublished(), token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Assuming successful deletion, fetch updated data
                if (response.isSuccessful()) {
                    if (Objects.equals(username, ""))
                        get();
                    else
                        getUserPost(username);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("onFailure");
            }
        });
    }
    public void getUserPost(String username){

        String token = "bearer " + JWT.getInstance().getToken();
        Call<List<Post>> call = postAPI.getUserPosts(username, token);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    if (response.body().get(i).getCommentsInt() == null)
                        response.body().get(i).setCommentsInt(new ArrayList<>());
                    if (response.body().get(i).getImageView() != null)
                        response.body().get(i).setContainsPostPic(true);
                }
                new Thread(() -> {
                    dao.clear();
                    dao.insertList(response.body());
                    postListData.postValue(dao.index());
                }).start();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                System.out.println("onFailure");

            }
        });
    }
    public void likePost(Post post, String username) {
        ActiveUser activeUser = ActiveUser.getInstance();
        String token = "bearer " + JWT.getInstance().getToken();
        Call<Void> call = postAPI.likePost(activeUser.getUsername(), post.get_id(), token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Assuming successful deletion, fetch updated data
                if (response.isSuccessful()) {
                    if (Objects.equals(username, ""))
                        get();
                    else
                        getUserPost(username);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("onFailure");
            }
        });
    }
}