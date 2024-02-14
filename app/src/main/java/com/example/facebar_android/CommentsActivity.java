package com.example.facebar_android;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CommentsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_page);

        // Retrieve the comments passed from the previous activity
        List<Comment> comments = getIntent().getParcelableArrayListExtra("comments");

        // Initialize RecyclerView
        RecyclerView lstComments = findViewById(R.id.lstComments);

        // Set up the adapter
        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        EditText newComment = findViewById(R.id.newComment);
        final CommentListAdapter adapter = new CommentListAdapter(this, btnAdd, newComment);
        lstComments.setAdapter(adapter);
        lstComments.setLayoutManager(new LinearLayoutManager(this));

        // Set comments to the adapter
        adapter.setComments(comments);
    }
}
