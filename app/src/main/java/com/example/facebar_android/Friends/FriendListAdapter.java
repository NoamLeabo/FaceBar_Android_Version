package com.example.facebar_android.Friends;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.facebar_android.APP_Utilities.DoubleArray;
import com.example.facebar_android.R;
import com.example.facebar_android.Screens.FeedActivity;
import com.example.facebar_android.Users.ActiveUser;
import com.example.facebar_android.Users.ProfileUser;
import com.example.facebar_android.API.UsersAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendViewHolder> {

    private final LayoutInflater mInflater;
    private List<String> friends;
    private DoubleArray images = new DoubleArray();
    private ActiveUser activeUser;
    private String username;
    private UsersAPI usersAPI;
    private boolean friend;

    // an adapter that put the comment's content into a comment layout
    public FriendListAdapter(Context context, boolean friend) {
        mInflater = LayoutInflater.from(context);
        this.activeUser = ActiveUser.getInstance();
        this.friends = activeUser.getFriends();
        this.usersAPI = new UsersAPI();
        this.friend = friend;
        this.username = ProfileUser.getInstance().getUsername();
    }

    public void updateFriends() {
        notifyDataSetChanged();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final FloatingActionButton deleteBtn;
        private ImageView profile;
        // constructor
        private FriendViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            profile = itemView.findViewById(R.id.profile_img);
        }
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (FeedActivity.NIGHT_MODE == 0) {
            itemView = mInflater.inflate(R.layout.friend_layout, parent, false);
        }
        else {
            itemView = mInflater.inflate(R.layout.friend_layout_dark, parent, false);
        }
        return new FriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        // if the comments ain't null we shall bind them with layouts

        /*attaching all comments fields */

        if (friends != null) {
            final String current = friends.get(position);
            if (!images.ifKeyExists(current))
                images.insertPair(current, "");
            usersAPI.getProfileUser(friends.get(position), new UsersAPI.AddUserCallback() {
                @Override
                public void onSuccess() {
                    System.out.println("got user profile");
                    ProfileUser profileUser = ProfileUser.getInstance();
                    holder.tvAuthor.setText(current);

                    if (Objects.equals(images.getValueOfKey(current), "")) {
                        images.insertValueToKey(current, profileUser.getProfileImage());
                    }
                    byte[] bytes= Base64.decode(images.getValueOfKey(current),Base64.DEFAULT);
                    // Initialize bitmap
                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    // set bitmap on imageView
                    holder.profile.setImageBitmap(bitmap);
                }

                @Override
                public void onError(String message) {
                    System.out.println("did not get user profile");
                }
            });




            if (friend){
                holder.deleteBtn.setVisibility(View.GONE);
            }

            // the delete comment btn
            holder.deleteBtn.setOnClickListener(v -> {
                usersAPI.rejectFriend(activeUser.getUsername(), current, new UsersAPI.AddUserCallback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("got user profile");
                    }

                    @Override
                    public void onError(String message) {
                        System.out.println("did not get user profile");
                    }
                });

                deleteFriend(position);
                updateFriends();
            });

            usersAPI.getProfileUser(username, new UsersAPI.AddUserCallback() {
                @Override
                public void onSuccess() {
                    System.out.println("good");
                }

                @Override
                public void onError(String message) {
                    System.out.println("did not get user profile");
                }
            });

        }
    }

    public void setFriends(List<String> f) {
        this.friends = f;
        notifyDataSetChanged();
    }

    public void deleteFriend(int position){
        friends.remove(position);
        updateFriends();
    }
    @Override
    public int getItemCount() {
        return friends != null ? friends.size() : 0;
    }
}
