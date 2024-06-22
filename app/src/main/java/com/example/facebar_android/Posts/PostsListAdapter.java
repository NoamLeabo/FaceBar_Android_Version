package com.example.facebar_android.Posts;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.facebar_android.Users.ActiveUser;
import com.example.facebar_android.Screens.CommentsActivity;
import com.example.facebar_android.APP_Utilities.MyApplication;
import com.example.facebar_android.Screens.ProfilePageActivity;
import com.example.facebar_android.Screens.AddPostActivity;
import com.example.facebar_android.Screens.FeedActivity;
import com.example.facebar_android.R;
import com.example.facebar_android.API.UsersAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {
    public static final int ADD_POST_TEXT_ONLY = 111;


    class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final ImageView ivPic;
        private final ImageView profPic;
        private final TextView likes;
        private final TextView comments;
        private final ImageButton likeBtn;
        private final FloatingActionButton deleteBtn;
        private final ImageButton commentBtn;
        private final ImageButton shareBtn;
        private final TextView tvDate;
        private final FloatingActionButton editBtn;
        private final EditText teContent;
        private boolean editTMode;
        private boolean shareMode;
        private LinearLayout share_feed;

        private PostViewHolder(View itemView) {
            super(itemView);
            // we attach the fields
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.ivPic);
            likes = itemView.findViewById(R.id.likes);
            comments = itemView.findViewById(R.id.comments);
            likeBtn = itemView.findViewById(R.id.like_btn); // Initialize the like button
            commentBtn = itemView.findViewById(R.id.comment_btn);
            tvDate = itemView.findViewById(R.id.tvDate);
            editBtn = itemView.findViewById(R.id.editBtn);
            teContent = itemView.findViewById(R.id.teContent);
            shareBtn = itemView.findViewById(R.id.share_btn);
            share_feed = itemView.findViewById(R.id.share_feed);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            profPic = itemView.findViewById(R.id.profile_img);
        }
    }

    private final LayoutInflater mInflater;
    private List<Post> posts;
    private Activity mActivity;
    private PostViewModel viewModel;
    private ActiveUser activeUser;
    private UsersAPI usersAPI;

    public PostsListAdapter(Activity activity, PostViewModel viewModel) {
        mInflater = LayoutInflater.from(activity);
        mActivity = activity;
        this.viewModel = viewModel;
        this.activeUser = ActiveUser.getInstance();
        usersAPI = new UsersAPI();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (FeedActivity.NIGHT_MODE == 0) {
            itemView = mInflater.inflate(R.layout.post_layout, parent, false);
        }
        else {
            itemView = mInflater.inflate(R.layout.post_dark, parent, false);
        }
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        if (posts != null) {

            /* we set all post's content fields*/

            final Post current = posts.get(position);

            holder.profPic.setImageDrawable(current.getProfPic());
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvAuthor.setOnClickListener(v -> usersAPI.getProfileUser((String) holder.tvAuthor.getText(), new UsersAPI.AddUserCallback() {
                @Override
                public void onSuccess() {
                    System.out.println("got user profile");
                    Intent i = new Intent(MyApplication.context, ProfilePageActivity.class);
                    mActivity.startActivityForResult(i, ADD_POST_TEXT_ONLY);
                }

                @Override
                public void onError(String message) {
                    System.out.println("did not get user profile");
                }
            }));

            holder.tvContent.setText(current.getContent());
            if (current.getPublished().equals("date")) {
                current.setPublished(FeedActivity.getCurrentTime());
                holder.tvDate.setText(FeedActivity.getCurrentTime());
            } else
                holder.tvDate.setText(current.getPublished());

//            int liked = activeUser.getLikedPosts().indexOf(current.getPostId());
            boolean liked = current.getUsersWhoLiked().contains(activeUser.getUsername());
            if (liked) {
                holder.likeBtn.setBackgroundResource(R.drawable.rounded_button_pressed);
            } else {
                if (FeedActivity.NIGHT_MODE == 0)
                    holder.likeBtn.setBackgroundResource(R.drawable.rounded_button);
                else
                    holder.likeBtn.setBackgroundResource(R.drawable.rounded_button_dark);
            }
            if (current.getContainsPostPic()) {

                byte[] bytes= Base64.decode(current.getImageView(),Base64.DEFAULT);
                // Initialize bitmap
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                // set bitmap on imageView
                holder.ivPic.setImageBitmap(bitmap);

                holder.ivPic.setVisibility(View.VISIBLE); // show the ImageView if an image is chosen
            } else {
                holder.ivPic.setVisibility(View.GONE); // hide the ImageView if no image is chosen
            }

            byte[] bytes= Base64.decode(current.getProfilePic(),Base64.DEFAULT);
            // Initialize bitmap
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            // set bitmap on imageView
            holder.profPic.setImageBitmap(bitmap);
            String likesStr = current.getUsersWhoLiked().size() + " likes";
            holder.likes.setText(likesStr);
            holder.comments.setText(current.getNumOfCommentsInt() + mActivity.getString(R.string.commentss));


            holder.shareMode = false;


            // Set OnClickListener for like button
            holder.likeBtn.setOnClickListener(v -> {
                viewModel.likePost(current);
                if (!liked) {
                    String likesStr2 = current.getUsersWhoLiked().size() + " likes";
                    holder.likes.setText(likesStr2);
                    holder.likeBtn.setBackgroundResource(R.drawable.rounded_button_pressed);
                } else {
                    holder.likes.setText(current.getUsersWhoLiked().size() + " likes");
                    if (FeedActivity.NIGHT_MODE == 0)
                        holder.likeBtn.setBackgroundResource(R.drawable.rounded_button);
                    else
                        holder.likeBtn.setBackgroundResource(R.drawable.rounded_button_dark);

                }
            });
            if (!holder.tvAuthor.getText().equals(activeUser.getUsername()))
                holder.editBtn.setVisibility(View.GONE);
            else
                holder.editBtn.setVisibility(View.VISIBLE);
            // change between the edit and noEdit mode
            holder.editBtn.setOnClickListener(v -> {
                if (!holder.editTMode){

                    Intent i = new Intent(MyApplication.context, AddPostActivity.class);
                    if (current.getContainsPostPic())
                        i.putExtra("imageView", current.getImageView());
                    else
                        i.putExtra("imageView","");
                    i.putExtra("author", current.getAuthor());
                    i.putExtra("id", current.get_id());
                    i.putExtra("content", current.getContent());
                    mActivity.startActivityForResult(i, ADD_POST_TEXT_ONLY);
                    System.out.println("got user profile");
                } else {
                    Post edited = current;
                    edited.setContent(String.valueOf(holder.teContent.getText()));
                    edited.setPublished(FeedActivity.getCurrentTime() + " edited");
                    viewModel.edit(edited);
                    holder.tvContent.setText(holder.teContent.getText());
                    holder.teContent.setVisibility(View.GONE);
                    holder.tvContent.setVisibility(View.VISIBLE);
                    holder.editTMode = false;
                    holder.editBtn.setImageResource(android.R.drawable.ic_menu_edit);
                    holder.tvDate.setText(FeedActivity.getCurrentTime() + mActivity.getString(R.string.edited));
                }
            });

            holder.commentBtn.setOnClickListener(v -> {
                // we move to comments page if the btn was clicked
                moveToComments(position);
            });
            // we display share menu
            holder.shareBtn.setOnClickListener(v -> {
                if (!holder.shareMode){
                    holder.share_feed.setVisibility(View.VISIBLE);
                    holder.shareMode = true;
                } else {
                    holder.share_feed.setVisibility(View.GONE);
                    holder.shareMode = false;
                }
            });

            if (!holder.tvAuthor.getText().equals(activeUser.getUsername()))
                holder.deleteBtn.setVisibility(View.GONE);
            else
                holder.deleteBtn.setVisibility(View.VISIBLE);

            // we delete post and notify so
            holder.deleteBtn.setOnClickListener(v -> {
                deletePost(position);
                notifyDataSetChanged();
            });
        }
    }

    public void moveToComments(int position){
        Intent intent = new Intent(this.mActivity, CommentsActivity.class);
        // get the comments associated with the current post
        // pass the reference of the original list of comments
        intent.putExtra("comments",(ArrayList<Integer>) posts.get(position).getCommentsInt());
        //intent.putExtra("comments", comments);
        intent.putExtra("position", position);
        mActivity.startActivityForResult(intent, 555);
    }


    public void deletePost(int position){
        Post toDelete = posts.remove(position);
        viewModel.delete(toDelete);
//        this.posts.remove(position);
        System.out.println("deletePost\n");
    }

    @Override
    public void onViewRecycled(PostViewHolder holder) {
        super.onViewRecycled(holder);
        // perform cleanup or release resources associated with the view holder

        holder.teContent.getText().clear();
        holder.teContent.setVisibility(View.GONE);
        holder.tvContent.setVisibility(View.VISIBLE);
        holder.editTMode = false;
        holder.editBtn.setImageResource(android.R.drawable.ic_menu_edit);

        holder.share_feed.setVisibility(View.GONE);
        holder.shareMode = false;
    }

    public void setPosts(List<Post> s){
        posts = s;
        notifyDataSetChanged();
        System.out.println("setPosts\n");
    }
    public void updatePosts(){
        notifyDataSetChanged();
        System.out.println("updatePosts\n");
    }
    @Override
    public int getItemCount(){
        if (posts != null)
            return posts.size();
        else return 0;
    }
}
