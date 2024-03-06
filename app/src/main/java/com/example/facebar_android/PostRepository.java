package com.example.facebar_android;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.facebar_android.Commets.Comment;
import com.example.facebar_android.Posts.Post;
import com.example.facebar_android.Posts.PostsListAdapter;
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
    private PostListData postsListDate;

//    private PostAPI api;

    public PostRepository() {
        db = Room.databaseBuilder(MyApplication.context, AppDB.class, "PostsDB").build();
        dao = db.postDao();
        postsListDate = new PostListData();
        //api = new PostAPI(postsListDate, dao);
    }


    class PostListData extends MutableLiveData<List<Post>> {
        public PostListData() {
            super();

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
//                    ArrayList<Comment> commentsList = new ArrayList<>();
//                    for (int j = 0; j < commentsArray.length(); j++) {
//                        JSONObject commentObject = commentsArray.getJSONObject(j);
//                        String commentAuthor = commentObject.getString("author");
//                        String commentContent = commentObject.getString("content");
//                        Comment comment = new Comment(commentAuthor, commentContent);
//                        commentsList.add(comment);
//                    }

                    Post post = new Post(author, content, path, numlikes, pathPost);
//                    post.setComments(commentsList);
                    post.setDate(FeedActivity.getCurrentTime());
                    posts.add(post);
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
//            for (int i = 0; i < posts.size(); i++) {
//                Post post = posts.get(i);
//                try {
//                    InputStream stream = assetManager.open(post.getPath());
//                    Drawable drawable = Drawable.createFromStream(stream, null);
//                    post.setProfPic(drawable);
//
//                    stream = assetManager.open(post.getPathPostPic());
//                    Drawable drawablePost = Drawable.createFromStream(stream, null);
//                    post.setPostPic(drawablePost);
//                    post.setContainsPostPic();
//                    stream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            new Thread(() -> {
//                dao.insertList(posts);
//            }).start();
//            this.setValue(posts);
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                postsListDate.postValue(dao.index());
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
            dao.insert(post);
        }).start();
    }

    public void delete(final Post post) {
        new Thread(() -> {
            dao.delete(post);
        }).start();
    }

    public void edit(final Post post) {
        new Thread(() -> {
            dao.update(post);
        }).start();
    }

    public void reload() {
        new Thread(() -> {
            postsListDate.postValue(dao.index());
        }).start();
    }
}
