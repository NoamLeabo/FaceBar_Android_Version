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

/**
 * Activity class for handling friend requests and displaying friends.
 * Manages the UI and interactions for the friends request screen.
 */
public class FriendsReqActivity extends AppCompatActivity {
    // current active user
    private ActiveUser activeUser;
    // current user's profile viewed
    private ProfileUser profileUser;
    // friends adapter
    private FriendListAdapter adapterF;
    // a boolean whether this is a self-view of a user over his own friends or a friend who's watching
    private boolean friend = false;
    // list layout
    private SwipeRefreshLayout refreshLayoutF;
    // the username of the viewed user
    private String username;
    // user-API variable
    private UsersAPI usersAPI = new UsersAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // determine with which theme to load the feed screen
        if (FeedActivity.NIGHT_MODE == 0)
            setContentView(R.layout.activity_friends_req);
        else
            setContentView(R.layout.activity_friends_req_dark);

        // set the active user and the user's profile that's being viewed
        activeUser = ActiveUser.getInstance();
        profileUser = ProfileUser.getInstance();
        // we save the username of the user whose profile is being viewed
        this.username = profileUser.getUsername();

        // check if the activity was started with the "friend" extra
        if (getIntent().getStringExtra("friend") != null) {
            friend = true;
        }

        // initialize the friends RecyclerView
        RecyclerView lstFriends = findViewById(R.id.lstFriends);
        refreshLayoutF = findViewById(R.id.refreshLayoutF);

        /* we shall create an adapter for the RecyclerView in order to display friends */

        adapterF = new FriendListAdapter(this, friend);
        // we set this adapter as the user-friends-list's adapter
        lstFriends.setAdapter(adapterF);
        lstFriends.setLayoutManager(new LinearLayoutManager(this));
        // set the user's friends list to the adapter
        adapterF.setFriends(profileUser.getFriends());

        // set the auto update of the user's friends list
        refreshLayoutF.setOnRefreshListener(() -> {
            usersAPI.getFriends(username, new UsersAPI.AddUserCallback() {
                @Override
                public void onSuccess() {
                    profileUser = ProfileUser.getInstance();
                    adapterF.setFriends(profileUser.getFriends());
                    refreshLayoutF.setRefreshing(false);
                }

                @Override
                public void onError(String message) {
                    System.out.println("error occurred");
                }
            });
        });

        // initialize the pending requests RecyclerView
        RecyclerView lstPendings = findViewById(R.id.lstReq);
        SwipeRefreshLayout refreshLayoutR = findViewById(R.id.refreshLayoutR);

        // hide pending requests section if viewing a friend's profile and not a self-view
        if (friend) {
            refreshLayoutR.setVisibility(View.GONE);
            TextView req = findViewById(R.id.friends_req);
            req.setVisibility(View.GONE);
        }

        /* we shall create an adapter for the RecyclerView in order to display to pendings requests */

        final PendingListAdapter adapterR = new PendingListAdapter(this, this);
        // we set this adapter as the user-pending-friends-requests-list's adapter
        lstPendings.setAdapter(adapterR);
        lstPendings.setLayoutManager(new LinearLayoutManager(this));
        // set the user's pending requests list to the adapter
        adapterR.setPendings(activeUser.getPendings());

        // set the auto update of the user's pending requests list
        refreshLayoutR.setOnRefreshListener(() -> {
            activeUser = ActiveUser.getInstance();
            adapterR.setPendings(activeUser.getPendings());
            refreshLayoutR.setRefreshing(false);
        });

        // we take the latest info from the server
        updateFList();
    }

    /**
     * Updates the friends list by fetching the latest data from the server.
     */
    public void updateFList() {
        usersAPI.getFriends(username, new UsersAPI.AddUserCallback() {
            @Override
            public void onSuccess() {
                profileUser = ProfileUser.getInstance();
                adapterF.setFriends(profileUser.getFriends());
                refreshLayoutF.setRefreshing(false);
            }

            @Override
            public void onError(String message) {
                System.out.println("bad");
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

    /**
     * Sends the result back to the previous activity.
     */
    private void sendResult() {
        // send the updated results back
        Intent resultIntent = new Intent();
        setResult(555, resultIntent);
    }
}