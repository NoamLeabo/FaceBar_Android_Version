package com.example.facebar_android;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.facebar_android.Posts.Post;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostAPI {
    private MutableLiveData<List<Post>> postListData;
    private PostDao dao;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public PostAPI(MutableLiveData<List<Post>> postListData, PostDao dao) {
        this.postListData = postListData;
        this.dao = dao;

        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void get() {
        String token = "bearer " + JWT.getInstance().getToken();
        Call<List<Post>> call = webServiceAPI.getPosts(token);
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

    public void add(Post post) {
        ActiveUser activeUser = ActiveUser.getInstance();
        String token = "bearer " + JWT.getInstance().getToken();
        Log.d(TAG, "add: " + post.getContent());
        Log.d(TAG, "add: " + post.getImageView());
        Call<Void> call = webServiceAPI.createPost(post.getAuthor(), post.getContent(), post.getImageView(), post.getPublished(), activeUser.getProfileImage(), token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Assuming successful addition, fetch updated data
                if (response.isSuccessful()) {
                    System.out.println("200 OK");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("onFailure");
            }
        });
    }

    public void delete(Post post) {
        String token = "bearer " + JWT.getInstance().getToken();
        String postId = post.get_id(); // Assuming Post has getId() method to retrieve post ID
        Call<Void> call = webServiceAPI.deletePost(post.getAuthor(), post.get_id(), token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Assuming successful deletion, fetch updated data
                if (response.isSuccessful()) {
                    get();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("onFailure");
            }
        });
    }

    public void update(Post post) {
        int postId = post.getPostId(); // Assuming Post has getId() method to retrieve post ID
        String token = "bearer " + JWT.getInstance().getToken();
        Call<Void> call = webServiceAPI.updatePost(post.getAuthor(), post.get_id(), post.getContent(), post.getImageView(), post.getPublished(), token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Assuming successful deletion, fetch updated data
                if (response.isSuccessful()) {
                    get();
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
        Call<List<Post>> call = webServiceAPI.getUserPosts(username, token);
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
}