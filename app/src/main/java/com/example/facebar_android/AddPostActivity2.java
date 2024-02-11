package com.example.facebar_android;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AddPostActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        Post post = new Post();
        EditText editText = findViewById(R.id.tvWrite);


        List<Post> posts = getIntent().getParcelableArrayListExtra("posts");

        if (posts == null) {
            posts = new ArrayList<>();
        }



        ImageButton post_post_btn = findViewById(R.id.post_post_btn);

        List<Post> finalPosts = posts;
        post_post_btn.setOnClickListener(v -> {
            post.setContent(String.valueOf(editText.getText()));
            post.setAuthor("Noam");
            post.setPic(R.drawable.pic3);
            finalPosts.add(post);
            returnToOldActivity();
        });
    }

    // Method to return to the previous activity
    private void returnToOldActivity() {
        finish(); // This finishes the current activity and returns to the previous one
    }
}