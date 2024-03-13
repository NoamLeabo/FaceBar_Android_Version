package com.example.facebar_android.Screens;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.facebar_android.Users.ActiveUser;
import com.example.facebar_android.APP_Utilities.MyApplication;
import com.example.facebar_android.Posts.PostViewModel;
import com.example.facebar_android.Posts.Post;
import com.example.facebar_android.Posts.PostsListAdapter;
import com.example.facebar_android.R;
import com.example.facebar_android.API.UsersAPI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class FeedActivity extends AppCompatActivity {
    public static final int ADD_POST_TEXT_ONLY = 111;
    public static final int ADD_POST_BITMAP = 222;
    public static final int ADD_POST_URI = 333;
    public static final int ADD_POST_TEXT = 444;
    public static int NIGHT_MODE = 0;


    private PostViewModel viewModel;
    private PostsListAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private List<Post> posts = new ArrayList<>();
    private ActiveUser activeUser;
    private boolean menuOpen = false;
    private UsersAPI usersAPI;


    public Context getContext() {
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

    public static int getTimeInt() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);

        // Get the current date
        int timeInt = hourOfDay + minute + calendar.getTime().getDay() + calendar.getTime().getMonth() +sec;

        // Construct the time string
        return timeInt;
    }

    private void initializeViews() {
        activeUser = ActiveUser.getInstance();
        // we get the RecyclerView
        RecyclerView lstPosts = findViewById(R.id.lstPosts);

        // we append the welcome msg
        TextView textView = findViewById(R.id.con_user);
        textView.append(getWelcome(activeUser.getUsername()));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersAPI.getProfileUser(activeUser.getUsername(), new UsersAPI.AddUserCallback() {
                    @Override
                    public void onSuccess() {
                        Intent i = new Intent(getContext(), ProfilePageActivity.class);
                        startActivityForResult(i, ADD_POST_TEXT_ONLY);
                        System.out.println("got user profile");
                    }

                    @Override
                    public void onError(String message) {
                        System.out.println("did not get user profile");
                    }
                });
            }
        });

        ImageButton menuBtn = findViewById(R.id.menu_btn);
        ImageButton editBtn = findViewById(R.id.editBtn);
        LinearLayout menu = findViewById(R.id.TOP_START);
        ImageButton logOutBtn = findViewById(R.id.log_out_btn);
        ImageButton profilePage = findViewById(R.id.profilePage);
        ImageButton friendsBtn = findViewById(R.id.friends_btn);

        friendsBtn.setOnClickListener(v -> {

            usersAPI.getProfileUser(activeUser.getUsername(), new UsersAPI.AddUserCallback() {
                @Override
                public void onSuccess() {
                    Intent i = new Intent(getContext(), FriendsReqActivity.class);
                    startActivityForResult(i, ADD_POST_TEXT_ONLY);
                }

                @Override
                public void onError(String message) {
                    System.out.println("did not get user profile");
                }
            });
        });
        profilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersAPI.getProfileUser(activeUser.getUsername(), new UsersAPI.AddUserCallback() {
                    @Override
                    public void onSuccess() {
                        Intent i = new Intent(getContext(), ProfilePageActivity.class);
                        startActivityForResult(i, ADD_POST_TEXT_ONLY);
                        System.out.println("got user profile");
                    }

                    @Override
                    public void onError(String message) {
                        System.out.println("did not get user profile");
                    }
                });
            }
        });

        ImageButton deleteBtn = findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersAPI.deleteUser(activeUser.getUsername(), new UsersAPI.AddUserCallback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("got user profile");
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        System.out.println("did not get user profile");
                    }
                });
            }
        });

        ImageButton nightModeBtn = findViewById(R.id.night_mode_btn);
        ImageView profileImg = findViewById(R.id.profile_img);

        byte[] bytes= android.util.Base64.decode(activeUser.getProfileImage(), android.util.Base64.DEFAULT);
        // Initialize bitmap
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        // set bitmap on imageView
        profileImg.setImageBitmap(bitmap);

        nightModeBtn.setOnClickListener(v -> {
            // Call method to switch between day mode and night mode
            switchNightMode();
        });

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(() -> {
            viewModel.reload();
        });

        editBtn.setOnClickListener(v -> {
            Intent i = new Intent(MyApplication.context, SubscribeActivity.class);
            i.putExtra("edit", 404);
            startActivityForResult(i, 404);
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
        final PostsListAdapter adapter = new PostsListAdapter(this, viewModel);
        this.adapter = adapter;
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));

        adapter.setPosts(viewModel.getPosts().getValue());

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
        usersAPI = new UsersAPI();
        //loadFromJson();
        viewModel = new PostViewModel("");

        viewModel.getPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                adapter.setPosts(posts);
                adapter.updatePosts();
                refreshLayout.setRefreshing(false);
                System.out.println("onChanged\n");
            }
        });
        initializeViews();

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
            if (data != null && data.hasExtra("newIds") && data.hasExtra("position")) {
                ArrayList<Integer> newIds = data.getIntegerArrayListExtra("newIds");
                int position = data.getIntExtra("position", 0);
                Post commentsChanged = viewModel.getPosts().getValue().get(position);
                commentsChanged.setCommentsInt(newIds);
                viewModel.edit(commentsChanged);
//                adapter.updatePosts();
            }
        } else if (resultCode == ADD_POST_BITMAP) {
            // Check if data contains the content and the bitmap
            if (data != null && data.hasExtra("content") && data.hasExtra("newPic")) {
                // Retrieve the content and the bitmap
                String content = data.getStringExtra("content");
                Bitmap bitmap = data.getParcelableExtra("newPic");

                BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);

                ByteArrayOutputStream stream=new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte[] bytes=stream.toByteArray();
                String base = Base64.getEncoder().encodeToString(bytes);

                // Create a new Post object
                Post post = new Post(activeUser.getUsername(), content, drawable, 0, this.getContext(), base);
                post.setContainsPostPic(true);
                post.setPublished(FeedActivity.getCurrentTime());
                if (data.getStringExtra("edit") != null) {
                    String _id = data.getStringExtra("edit");
                    post.set_id(_id);
                    post.setPublished(FeedActivity.getCurrentTime() + " edited");
                    updatePostInDB(post);
                }
                else
                    addPostToDB(post);//                adapter.updatePosts();
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

                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte[] bytes=stream.toByteArray();
                String base = Base64.getEncoder().encodeToString(bytes);

                // Create a BitmapDrawable from the Bitmap
                BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);

                // Create a new Post object
                Post post = new Post(activeUser.getUsername(), content, drawable, 0, this.getContext(), base);
                post.setContainsPostPic(true);
                post.setPublished(FeedActivity.getCurrentTime());
                if (data.getStringExtra("edit") != null) {
                    String _id = data.getStringExtra("edit");
                    post.set_id(_id);
                    post.setPublished(FeedActivity.getCurrentTime() + " edited");
                    updatePostInDB(post);
                }
                else
                    addPostToDB(post);//                 // adapter.updatePosts();
                // Use the content and the bitmap as needed
                // For example, display the content in a TextView
                // and set the bitmap to an ImageView
            }
        } else if (resultCode == ADD_POST_TEXT) {
            if (data != null && data.hasExtra("content")) {
                // Retrieve the content and the bitmap
                String content = data.getStringExtra("content");

                Post post = new Post(activeUser.getUsername(), content, 0, this.getContext());
                post.setPublished(FeedActivity.getCurrentTime());
                if (data.getStringExtra("edit") != null) {
                    String _id = data.getStringExtra("edit");
                    post.set_id(_id);
                    post.setPublished(FeedActivity.getCurrentTime() + " edited");
                    updatePostInDB(post);
                }
                else
                    addPostToDB(post);// //                adapter.updatePosts();
                // Use the content and the bitmap as needed
                // For example, display the content in a TextView
                // and set the bitmap to an ImageView
            }
        } else if (resultCode == 404) {
            activeUser = ActiveUser.getInstance();
            recreate();
        }
    }

    public void addPostToDB(Post post) {
        new Thread(() -> {
            viewModel.add(post);
        }).start();
    }
    public void updatePostInDB(Post post) {
        new Thread(() -> {
            viewModel.edit(post);
        }).start();
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