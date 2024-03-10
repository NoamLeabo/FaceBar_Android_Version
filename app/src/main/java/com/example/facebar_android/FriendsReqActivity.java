package com.example.facebar_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.facebar_android.Commets.CommentListAdapter;
import com.example.facebar_android.Screens.FeedActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FriendsReqActivity extends AppCompatActivity {
    ActiveUser activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // we display the comments overall layout
//        if (FeedActivity.NIGHT_MODE == 0)
//            setContentView(R.layout.comments_page);
//        else
//            setContentView(R.layout.comments_page_dark);

        activeUser = ActiveUser.getInstance();
        setContentView(R.layout.activity_friends_req);

        // retrieve the comments passed from the previous activity

        // initialize FRIENDS RecyclerView
        RecyclerView lstFriends = findViewById(R.id.lstFriends);
        SwipeRefreshLayout refreshLayoutF = findViewById(R.id.refreshLayoutF);

        final FriendListAdapter adapterF = new FriendListAdapter(this);
        lstFriends.setAdapter(adapterF);
        lstFriends.setLayoutManager(new LinearLayoutManager(this));

        adapterF.setFriends(activeUser.getFriends());
        refreshLayoutF.setOnRefreshListener(() -> {
            activeUser = ActiveUser.getInstance();
            adapterF.setFriends(activeUser.getFriends());
            refreshLayoutF.setRefreshing(false);
        });

        // initialize PENDINGS RecyclerView
        RecyclerView lstPendings = findViewById(R.id.lstReq);
        SwipeRefreshLayout refreshLayoutR = findViewById(R.id.refreshLayoutR);

        final PendingListAdapter adapterR = new PendingListAdapter(this);
        lstPendings.setAdapter(adapterR);
        lstPendings.setLayoutManager(new LinearLayoutManager(this));

        adapterR.setPendings(activeUser.getPendings());
        refreshLayoutR.setOnRefreshListener(() -> {
            activeUser = ActiveUser.getInstance();
            adapterR.setPendings(activeUser.getPendings());
            refreshLayoutR.setRefreshing(false);
        });

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
        setResult(555, resultIntent);
    }
}