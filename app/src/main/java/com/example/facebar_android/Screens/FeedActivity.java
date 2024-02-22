package com.example.facebar_android.Screens;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebar_android.ActiveUser;
import com.example.facebar_android.Commets.Comment;
import com.example.facebar_android.Posts.AddPostActivity;
import com.example.facebar_android.Posts.Post;
import com.example.facebar_android.Posts.PostsListAdapter;
import com.example.facebar_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class FeedActivity extends AppCompatActivity {
    public static final int ADD_POST_TEXT_ONLY = 111;
    public static final int ADD_POST_BITMAP = 222;
    public static final int ADD_POST_URI = 333;
    public static final int ADD_POST_TEXT = 444;
    public static int NIGHT_MODE = 0;



    private PostsListAdapter adapter;
    private List<Post> posts = new ArrayList<>();
    private ActiveUser activeUser;
    private boolean menuOpen = false;


    public Context getContext(){
        return this;
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

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        // Get the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
        String date = dateFormat.format(calendar.getTime());

        // Construct the time string
        return String.format(Locale.getDefault(), "%02d:%02d, %s", hourOfDay, minute, date);
    }
    private void initializeViews() {
        activeUser = new ActiveUser("Mark", "Zuckerberg", "Mark Z", "123456", R.drawable.zukiprofile);

        // we get the RecyclerView
        RecyclerView lstPosts = findViewById(R.id.lstPosts);

        // we append the welcome msg
        TextView textView = findViewById(R.id.con_user);
        textView.append(getWelcome(activeUser.getUsername()));
        ImageButton menuBtn = findViewById(R.id.menu_btn);
        LinearLayout menu = findViewById(R.id.TOP_START);
        ImageButton logOutBtn = findViewById(R.id.log_out_btn);
        ImageButton nightModeBtn = findViewById(R.id.night_mode_btn);
        ImageView profileImg = findViewById(R.id.profile_img);
        profileImg.setImageResource(activeUser.getProfileImage());
        nightModeBtn.setOnClickListener(v -> {
            // Call method to switch between day mode and night mode
            switchNightMode();
        });

        menuBtn.setOnClickListener(v -> {
            if (menuOpen) {
                menu.setVisibility(View.GONE);
                menuOpen = false;
            } else {
                menu.setVisibility(View.VISIBLE);
                menuOpen = true;
            }
        });
        logOutBtn.setOnClickListener(v -> finish());

        // we create a new adapter for the RecyclerView
        final PostsListAdapter adapter = new PostsListAdapter(this);
        this.adapter = adapter;
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));

        adapter.setPosts(posts);

        Button add_post_btn = findViewById(R.id.add_post_btn);

        add_post_btn.setOnClickListener(v -> {
            Intent i = new Intent(this, AddPostActivity.class);
            startActivityForResult(i, ADD_POST_TEXT_ONLY);
        });
    }

    public String getWelcome(String username) {
        String time;

        // Get the current hour of the day
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        // Determine the time of day based on the current hour
        if (hourOfDay >= 0 && hourOfDay < 12) {
            time = "Good morning";
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            time = "Good afternoon";
        } else {
            time = "Good evening";
        }
        return time + ", " + username + "!";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FeedActivity.NIGHT_MODE == 0)
            setContentView(R.layout.scrolled_feed);
        else
            setContentView(R.layout.scrolled_feed_dark);

        loadFromJson();

        initializeViews();
    }

    private void loadFromJson(){
        Context context = getApplicationContext();
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
                JSONArray commentsArray = jsonObject.getJSONArray("comments");

                // Parse comments array
                ArrayList<Comment> commentsList = new ArrayList<>();
                for (int j = 0; j < commentsArray.length(); j++) {
                    JSONObject commentObject = commentsArray.getJSONObject(j);
                    String commentAuthor = commentObject.getString("author");
                    String commentContent = commentObject.getString("content");
                    Comment comment = new Comment(commentAuthor, commentContent);
                    commentsList.add(comment);
                }

                Post post = new Post(author, content, path, numlikes, pathPost);
                post.setComments(commentsList);
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

        // setting the posts' images
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            try {
                InputStream stream = assetManager.open(post.getPath());
                Drawable drawable = Drawable.createFromStream(stream, null);
                post.setProfPic(drawable);

                stream = assetManager.open(post.getPathPostPic());
                Drawable drawablePost = Drawable.createFromStream(stream, null);
                post.setPostPic(drawablePost);
                post.setContainsPostPic();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void switchNightMode() {
        // Toggle night mode state or implement logic to switch UI elements
        // Here you should update the layout of your activity to the night mode version
        if (NIGHT_MODE == 1) {
            // Switch to day mode layout
            setContentView(R.layout.scrolled_feed);
            NIGHT_MODE = 0;
            menuOpen = false;
            initializeViews();
        } else {
            // Switch to night mode layout
            setContentView(R.layout.scrolled_feed_dark);
            NIGHT_MODE = 1;
            menuOpen = false;
            initializeViews();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 555) {
            if (data != null && data.hasExtra("comments") && data.hasExtra("position")) {
                ArrayList<Comment> comments = data.getParcelableArrayListExtra("comments");
                int position = data.getIntExtra("position", 0);
                posts.get(position).setComments(comments);
                adapter.updatePosts();
            }
        }
        else if (resultCode == ADD_POST_BITMAP) {
            // Check if data contains the content and the bitmap
            if (data != null && data.hasExtra("content") && data.hasExtra("newPic")) {
                // Retrieve the content and the bitmap
                String content = data.getStringExtra("content");
                Bitmap bitmap = data.getParcelableExtra("newPic");
                BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);

                // Create a new Post object
                Post post = new Post("Mark Z.", content, drawable, 0, this.getContext());
                post.setContainsPostPic();
                posts.add(post);
                adapter.updatePosts();
                // Use the content and the bitmap as needed
                // For example, display the content in a TextView
                // and set the bitmap to an ImageView
            }
        } else if (resultCode == ADD_POST_URI) {
            // Check if data contains the content and the bitmap
            if (data != null && data.hasExtra("content") && data.hasExtra("newPic")) {
                // Retrieve the content and the bitmap
                String content = data.getStringExtra("content");
                Uri uri = data.getParcelableExtra("newPic");
                // Load the image from the URI into a Bitmap
                Bitmap bitmap = null;
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Create a BitmapDrawable from the Bitmap
                BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);

                // Create a new Post object
                Post post = new Post("Mark Z.", content, drawable, 0, this.getContext());
                post.setContainsPostPic();
                post.setContainsPostPic();
                posts.add(post);
                adapter.updatePosts();
                // Use the content and the bitmap as needed
                // For example, display the content in a TextView
                // and set the bitmap to an ImageView
            }
        } else if (resultCode == ADD_POST_TEXT) {
            if (data != null && data.hasExtra("content")) {
                // Retrieve the content and the bitmap
                String content = data.getStringExtra("content");

                Post post = new Post("Mark Z.", content, 0, this.getContext());
                posts.add(post);
                adapter.updatePosts();
                // Use the content and the bitmap as needed
                // For example, display the content in a TextView
                // and set the bitmap to an ImageView
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendResult();
    }

    @Override
    public void finish() {
        sendResult();
        super.finish();
    }

    private void sendResult() {
        // we send the updated comments list back to the feed screen
        Intent resultIntent = new Intent();
        setResult(999, resultIntent);
    }
}