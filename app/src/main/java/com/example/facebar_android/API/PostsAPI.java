package com.example.facebar_android.API;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

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
    // retrofit variable
    Retrofit retrofit;
    // postApi variable
    PostAPI postAPI;
    // updating post lists retrieved from server
    private MutableLiveData<List<Post>> postListData;
    // local DB for storing the mose updated posts
    private PostDao dao;

    /**
     * Constructor for PostsAPI.
     *
     * @param postListData LiveData object to hold the list of posts
     * @param dao          Data Access Object for posts
     */
    public PostsAPI(MutableLiveData<List<Post>> postListData, PostDao dao) {
        this.postListData = postListData;
        this.dao = dao;
        // initialize Retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // create PostAPI instance
        postAPI = retrofit.create(PostAPI.class);
    }

    /**
     * Fetches the list of posts from the server and updates the local database and LiveData.
     */
    public void get() {
        // constructing the token
        String token = "bearer " + JWT.getInstance().getToken();
        // sending the call to the server
        Call<List<Post>> call = postAPI.getPosts(token);
        // creating the callback
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                // process each post in the response and initializing any null field
                for (int i = 0; i < response.body().size(); i++) {
                    if (response.body().get(i).getCommentsInt() == null)
                        response.body().get(i).setCommentsInt(new ArrayList<>());
                    if (response.body().get(i).getImageView() != null)
                        response.body().get(i).setContainsPostPic(true);
                }
                // update the local database and LiveData
                new Thread(() -> {
                    // clear old data
                    dao.clear();
                    // inserting new data
                    dao.insertList(response.body());
                    postListData.postValue(dao.index());
                }).start();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // handle errors
                System.out.println("error occurred");
            }
        });
    }

    /**
     * Adds a new post to the server and updates the local database and LiveData.
     *
     * @param post     The post to add
     * @param username The username of the user adding the post
     */
    public void add(Post post, String username) {
        // get the current active user information
        ActiveUser activeUser = ActiveUser.getInstance();
        // constructing the token
        String token = "bearer " + JWT.getInstance().getToken();
        Log.d(TAG, "add: " + post.getContent());
        Log.d(TAG, "add: " + post.getImageView());
        // sending the call to the server
        Call<Void> call = postAPI.createPost(post.getAuthor(), post.getContent(), post.getImageView(), post.getPublished(), activeUser.getProfileImage(), token);
        // creating the callback
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // assuming successful addition -> fetch updated data
                if (response.isSuccessful()) {
                    // in case we need specific user's posts we get his posts, otherwise we perform a general 'get'
                    if (Objects.equals(username, ""))
                        get();
                    else
                        getUserPost(username);
                } else if (response.code() == 403) {
                    // error adding post due to violation of the app policy
                    Toast.makeText(MyApplication.context, "Post wasn't uploaded due to inclusion of illegal links!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // handle errors
                System.out.println("error occurred");
            }
        });
    }

    /**
     * Deletes a post from the server and updates the local database and LiveData.
     *
     * @param post     The post to delete
     * @param username The username of the user deleting the post
     */
    public void delete(Post post, String username) {
        // constructing the token
        String token = "bearer " + JWT.getInstance().getToken();
        // sending the call to the server
        Call<Void> call = postAPI.deletePost(post.getAuthor(), post.get_id(), token);
        // creating the callback
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // assuming successful deletion -> fetch updated data
                if (response.isSuccessful()) {
                    // in case we need specific user's posts we get his posts, otherwise we perform a general 'get'
                    if (Objects.equals(username, ""))
                        get();
                    else
                        getUserPost(username);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // handle errors
                System.out.println("error occurred");
            }
        });
    }

    /**
     * Updates a post on the server and updates the local database and LiveData.
     *
     * @param post     The post to update
     * @param username The username of the user updating the post
     */
    public void update(Post post, String username) {
        // constructing the token
        String token = "bearer " + JWT.getInstance().getToken();
        // sending the call to the server
        Call<Void> call = postAPI.updatePost(post.getAuthor(), post.get_id(), post.getContent(), post.getImageView(),
                post.getPublished(), token);
        // creating the callback
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // assuming successful update -> fetch updated data
                if (response.isSuccessful()) {
                    // in case we need specific user's posts we get his posts, otherwise we perform a general 'get'
                    if (Objects.equals(username, ""))
                        get();
                    else
                        getUserPost(username);
                } else if (response.code() == 403) {
                    // error updating post due to violation of the app policy
                    Toast.makeText(MyApplication.context, "Values weren't updated due to inclusion of illegal links!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // handle errors
                System.out.println("error occurred");
            }
        });
    }

    /**
     * Fetches the list of posts for a specific user from the server and updates the local database and LiveData.
     *
     * @param username The username of the user whose posts to fetch
     */
    public void getUserPost(String username) {
        // constructing the token
        String token = "bearer " + JWT.getInstance().getToken();
        // sending the call to the server
        Call<List<Post>> call = postAPI.getUserPosts(username, token);
        // creating the callback
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                // process each post in the response and initializing any null field
                for (int i = 0; i < response.body().size(); i++) {
                    if (response.body().get(i).getCommentsInt() == null)
                        response.body().get(i).setCommentsInt(new ArrayList<>());
                    if (response.body().get(i).getImageView() != null)
                        response.body().get(i).setContainsPostPic(true);
                }
                // update the local database and LiveData
                new Thread(() -> {
                    // clear old data
                    dao.clear();
                    // inserting new data
                    dao.insertList(response.body());
                    postListData.postValue(dao.index());
                }).start();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // handle errors
                System.out.println("error occurred");
            }
        });
    }

    /**
     * Likes a post on the server and updates the local database and LiveData.
     *
     * @param post     The post to like
     * @param username The username of the user liking the post
     */
    public void likePost(Post post, String username) {
        // get the current active user information
        ActiveUser activeUser = ActiveUser.getInstance();
        // constructing the token
        String token = "bearer " + JWT.getInstance().getToken();
        // sending the call to the server
        Call<Void> call = postAPI.likePost(activeUser.getUsername(), post.get_id(), token);
        // creating the callback
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // assuming successful like -> fetch updated data
                if (response.isSuccessful()) {
                    // in case we need specific user's posts we get his posts, otherwise we perform a general 'get'
                    if (Objects.equals(username, ""))
                        get();
                    else
                        getUserPost(username);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // handle errors
                System.out.println("error occurred");
            }
        });
    }
}