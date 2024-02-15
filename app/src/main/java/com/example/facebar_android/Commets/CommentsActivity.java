package com.example.facebar_android.Commets;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebar_android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {
    ArrayList<Comment> comments;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_page);

        // Retrieve the comments passed from the previous activity
        this.comments = getIntent().getParcelableArrayListExtra("comments");
        this.position = getIntent().getIntExtra("position",0);

        // Initialize RecyclerView
        RecyclerView lstComments = findViewById(R.id.lstComments);

        // Set up the adapter
        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        EditText newComment = findViewById(R.id.newComment);
        final CommentListAdapter adapter = new CommentListAdapter(this, btnAdd, newComment);
        lstComments.setAdapter(adapter);
        lstComments.setLayoutManager(new LinearLayoutManager(this));

        // Set comments to the adapter
        adapter.setComments(this.comments);
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
        Intent resultIntent = new Intent();
        resultIntent.putParcelableArrayListExtra("comments", this.comments);
        resultIntent.putExtra("position", position);
        setResult(555, resultIntent);
    }


    private void sendResultBack() {

    }

}
