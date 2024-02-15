package com.example.facebar_android.Posts;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.facebar_android.Commets.Comment;
import com.example.facebar_android.Commets.CommentsActivity;
import com.example.facebar_android.Screens.FeedActivity;
import com.example.facebar_android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

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

    public PostsListAdapter(Activity activity) {
        mInflater = LayoutInflater.from(activity);
        mActivity = activity;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.post_layout, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        if (posts != null) {
            final Post current = posts.get(position);

            holder.profPic.setImageDrawable(current.getProfPic());
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvContent.setText(current.getContent());
            if (current.getDate().equals("date")) {
                current.setDate(FeedActivity.getCurrentTime());
                holder.tvDate.setText(FeedActivity.getCurrentTime());
            } else
                holder.tvDate.setText(current.getDate());
            if (current.isLiked()) {
                holder.likeBtn.setBackgroundResource(R.drawable.rounded_button_pressed);
            } else {
                holder.likeBtn.setBackgroundResource(R.drawable.rounded_button);
            }
            if (current.isContainsPostPic()) {
                holder.ivPic.setImageDrawable(current.getPostPic());
                holder.ivPic.setVisibility(View.VISIBLE); // Show the ImageView if an image is chosen
            } else {
                holder.ivPic.setVisibility(View.GONE); // Hide the ImageView if no image is chosen
            }
            holder.likes.setText(current.getLikes() + " Likes");
            holder.comments.setText(current.getNumOfComments() + " Comments");

            holder.shareMode = false;


            // Set OnClickListener for like button
            holder.likeBtn.setOnClickListener(v -> {
                if (!current.isLiked()) {
                    // Increase the number of likes by 1
                    current.setLikes(current.getLikes() + 1);
                    // Update the TextView to display the updated number of likes
                    holder.likes.setText(current.getLikes() + " Likes");
                    holder.likeBtn.setBackgroundResource(R.drawable.rounded_button_pressed);
                    current.setOppLiked();
                } else {
                    current.setLikes(current.getLikes() - 1);
                    holder.likes.setText(current.getLikes() + " Likes");
                    holder.likeBtn.setBackgroundResource(R.drawable.rounded_button);
                    current.setOppLiked();
                }
            });
            holder.editBtn.setOnClickListener(v -> {
                if (!holder.editTMode){
                    holder.teContent.setText(holder.tvContent.getText());
                    holder.teContent.setVisibility(View.VISIBLE);
                    holder.tvContent.setVisibility(View.GONE);
                    holder.editBtn.setImageResource(R.drawable.done_sign);
                    holder.editTMode = true;
                } else {
                    current.setContent(String.valueOf(holder.teContent.getText()));
                    holder.tvContent.setText(holder.teContent.getText());
                    holder.teContent.setVisibility(View.GONE);
                    holder.tvContent.setVisibility(View.VISIBLE);
                    holder.editTMode = false;
                    holder.editBtn.setImageResource(android.R.drawable.ic_menu_edit);
                    holder.tvDate.setText(FeedActivity.getCurrentTime() + " edited");
                    current.setDate(FeedActivity.getCurrentTime() + " edited");
                }
            });

            holder.commentBtn.setOnClickListener(v -> {
                test(position);
            });

            holder.shareBtn.setOnClickListener(v -> {
                if (!holder.shareMode){
                    holder.share_feed.setVisibility(View.VISIBLE);
                    holder.shareMode = true;
                } else {
                    holder.share_feed.setVisibility(View.GONE);
                    holder.shareMode = false;
                }
            });

            holder.deleteBtn.setOnClickListener(v -> {
                deletePost(position);
                notifyDataSetChanged();
            });
        }
    }

    public void test(int position){
        Intent intent = new Intent(this.mActivity, CommentsActivity.class);
        // Get the comments associated with the current post
        ArrayList<Comment> postComments = posts.get(position).getComments();
        // Pass the reference of the original list of comments
        intent.putExtra("comments", postComments);
        intent.putExtra("position", position);
        mActivity.startActivityForResult(intent, 555);
    }


    public void deletePost(int position){
        this.posts.remove(position);
    }

    @Override
    public void onViewRecycled(PostViewHolder holder) {
        super.onViewRecycled(holder);
        // Perform cleanup or release resources associated with the view holder
        
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
    }
    public void updatePosts(){
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount(){
        if (posts != null)
            return posts.size();
        else return 0;
    }
}
