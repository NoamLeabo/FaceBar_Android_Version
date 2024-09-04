package com.example.facebar_android.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.facebar_android.Commets.CommentViewModel;
import com.example.facebar_android.Commets.Comment;
import com.example.facebar_android.Commets.CommentListAdapter;
import com.example.facebar_android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {
    int position;
    private ArrayList<Integer> comments;
    private CommentViewModel viewModel;
    private SwipeRefreshLayout refreshLayout;
    //private CommentViewModel viewModel;
    private ArrayList<Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // we display the comments overall layout
        if (FeedActivity.NIGHT_MODE == 0)
            setContentView(R.layout.comments_page);
        else
            setContentView(R.layout.comments_page_dark);
        // retrieve the comments passed from the previous activity
        this.comments = getIntent().getIntegerArrayListExtra("comments");

        this.position = getIntent().getIntExtra("position", 0);

        // initialize RecyclerView
        RecyclerView lstComments = findViewById(R.id.lstComments);
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(() -> {
            viewModel.reload();
        });

        // set up the adapter
        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        EditText newComment = findViewById(R.id.newComment);
        viewModel = new CommentViewModel(comments);

        final CommentListAdapter adapter = new CommentListAdapter(this, btnAdd, newComment, viewModel);
        lstComments.setAdapter(adapter);
        lstComments.setLayoutManager(new LinearLayoutManager(this));


        viewModel.getComments().observe(this, new Observer<List<Comment>>() {
            @Override
            public void onChanged(List<Comment> comments) {
                adapter.setComments(comments);
                adapter.updateComments();
                refreshLayout.setRefreshing(false);
            }
        });


        // Set comments to the adapter
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
        List<Comment> newComments = viewModel.getComments().getValue();
        ArrayList<Integer> newIds = new ArrayList<>();
        for (int i = 0; i < newComments.size(); i++) {
            newIds.add(newComments.get(i).getCommentId());
        }

        // we send the updated comments list back to the feed screen
        Intent resultIntent = new Intent();
        resultIntent.putIntegerArrayListExtra("newIds", newIds);
        resultIntent.putExtra("position", position);
        setResult(555, resultIntent);
    }
}
