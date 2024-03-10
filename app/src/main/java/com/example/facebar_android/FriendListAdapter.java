package com.example.facebar_android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.facebar_android.CommentViewModel;
import com.example.facebar_android.Screens.FeedActivity;
import com.example.facebar_android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendViewHolder> {

    private final LayoutInflater mInflater;
    private List<String> friends;
    private ActiveUser activeUser;
    private usersAPI usersAPI;

    // an adapter that put the comment's content into a comment layout
    public FriendListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.activeUser = ActiveUser.getInstance();
        this.friends = activeUser.getFriends();
        this.usersAPI = new usersAPI();
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
        if (FeedActivity.NIGHT_MODE == 0) {
            View itemView = mInflater.inflate(R.layout.friend_layout, parent, false);
            return new FriendViewHolder(itemView);
        }
        else {
            View itemView = mInflater.inflate(R.layout.friend_layout, parent, false);
            return new FriendViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        // if the comments ain't null we shall bind them with layouts

        /*attaching all comments fields */

        if (friends != null) {
            final String current = friends.get(position);
            usersAPI.getProfileUser(friends.get(position), new usersAPI.AddUserCallback() {
                @Override
                public void onSuccess() {
                    System.out.println("got user profile");
                }

                @Override
                public void onError(String message) {
                    System.out.println("did not get user profile");
                }
            });
            ProfileUser profileUser = ProfileUser.getInstance();
            holder.tvAuthor.setText(current);

            byte[] bytes= Base64.decode(profileUser.getProfileImage(),Base64.DEFAULT);
            // Initialize bitmap
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            // set bitmap on imageView
            holder.profile.setImageBitmap(bitmap);

            // the delete comment btn
            holder.deleteBtn.setOnClickListener(v -> {
                usersAPI.rejectFriend(activeUser.getUsername(), profileUser.getUsername(), new usersAPI.AddUserCallback() {
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
        }
    }

    public void setFriends(List<String> f) {
        this.friends = f;
        notifyDataSetChanged();
    }

    public void deleteFriend(int position){
        String toDelete = friends.remove(position);
        updateFriends();
    }
    @Override
    public int getItemCount() {
        return friends != null ? friends.size() : 0;
    }

    public List<String> getFriends() {
        return friends;
    }
}
