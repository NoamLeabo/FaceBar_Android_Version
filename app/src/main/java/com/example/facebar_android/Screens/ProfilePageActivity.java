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

public class ProfilePageActivity extends AppCompatActivity {
    public static final int ADD_POST_TEXT_ONLY = 111;
    public static final int ADD_POST_BITMAP = 222;
    public static final int ADD_POST_URI = 333;
    public static final int ADD_POST_TEXT = 444;
    private final UsersAPI usersAPI = new UsersAPI();
    ImageButton friendsBtn;
    private PostViewModel viewModel;
    private SwipeRefreshLayout refreshLayout;
    private ActiveUser activeUser = ActiveUser.getInstance();
    private ProfileUser profileUser;
    private TextView private_msg;
    private boolean me;

    public Context getContext() {
        return this;
    }

    private void initializeViews() {

        // we get the RecyclerView
        RecyclerView lstPosts = findViewById(R.id.lstPosts);

        // we append the welcome msg
        TextView textView = findViewById(R.id.con_user);
        textView.append(profileUser.getUsername());
        ImageView profileImg = findViewById(R.id.profile_img);

        byte[] bytes = android.util.Base64.decode(profileUser.getProfileImage(), android.util.Base64.DEFAULT);
        // Initialize bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        // set bitmap on imageView
        profileImg.setImageBitmap(bitmap);

        //profileImg.setImageResource(activeUser.getProfileImage());
        friendsBtn = findViewById(R.id.friends_btn);
        private_msg = findViewById(R.id.private_msg);
        refreshLayout = findViewById(R.id.refreshLayout);


        if (me) {
            private_msg.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
            friendsBtn.setOnClickListener(v -> {
                ProfileUser.updateInstance(this.profileUser);
                Intent i = new Intent(this, FriendsReqActivity.class);
                startActivityForResult(i, ADD_POST_TEXT_ONLY);
            });
        } else {
            //pending friend
            if (profileUser.getPending().contains(activeUser.getUsername())) {
                if (FeedActivity.NIGHT_MODE == 0)
                    friendsBtn.setBackgroundResource(R.drawable.rec_button_pressed);
                else
                    friendsBtn.setBackgroundResource(R.drawable.rec_button_pressed_dark);

                if (FeedActivity.NIGHT_MODE == 0)
                    friendsBtn.setImageResource(R.drawable.add_friend_sign);
                else
                    friendsBtn.setImageResource(R.drawable.add_friend_sign_b);

                friendsBtn.setClickable(false);
            }
            // is friend of
            else if (profileUser.getFriends().contains(activeUser.getUsername())) {
                private_msg.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
                friendsBtn.setOnClickListener(v -> {
                    Intent i = new Intent(this, FriendsReqActivity.class);
                    i.putExtra("friend", "friend");
                    startActivityForResult(i, ADD_POST_TEXT_ONLY);
                });
            } else {
                if (FeedActivity.NIGHT_MODE == 0)
                    friendsBtn.setImageResource(R.drawable.add_friend_sign);
                else
                    friendsBtn.setImageResource(R.drawable.add_friend_sign_b);
                friendsBtn.setOnClickListener(v -> {
                    //send friends req
                    usersAPI.pendingFriend(activeUser.getUsername(), profileUser.getUsername(), new UsersAPI.AddUserCallback() {
                        @Override
                        public void onSuccess() {
                            if (FeedActivity.NIGHT_MODE == 0)
                                friendsBtn.setBackgroundResource(R.drawable.rec_button_pressed);
                            else
                                friendsBtn.setBackgroundResource(R.drawable.rec_button_pressed_dark);

                            if (friendsBtn.isClickable())
                                Toast.makeText(MyApplication.context, "Friend request was sent!", Toast.LENGTH_SHORT).show();
                            friendsBtn.setClickable(false);

                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(MyApplication.context, "Friend request was not sent!", Toast.LENGTH_SHORT).show();

                        }
                    });
                });
            }
        }

        refreshLayout.setOnRefreshListener(() -> viewModel.reloadUserPost());

        // we create a new adapter for the RecyclerView
        final PostsListAdapter adapter = new PostsListAdapter(this, viewModel);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));

//        adapter.setPosts(viewModel.getUserPosts(profileUser.getUsername()).getValue());

        viewModel.getPosts().observe(this, posts -> {
            adapter.setPosts(posts);
            adapter.updatePosts();
            refreshLayout.setRefreshing(false);
        });

        Button add_post_btn = findViewById(R.id.add_post_btn);

        if (!me) {
            add_post_btn.setVisibility(View.GONE);
        } else {
            add_post_btn.setOnClickListener(v -> {
                Intent i = new Intent(this, AddPostActivity.class);
                startActivityForResult(i, ADD_POST_TEXT_ONLY);
            });
        }
//        viewModel.reloadUserPost();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FeedActivity.NIGHT_MODE == 0)
            setContentView(R.layout.activity_profile_page);
        else
            setContentView(R.layout.activity_profile_page_dark);

        activeUser = ActiveUser.getInstance();
        profileUser = ProfileUser.getInstance();
        me = profileUser.getUsername().equals(activeUser.getUsername());

        viewModel = new PostViewModel(profileUser.getUsername());

        initializeViews();
//        viewModel.reloadUserPost();

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

    public void addPostToDB(Post post) {
        new Thread(() -> viewModel.add(post)).start();
    }

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

    private void sendResult() {
        // we send the updated comments list back to the feed screen
        Intent resultIntent = new Intent();
        setResult(999, resultIntent);
    }
}