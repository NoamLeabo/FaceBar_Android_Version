package com.example.facebar_android;

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

import com.example.facebar_android.Posts.AddPostActivity;
import com.example.facebar_android.Posts.Post;
import com.example.facebar_android.Posts.PostsListAdapter;
import com.example.facebar_android.Screens.FeedActivity;

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
    private PostViewModel viewModel;
    private PostsListAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private List<Post> posts = new ArrayList<>();
    public Context getContext() {
        return this;
    }
    private ActiveUser activeUser;
    private ProfileUser profileUser;
    private boolean me;
    ImageButton friendsBtn;
    private void initializeViews() {

        // we get the RecyclerView
        RecyclerView lstPosts = findViewById(R.id.lstPosts);

        // we append the welcome msg
        TextView textView = findViewById(R.id.con_user);
        textView.append(profileUser.getUsername());
        ImageView profileImg = findViewById(R.id.profile_img);

        byte[] bytes= android.util.Base64.decode(profileUser.getProfileImage(), android.util.Base64.DEFAULT);
        // Initialize bitmap
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        // set bitmap on imageView
        profileImg.setImageBitmap(bitmap);

        //profileImg.setImageResource(activeUser.getProfileImage());
        friendsBtn = findViewById(R.id.friends_btn);

        if (me) {
            friendsBtn.setOnClickListener(v -> {
                Intent i = new Intent(this, FriendsReqActivity.class);
                startActivityForResult(i, ADD_POST_TEXT_ONLY);
            });
        } else {
            if (activeUser.getFriends().contains(profileUser.getUsername()) || activeUser.getPendings().contains(profileUser.getUsername())) {
                friendsBtn.setBackgroundResource(R.drawable.rec_button_pressed);
                friendsBtn.setClickable(false);
            } else {
                friendsBtn.setImageResource(R.drawable.add_friend_sign);
                friendsBtn.setOnClickListener(v -> {
                    //send friends req
                    friendsBtn.setBackgroundResource(R.drawable.rec_button_pressed);
                    if (friendsBtn.isClickable())
                        Toast.makeText(this, "Friend request was sent!", Toast.LENGTH_SHORT).show();
                    friendsBtn.setClickable(false);


                });
            }
        }

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(() -> {
            viewModel.reload();
        });

        // we create a new adapter for the RecyclerView
        final PostsListAdapter adapter = new PostsListAdapter(this, viewModel);
        this.adapter = adapter;
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));

        adapter.setPosts(viewModel.getPosts().getValue());

        Button add_post_btn = findViewById(R.id.add_post_btn);

        if (!me) {
            add_post_btn.setVisibility(View.GONE);
        } else {
            add_post_btn.setOnClickListener(v -> {
                Intent i = new Intent(this, AddPostActivity.class);
                startActivityForResult(i, ADD_POST_TEXT_ONLY);
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (FeedActivity.NIGHT_MODE == 0)
//            setContentView(R.layout.scrolled_feed);
//        else
//            setContentView(R.layout.scrolled_feed_dark);

        activeUser = ActiveUser.getInstance();
        profileUser = ProfileUser.getInstance();
        setContentView(R.layout.activity_profile_page);
        me = profileUser.getUsername().equals(activeUser.getUsername());

        viewModel = new PostViewModel(profileUser.getUsername());

        viewModel.getPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                adapter.setPosts(posts);
                adapter.updatePosts();
                refreshLayout.setRefreshing(false);
            }
        });
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
                addPostToDB(post);//
                // adapter.updatePosts();
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
                addPostToDB(post);
//                adapter.updatePosts();
                // Use the content and the bitmap as needed
                // For example, display the content in a TextView
                // and set the bitmap to an ImageView
            }
        }
    }

    public void addPostToDB(Post post) {
        new Thread(() -> {
            viewModel.add(post);
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