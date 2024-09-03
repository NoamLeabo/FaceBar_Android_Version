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
import java.util.Locale;

/**
 * Activity class for displaying the feed of posts.
 * Handles the UI and interactions for the feed screen.
 */
public class FeedActivity extends AppCompatActivity {

    /* status code for when returning back to the feed screen from another intent */

    // default code
    public static final int DEF = 111;
    // adding a post with image that is saved in Bitmap format
    public static final int ADD_POST_BITMAP = 222;
    // adding a post with image that is saved in URI format
    public static final int ADD_POST_URI = 333;
    // adding a post with text only
    public static final int ADD_POST_TEXT = 444;

    // indicator whether the current theme is nightMode or not
    public static int NIGHT_MODE = 0;

    // feed posts list
    private PostViewModel viewModel;
    // posts adapter
    private PostsListAdapter adapter;
    // list layout
    private SwipeRefreshLayout refreshLayout;
    // current active user
    private ActiveUser activeUser;
    // boolean whether the main menu is open or not
    private boolean menuOpen = false;
    // user-API variable
    private UsersAPI usersAPI;

    /**
     * Returns the context of the activity.
     *
     * @return the context of the activity
     */
    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // determine with which theme to load the feed screen
        if (FeedActivity.NIGHT_MODE == 0)
            setContentView(R.layout.scrolled_feed);
        else
            setContentView(R.layout.scrolled_feed_dark);
        usersAPI = new UsersAPI();
        //loadFromJson();
        viewModel = new PostViewModel("");

        viewModel.getPosts().observe(this, posts -> {
            adapter.setPosts(posts);
            adapter.updatePosts();
            refreshLayout.setRefreshing(false);
            System.out.println("onChanged\n");
        });
        initializeViews();
    }

    /**
     * Initializes the views and sets up the UI components.
     */
    private void initializeViews() {
        // we set the activeUser values to be the current active user's
        activeUser = ActiveUser.getInstance();
        // we set the RecyclerView for displaying  feed's post
        RecyclerView lstPosts = findViewById(R.id.lstPosts);

        // we append the welcome msg and setting it at the main log
        TextView textView = findViewById(R.id.con_user);
        textView.append(getWelcome(activeUser.getUsername()));

        /*
        in case of a click on the user's name we wish to switch to his profile page, therefore we set the user which
        we want to switch to his profPage as the active user
        */
        textView.setOnClickListener(v -> usersAPI.getProfileUser(activeUser.getUsername(), new UsersAPI.AddUserCallback() {
            @Override
            public void onSuccess() {
                // we switch to the profPage
                Intent i = new Intent(getContext(), ProfilePageActivity.class);
                startActivityForResult(i, DEF);
                System.out.println("got user profile");
            }

            @Override
            public void onError(String message) {
                // in case of error we indicate so
                System.out.println("did not get user profile");
            }
        }));

        // we attach the main menu btns to vars
        ImageButton menuBtn = findViewById(R.id.menu_btn);
        ImageButton editBtn = findViewById(R.id.editBtn);
        LinearLayout menu = findViewById(R.id.TOP_START);
        ImageButton logOutBtn = findViewById(R.id.log_out_btn);
        ImageButton profilePage = findViewById(R.id.profilePage);
        ImageButton friendsBtn = findViewById(R.id.friends_btn);
        ImageButton nightModeBtn = findViewById(R.id.night_mode_btn);
        ImageView profileImg = findViewById(R.id.profile_img);
        ImageButton deleteBtn = findViewById(R.id.delete_btn);
        Button add_post_btn = findViewById(R.id.add_post_btn);


        // clicking on the friends btn will switch to the active-user's friends list
        friendsBtn.setOnClickListener(v -> {
            // the user's profile in which we want to see his friends is the activeUser so we set the ProfileUser so
            usersAPI.getProfileUser(activeUser.getUsername(), new UsersAPI.AddUserCallback() {
                @Override
                public void onSuccess() {
                    Intent i = new Intent(getContext(), FriendsReqActivity.class);
                    startActivityForResult(i, DEF);
                }

                @Override
                public void onError(String message) {
                    System.out.println("did not get user profile");
                }
            });
        });

        // clicking on the profilePage btn will switch to the active-user's profile page
        profilePage.setOnClickListener(v -> usersAPI.getProfileUser(activeUser.getUsername(), new UsersAPI.AddUserCallback() {
            @Override
            public void onSuccess() {
                Intent i = new Intent(getContext(), ProfilePageActivity.class);
                startActivityForResult(i, DEF);
                System.out.println("got user profile");
            }

            @Override
            public void onError(String message) {
                System.out.println("did not get user profile");
            }
        }));

        // clicking on the delete btn will delete the active-user from the app
        deleteBtn.setOnClickListener(v -> usersAPI.deleteUser(activeUser.getUsername(), new UsersAPI.AddUserCallback() {
            @Override
            public void onSuccess() {
                System.out.println("got user profile");
                finish();
            }

            @Override
            public void onError(String message) {
                System.out.println("did not get user profile");
            }
        }));

        // clicking on the edit btn will switch to the edit-screen where the active-user can edit his user info
        editBtn.setOnClickListener(v -> {
            Intent i = new Intent(MyApplication.context, SubscribeActivity.class);
            i.putExtra("edit", 404);
            startActivityForResult(i, 404);
        });

        // clicking on the nightMode btn will switch to the nightMode theme
        nightModeBtn.setOnClickListener(v -> {
            // call method to switch between day mode and night mode
            switchNightMode();
        });

        // clicking on the menu btn opens and closes the main menu tab
        menuBtn.setOnClickListener(v -> {
            if (menuOpen) {
                menu.setVisibility(View.GONE);
                menuOpen = false;
            } else {
                menu.setVisibility(View.VISIBLE);
                menuOpen = true;
            }
        });

        // clicking on the add_post btn switch to add-post-screen
        add_post_btn.setOnClickListener(v -> {
            Intent i = new Intent(this, AddPostActivity.class);
            startActivityForResult(i, DEF);
        });

        // clicking on the logOut btn disconnects the user and returns to the login screen
        logOutBtn.setOnClickListener(v -> finish());

        // we attach the refresh layout
        refreshLayout = findViewById(R.id.refreshLayout);
        // when refresh action has been activated we refresh the data from the viewModel
        refreshLayout.setOnRefreshListener(() -> {
            viewModel.reload();
        });

        // we set the active-user's profPic in its place at the top of the feed
        byte[] bytes= android.util.Base64.decode(activeUser.getProfileImage(), android.util.Base64.DEFAULT);
        // Initialize bitmap
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        // set bitmap on imageView
        profileImg.setImageBitmap(bitmap);

        /* we shall create an adapter for the RecyclerView in order to display to posts */

        final PostsListAdapter adapter = new PostsListAdapter(this, viewModel);
        // save the adapter as a field in order to have access to this later
        this.adapter = adapter;
        // we set this adapter as the feed-post-list's adapter
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));

        // we get current available posts from the viewModel and insert them to the adapter
        adapter.setPosts(viewModel.getPosts().getValue());
    }

    /**
     * Returns a welcome message based on the current time of day.
     *
     * @param username the username of the current user
     * @return the welcome message to display on feed
     */
    public String getWelcome(String username) {
        String time;

        // get the current hour of the day
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        // determine the time of day based on the current hour
        if (hourOfDay >= 0 && hourOfDay < 12) {
            time = "Good morning";
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            time = "Good afternoon";
        } else {
            time = "Good evening";
        }
        return time + ", " + username + "!";
    }

    /**
     * Returns the current time formatted as HH:mm, dd/MM.
     *
     * @return the current time as a string
     */
    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        // get the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
        String date = dateFormat.format(calendar.getTime());

        // construct the time string
        return String.format(Locale.getDefault(), "%02d:%02d, %s", hourOfDay, minute, date);
    }

    /**
     * Switches between day mode and night mode.
     * Updates the layout of the activity accordingly.
     */
    private void switchNightMode() {
        // toggle night mode state
        if (NIGHT_MODE == 1) {
            // switch to day mode layout
            setContentView(R.layout.scrolled_feed);
            NIGHT_MODE = 0;
        } else {
            // switch to night mode layout
            setContentView(R.layout.scrolled_feed_dark);
            NIGHT_MODE = 1;
        }
        // either way we close the main menu after switching app theme
        menuOpen = false;
        // we redraw the feed UI
        initializeViews();
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
            }
        }
        // we returned to the feed screen after adding a post with an image save in Bitmap format
        else if (resultCode == ADD_POST_BITMAP) {
            // check if data contains the content and the bitmap
            if (data != null && data.hasExtra("content") && data.hasExtra("newPic")) {
                // retrieve the content and the bitmap
                String content = data.getStringExtra("content");
                Bitmap bitmap = data.getParcelableExtra("newPic");
                // saves the image as a BitmapDrawable
                BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);

                // converting the image to Base64
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                String base = Base64.getEncoder().encodeToString(bytes);

                // create a new Post object
                Post post = new Post(activeUser.getUsername(), content, drawable, 0, this.getContext(), base);
                post.setContainsPostPic(true);
                post.setPublished(FeedActivity.getCurrentTime());
                // if this is an edited post we its date and then update the post in the DB
                if (data.getStringExtra("edit") != null) {
                    String _id = data.getStringExtra("edit");
                    post.set_id(_id);
                    post.setPublished(FeedActivity.getCurrentTime() + " edited");
                    updatePostInDB(post);
                }
                // we add the post to the DB
                else
                    addPostToDB(post);
            }
        }
        // we returned to the feed screen after adding a post with an image save in URI format
        else if (resultCode == ADD_POST_URI) {
            // check if data contains the content and the URI
            if (data != null && data.hasExtra("content") && data.hasExtra("newPic")) {
                // retrieve the content and the URI
                String content = data.getStringExtra("content");
                Uri uri = data.getParcelableExtra("newPic");

                // covert the image from the URI into a Bitmap
                Bitmap bitmap = null;
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // converting the image to Base64
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                String base = Base64.getEncoder().encodeToString(bytes);

                // create a BitmapDrawable from the Bitmap
                BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);

                // create a new Post object
                Post post = new Post(activeUser.getUsername(), content, drawable, 0, this.getContext(), base);
                post.setContainsPostPic(true);
                post.setPublished(FeedActivity.getCurrentTime());
                // if this is an edited post we its date and then update the post in the DB
                if (data.getStringExtra("edit") != null) {
                    String _id = data.getStringExtra("edit");
                    post.set_id(_id);
                    post.setPublished(FeedActivity.getCurrentTime() + " edited");
                    updatePostInDB(post);
                }
                // we add the post to the DB
                else
                    addPostToDB(post);
            }
        }
        // we returned to the feed screen after adding a post with text only
        else if (resultCode == ADD_POST_TEXT) {
            // check if data contains the content
            if (data != null && data.hasExtra("content")) {
                // retrieve the content
                String content = data.getStringExtra("content");

                // create a new Post object
                Post post = new Post(activeUser.getUsername(), content, 0, this.getContext());
                post.setPublished(FeedActivity.getCurrentTime());
                // if this is an edited post we its date and then update the post in the DB
                if (data.getStringExtra("edit") != null) {
                    String _id = data.getStringExtra("edit");
                    post.set_id(_id);
                    post.setPublished(FeedActivity.getCurrentTime() + " edited");
                    updatePostInDB(post);
                }
                // we add the post to the DB
                else
                    addPostToDB(post);
            }
        }
        // user's information might has been changed - recreate the screen frame
        else if (resultCode == 404) {
            activeUser = ActiveUser.getInstance();
            recreate();
        }
    }

    /**
     * Adds a post to the database.
     *
     * @param post the post to add
     */
    public void addPostToDB(Post post) {
        new Thread(() -> viewModel.add(post)).start();
    }

    /**
     * Updates a post in the database.
     *
     * @param post the post to update
     */
    public void updatePostInDB(Post post) {
        new Thread(() -> viewModel.edit(post)).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendResult();
    }

    @Override
    public void finish() {
        // before finishing the activity sending the results
        sendResult();
        super.finish();
    }

    /**
     * Sends the result back to the previous activity.
     */
    private void sendResult() {
        Intent resultIntent = new Intent();
        setResult(999, resultIntent);
    }
}