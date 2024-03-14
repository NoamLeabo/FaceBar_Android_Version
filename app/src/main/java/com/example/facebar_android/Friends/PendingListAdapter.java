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
import com.example.facebar_android.Screens.FriendsReqActivity;
import com.example.facebar_android.Users.ActiveUser;
import com.example.facebar_android.Users.ProfileUser;
import com.example.facebar_android.API.UsersAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class PendingListAdapter extends RecyclerView.Adapter<PendingListAdapter.PendingViewHolder> {

    private final LayoutInflater mInflater;
    private List<String> pendings;
    private ActiveUser activeUser;
    private DoubleArray images = new DoubleArray();
    private UsersAPI usersAPI;
    private FriendsReqActivity friendsReqActivity;

    // an adapter that put the comment's content into a comment layout
    public PendingListAdapter(Context context, FriendsReqActivity friendsReqActivity) {
        mInflater = LayoutInflater.from(context);
        this.activeUser = ActiveUser.getInstance();
        this.pendings = activeUser.getPendings();
        this.usersAPI = new UsersAPI();
        this.friendsReqActivity = friendsReqActivity;
    }

    public void updatePendings() {
        notifyDataSetChanged();
    }

    class PendingViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final FloatingActionButton deleteBtn;
        private final FloatingActionButton acceptBtn;

        private ImageView profile;
        // constructor
        private PendingViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            profile = itemView.findViewById(R.id.profile_img);
            acceptBtn = itemView.findViewById(R.id.editBtn);
        }
    }

    @Override
    public PendingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (FeedActivity.NIGHT_MODE == 0) {
            itemView = mInflater.inflate(R.layout.friend_req_layout, parent, false);
        }
        else {
            itemView = mInflater.inflate(R.layout.friend_req_layout_dark, parent, false);
        }
        return new PendingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PendingViewHolder holder, int position) {
        // if the comments ain't null we shall bind them with layouts

        /*attaching all comments fields */

        if (pendings != null) {
            final String current = pendings.get(position);
            if (!images.ifKeyExists(current))
                images.insertPair(current, "");
            usersAPI.getProfileUser(pendings.get(position), new UsersAPI.AddUserCallback() {
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
            // the delete comment btn
            holder.deleteBtn.setOnClickListener(v -> {
                usersAPI.rejectFriend(activeUser.getUsername(), current, new UsersAPI.AddUserCallback() {
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

            holder.acceptBtn.setOnClickListener(v -> {
                usersAPI.acceptFriend(activeUser.getUsername(), current, new UsersAPI.AddUserCallback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("accepted friend");
                        friendsReqActivity.updateFList();
                        deletePendings(position);
                        updatePendings();
                    }

                    @Override
                    public void onError(String message) {
                        System.out.println("failed to accept friend");
                    }
                });
            });
        }
    }

    public void setPendings(List<String> p) {
        this.pendings = p;
        notifyDataSetChanged();
    }

    public void deletePendings(int position){
        pendings.remove(position);
        updatePendings();
    }
    @Override
    public int getItemCount() {
        return pendings != null ? pendings.size() : 0;
    }
}
