package com.example.facebar_android;

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

import com.example.facebar_android.Screens.FeedActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PendingListAdapter extends RecyclerView.Adapter<PendingListAdapter.PendingViewHolder> {

    private final LayoutInflater mInflater;
    private List<String> pendings;
    private List<String> images;
    private ActiveUser activeUser;
    private usersAPI usersAPI;
    private FriendsReqActivity friendsReqActivity;

    // an adapter that put the comment's content into a comment layout
    public PendingListAdapter(Context context, FriendsReqActivity friendsReqActivity) {
        mInflater = LayoutInflater.from(context);
        this.activeUser = ActiveUser.getInstance();
        this.pendings = activeUser.getPendings();
        this.usersAPI = new usersAPI();
        this.images = new ArrayList<>();
        this.friendsReqActivity = friendsReqActivity;
    }

    public void updatePendings() {
        notifyDataSetChanged();
    }

    class PendingViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final FloatingActionButton deleteBtn;
        private final FloatingActionButton editBtn;

        private ImageView profile;
        // constructor
        private PendingViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            profile = itemView.findViewById(R.id.profile_img);
            editBtn = itemView.findViewById(R.id.editBtn);
        }
    }

    @Override
    public PendingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (FeedActivity.NIGHT_MODE == 0) {
            View itemView = mInflater.inflate(R.layout.friend_req_layout, parent, false);
            return new PendingViewHolder(itemView);
        }
        else {
            View itemView = mInflater.inflate(R.layout.friend_req_layout, parent, false);
            return new PendingViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(PendingViewHolder holder, int position) {
        // if the comments ain't null we shall bind them with layouts

        /*attaching all comments fields */

        if (pendings != null) {
            final String current = pendings.get(position);

            ProfileUser profileUser = ProfileUser.getInstance();
            holder.tvAuthor.setText(current);

            byte[] bytes= Base64.decode(profileUser.getProfileImage(),Base64.DEFAULT);
            // Initialize bitmap
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            // set bitmap on imageView
            holder.profile.setImageBitmap(bitmap);

            // the delete comment btn
            holder.deleteBtn.setOnClickListener(v -> {
                usersAPI.rejectFriend(activeUser.getUsername(), current, new usersAPI.AddUserCallback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("deleted friend");
                    }

                    @Override
                    public void onError(String message) {
                        System.out.println("failed to delete friend");
                    }
                });

                deletePendings(position);
                updatePendings();
            });

            holder.editBtn.setOnClickListener(v -> {
                usersAPI.acceptFriend(activeUser.getUsername(), current, new usersAPI.AddUserCallback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("accepted friend");
                        friendsReqActivity.updateFList();
                    }

                    @Override
                    public void onError(String message) {
                        System.out.println("failed to accepte friend");
                    }
                });

                deletePendings(position);
                updatePendings();
            });
        }
    }

    public void setPendings(List<String> p) {
        this.pendings = p;
        notifyDataSetChanged();
    }

    public void deletePendings(int position){
        String toDelete = pendings.remove(position);
        updatePendings();
    }
    @Override
    public int getItemCount() {
        return pendings != null ? pendings.size() : 0;
    }

    public List<String> getPendings() {
        return pendings;
    }
}
