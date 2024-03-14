package com.example.facebar_android.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.facebar_android.Friends.FriendListAdapter;
import com.example.facebar_android.Friends.PendingListAdapter;
import com.example.facebar_android.R;
import com.example.facebar_android.Users.ActiveUser;
import com.example.facebar_android.Users.ProfileUser;
import com.example.facebar_android.API.UsersAPI;

public class FriendsReqActivity extends AppCompatActivity {
    private ActiveUser activeUser;
    private ProfileUser profileUser;
    private FriendListAdapter adapterF;
    private boolean friend = false;
    private SwipeRefreshLayout refreshLayoutF;

    private UsersAPI usersAPI = new UsersAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//         we display the comments overall layout
        if (FeedActivity.NIGHT_MODE == 0)
            setContentView(R.layout.activity_friends_req);
        else
            setContentView(R.layout.activity_friends_req_dark);

        activeUser = ActiveUser.getInstance();
        profileUser = ProfileUser.getInstance();

        if(getIntent().getStringExtra("friend") != null) {
            friend = true;
        }

        // retrieve the comments passed from the previous activity

        // initialize FRIENDS RecyclerView
        RecyclerView lstFriends = findViewById(R.id.lstFriends);
        refreshLayoutF = findViewById(R.id.refreshLayoutF);

        adapterF = new FriendListAdapter(this, friend);
        lstFriends.setAdapter(adapterF);
        lstFriends.setLayoutManager(new LinearLayoutManager(this));

        adapterF.setFriends(profileUser.getFriends());
        refreshLayoutF.setOnRefreshListener(() -> {
//            activeUser = ActiveUser.getInstance();
            usersAPI.getFriends(profileUser.getUsername(), new UsersAPI.AddUserCallback() {
                @Override
                public void onSuccess() {
                    profileUser =  ProfileUser.getInstance();
                    adapterF.setFriends(profileUser.getFriends());
                    refreshLayoutF.setRefreshing(false);
                }

                @Override
                public void onError(String message) {
                    System.out.println("bad");
                }
            });
        });

        // initialize PENDINGS RecyclerView
        RecyclerView lstPendings = findViewById(R.id.lstReq);
        SwipeRefreshLayout refreshLayoutR = findViewById(R.id.refreshLayoutR);

        if (friend) {
            refreshLayoutR.setVisibility(View.GONE);
            TextView req = findViewById(R.id.friends_req);
            req.setVisibility(View.GONE);
        }
        final PendingListAdapter adapterR = new PendingListAdapter(this, this);
        lstPendings.setAdapter(adapterR);
        lstPendings.setLayoutManager(new LinearLayoutManager(this));

        adapterR.setPendings(activeUser.getPendings());
        refreshLayoutR.setOnRefreshListener(() -> {
            activeUser = ActiveUser.getInstance();
            adapterR.setPendings(activeUser.getPendings());
            refreshLayoutR.setRefreshing(false);
        });

    }
    public void updateFList() {
        usersAPI.getFriends(profileUser.getUsername(), new UsersAPI.AddUserCallback() {
            @Override
            public void onSuccess() {
                profileUser = ProfileUser.getInstance();
                adapterF.setFriends(profileUser.getFriends());
                adapterF.notifyDataSetChanged();
                refreshLayoutF.setRefreshing(false);
            }

            @Override
            public void onError(String message) {

            }
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