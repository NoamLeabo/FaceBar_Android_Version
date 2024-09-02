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

/**
 * Adapter class for managing the pending friend requests in a RecyclerView.
 */
public class PendingListAdapter extends RecyclerView.Adapter<PendingListAdapter.PendingViewHolder> {

    private final LayoutInflater mInflater;
    private List<String> pendings;
    private ActiveUser activeUser;
    private DoubleArray images = new DoubleArray();
    private UsersAPI usersAPI;
    private String username;
    private FriendsReqActivity friendsReqActivity;

    /**
     * Constructor for initializing the PendingListAdapter.
     *
     * @param context the context in which the adapter is used
     * @param friendsReqActivity the activity managing the friend requests
     */
    public PendingListAdapter(Context context, FriendsReqActivity friendsReqActivity) {
        mInflater = LayoutInflater.from(context);
        this.activeUser = ActiveUser.getInstance();
        this.pendings = activeUser.getPendings();
        this.usersAPI = new UsersAPI();
        this.friendsReqActivity = friendsReqActivity;
        this.username = ProfileUser.getInstance().getUsername();
    }

    /**
     * Updates the list of pending friend requests and notifies the adapter.
     */
    public void updatePendings() {
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for managing the views of each pending friend request item.
     */
    class PendingViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final FloatingActionButton deleteBtn;
        private final FloatingActionButton acceptBtn;
        private final ImageView profile;

        /**
         * Constructor for initializing the PendingViewHolder.
         *
         * @param itemView the view of the pending friend request item
         */
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
        } else {
            itemView = mInflater.inflate(R.layout.friend_req_layout_dark, parent, false);
        }
        return new PendingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PendingViewHolder holder, int position) {
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
                    byte[] bytes = Base64.decode(images.getValueOfKey(current), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.profile.setImageBitmap(bitmap);
                }

                @Override
                public void onError(String message) {
                    System.out.println("did not get user profile");
                }
            });

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
                        deletePendings(position);
                        updatePendings();

                        usersAPI.getProfileUser(username, new UsersAPI.AddUserCallback() {
                            @Override
                            public void onSuccess() {
                                friendsReqActivity.updateFList();
                            }

                            @Override
                            public void onError(String message) {
                                System.out.println("failed to accept friend");
                            }
                        });
                    }

                    @Override
                    public void onError(String message) {
                        System.out.println("failed to accept friend");
                    }
                });
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

    /**
     * Sets the list of pending friend requests and notifies the adapter.
     *
     * @param p the list of pending friend requests
     */
    public void setPendings(List<String> p) {
        this.pendings = p;
        notifyDataSetChanged();
    }

    /**
     * Deletes a pending friend request at the specified position and updates the list.
     *
     * @param position the position of the pending friend request to delete
     */
    public void deletePendings(int position) {
        pendings.remove(position);
        updatePendings();
    }

    @Override
    public int getItemCount() {
        return pendings != null ? pendings.size() : 0;
    }
}