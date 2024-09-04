package com.example.facebar_android.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.facebar_android.APP_Utilities.MyApplication;
import com.example.facebar_android.Posts.PostViewModel;
import com.example.facebar_android.R;
import com.example.facebar_android.Posts.Post;
import com.example.facebar_android.Posts.PostsListAdapter;
import com.example.facebar_android.Users.ActiveUser;
import com.example.facebar_android.Users.ProfileUser;
import com.example.facebar_android.API.UsersAPI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Activity class for displaying the profile page.
 * Handles the UI and interactions for the profile screen.
 */
public class ProfilePageActivity extends AppCompatActivity {
    // default code
    public static final int DEF = 111;
    // adding a post with image that is saved in Bitmap format
    public static final int ADD_POST_BITMAP = 222;
    // adding a post with image that is saved in URI format
    public static final int ADD_POST_URI = 333;
    // adding a post with text only
    public static final int ADD_POST_TEXT = 444;
    // user-API variable
    private final UsersAPI usersAPI = new UsersAPI();
    // user's friends list btn
    ImageButton friendsBtn;
    // user's post list
    private PostViewModel viewModel;
    // list layout
    private SwipeRefreshLayout refreshLayout;
    // current active user
    private ActiveUser activeUser = ActiveUser.getInstance();
    // current user's profile viewed
    private ProfileUser profileUser;
    // string to present whether the profile is private and can be seen
    private TextView private_msg;
    // whether the profile viewed the the active user's
    private boolean me;

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
            setContentView(R.layout.activity_profile_page);
        else
            setContentView(R.layout.activity_profile_page_dark);

        // set the active user and the user's profile that's being viewed
        activeUser = ActiveUser.getInstance();
        profileUser = ProfileUser.getInstance();

        // check whether the active user views his own profile
        me = profileUser.getUsername().equals(activeUser.getUsername());

        // set the viewModel
        viewModel = new PostViewModel(profileUser.getUsername());

        // initiate UI
        initializeViews();
    }

    /**
     * Initializes the views and sets up the UI components.
     */
    private void initializeViews() {
        // we set the RecyclerView for displaying  feed's post
        RecyclerView lstPosts = findViewById(R.id.lstPosts);

        // we append the welcome msg and setting it at the main log
        TextView textView = findViewById(R.id.con_user);
        textView.append(profileUser.getUsername());

        // we attach the main menu btns to vars
        friendsBtn = findViewById(R.id.friends_btn);
        private_msg = findViewById(R.id.private_msg);
        refreshLayout = findViewById(R.id.refreshLayout);
        ImageView profileImg = findViewById(R.id.profile_img);
        Button add_post_btn = findViewById(R.id.add_post_btn);

        // we set the active-user's profPic in its place at the top of the feed
        byte[] bytes = android.util.Base64.decode(profileUser.getProfileImage(), android.util.Base64.DEFAULT);
        // Initialize bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        // set bitmap on imageView
        profileImg.setImageBitmap(bitmap);

        // in case of self-view of the profile
        if (me) {
            // we allow full view of the profile
            private_msg.setVisibility(View.GONE);
            // display user's posts
            refreshLayout.setVisibility(View.VISIBLE);
            // activation of friends btn - to see the user's friends
            friendsBtn.setOnClickListener(v -> {
                ProfileUser.updateInstance(this.profileUser);
                Intent i = new Intent(this, FriendsReqActivity.class);
                startActivityForResult(i, DEF);
            });
        }
        // in case the viewed user is not friend of active user
        else {
            // in case the viewed user is not friend of active user but the latter has already sent a friends req
            if (profileUser.getPending().contains(activeUser.getUsername())) {
                // since the active user's request is pending we do not allow sending more requests
                if (FeedActivity.NIGHT_MODE == 0) {
                    friendsBtn.setBackgroundResource(R.drawable.rec_button_pressed);
                    friendsBtn.setImageResource(R.drawable.add_friend_sign);
                } else {
                    friendsBtn.setBackgroundResource(R.drawable.rec_button_pressed_dark);
                    friendsBtn.setImageResource(R.drawable.add_friend_sign_b);
                }
                // sending requests btn is off
                friendsBtn.setClickable(false);
            }
            // in case the viewed user is friend of active user
            else if (profileUser.getFriends().contains(activeUser.getUsername())) {
                // we allow full view of the profile
                private_msg.setVisibility(View.GONE);
                // display user's posts
                refreshLayout.setVisibility(View.VISIBLE);
                // activation of friends btn - to see the user's friends
                friendsBtn.setOnClickListener(v -> {
                    Intent i = new Intent(this, FriendsReqActivity.class);
                    i.putExtra("friend", "friend");
                    startActivityForResult(i, DEF);
                });
            }
            // in case the viewed user is not friend of active user and the latter yet has not sent a friends req
            else {
                // we turn on the btn ti send a friends request
                if (FeedActivity.NIGHT_MODE == 0)
                    friendsBtn.setImageResource(R.drawable.add_friend_sign);
                else
                    friendsBtn.setImageResource(R.drawable.add_friend_sign_b);

                // when the btn is pressed a friends req is sent
                friendsBtn.setOnClickListener(v -> {
                    // a request from the server to add the active user to the pending list of the viewed user
                    usersAPI.pendingFriend(activeUser.getUsername(), profileUser.getUsername(), new UsersAPI.AddUserCallback() {
                        @Override
                        public void onSuccess() {
                            // we shall now turn off the btn since it is not possible to send more than one request
                            if (FeedActivity.NIGHT_MODE == 0)
                                friendsBtn.setBackgroundResource(R.drawable.rec_button_pressed);
                            else
                                friendsBtn.setBackgroundResource(R.drawable.rec_button_pressed_dark);
                            friendsBtn.setClickable(false);

                            // we display that the request has been sent
                            if (friendsBtn.isClickable())
                                Toast.makeText(MyApplication.context, "Friend request was sent!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String message) {
                            // handle error with the server req
                            Toast.makeText(MyApplication.context, "Friend request was not sent!", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        }

        // on refresh action we set a post refresh
        refreshLayout.setOnRefreshListener(() -> viewModel.reloadUserPost());

        /* we shall create an adapter for the RecyclerView in order to display to posts */

        final PostsListAdapter adapter = new PostsListAdapter(this, viewModel);
        // we set this adapter as the feed-post-list's adapter
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));

        // set the auto update of the post in the feed
        viewModel.getPosts().observe(this, posts -> {
            adapter.setPosts(posts);
            adapter.updatePosts();
            refreshLayout.setRefreshing(false);
        });

        // in case of self-view of the profile we add a btn for the user to add post from his profile page
        if (!me) {
            add_post_btn.setVisibility(View.GONE);
        } else {
            add_post_btn.setOnClickListener(v -> {
                Intent i = new Intent(this, AddPostActivity.class);
                startActivityForResult(i, DEF);
            });
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
        sendResult();
        super.finish();
    }

    /**
     * Sends the result back to the previous activity.
     */
    private void sendResult() {
        // send the updated results back
        Intent resultIntent = new Intent();
        setResult(999, resultIntent);
    }
}