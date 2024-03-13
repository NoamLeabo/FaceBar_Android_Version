package com.example.facebar_android.Posts;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.facebar_android.API.PostsAPI;
import com.example.facebar_android.APP_Utilities.AppDB;
import com.example.facebar_android.APP_Utilities.MyApplication;
import com.example.facebar_android.Screens.FeedActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PostRepository {
    private PostDao dao;
    private AppDB db;
    private String username;
    private PostListData postsListDate;

    private PostsAPI api;

    public PostRepository(String username) {
        this.username = username;
        db = Room.databaseBuilder(MyApplication.context, AppDB.class, "PostsDB").fallbackToDestructiveMigration().build();
        dao = db.postDao();
        postsListDate = new PostListData(username);
        api = new PostsAPI(postsListDate, dao);
    }


    class PostListData extends MutableLiveData<List<Post>> {
        private String username;

        public PostListData(String username) {
            super();
            this.username = username;
            List<Post> posts = new ArrayList<>();

            Context context = MyApplication.context;
            AssetManager assetManager = context.getAssets();

            InputStream inputStream = null;
            try {
                inputStream = assetManager.open("test.json");

                // Parse JSON array directly from InputStream
                JSONArray jsonArray = new JSONArray(inputStreamToString(inputStream));

                // Iterate over each object in the array
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    // Get values from each JSON object
                    String author = jsonObject.getString("author");
                    String content = jsonObject.getString("content");
                    String path = jsonObject.getString("path");
                    String likes = jsonObject.getString("likes");
                    int numlikes = Integer.parseInt(likes);
                    String pathPost = jsonObject.getString("pathPost");
                    //JSONArray commentsArray = jsonObject.getJSONArray("comments");

                    // Parse comments array

                    Post post = new Post(author, content, path, numlikes, pathPost);
//                    post.setComments(commentsList);
                    post.setPublished(FeedActivity.getCurrentTime());
                    // posts.add(post);
                }

            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            } finally {
                // Close the InputStream in a finally block to ensure it's always closed
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            //setting the posts' images
            new Thread(() -> dao.index()).start();
            this.setValue(posts);
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
//                  api.get();
                if (username.equals(""))
                    api.get();
                else
                    api.getUserPost(username);
            }).start();
        }
    }

    private String inputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            stringBuilder.append(new String(buffer, 0, bytesRead));
        }
        return stringBuilder.toString();
    }

    public LiveData<List<Post>> getAll() {
        return postsListDate;
    }

    public void add(final Post post) {
        new Thread(() -> {
//            dao.insert(post);
            api.add(post);
        }).start();
    }

    public void delete(final Post post) {
        new Thread(() -> {
//            dao.delete(post);
            api.delete(post);
        }).start();
    }

    public void edit(final Post post) {
        new Thread(() -> {
//            dao.update(post);
            api.update(post);
        }).start();
    }

    public void reload() {
        new Thread(() -> api.get()).start();
    }

    public void getUserPost(String username) {
        new Thread(() -> api.getUserPost(username)).start();

    }

    public void reloadUserPost() {
        new Thread(() -> api.getUserPost(username)).start();
    }

    public void likePost(Post post, String username) {
        new Thread(() -> api.likePost(post, username)).start();
    }

}
