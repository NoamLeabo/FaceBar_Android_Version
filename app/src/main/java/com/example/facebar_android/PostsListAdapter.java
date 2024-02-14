package com.example.facebar_android;

import android.content.Context;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

    class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final ImageView ivPic;
        private final TextView likes;
        private final TextView comments;
        private final ImageButton likeBtn;
        private boolean liked = false;
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
        }
    }

    private final LayoutInflater mInflater;
    private List<Post> posts;

    public PostsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
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
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvContent.setText(current.getContent());
            if (holder.tvDate.getText().equals("date")) {
                holder.tvDate.setText(MainActivity.getCurrentTime());
            }
            if (current.isLiked()) {
                holder.likeBtn.setBackgroundResource(R.drawable.rounded_button_pressed);
            } else {
                holder.likeBtn.setBackgroundResource(R.drawable.rounded_button);
            }
            if (current.getPic() != 0) {
                holder.ivPic.setImageResource(current.getPic());
                holder.ivPic.setVisibility(View.VISIBLE); // Show the ImageView if an image is chosen
            } else if (current.getIv() != null) {
                holder.ivPic.setImageResource(current.getPic());
                holder.ivPic.setVisibility(View.VISIBLE);
            } else {
                holder.ivPic.setVisibility(View.GONE); // Hide the ImageView if no image is chosen
            }
            holder.likes.setText(current.getLikes() + " Likes");
            holder.comments.setText(current.getComments().size() + " Comments");

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
            holder.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                        holder.tvDate.setText(MainActivity.getCurrentTime() + " edited");
                    }
                }
            });

            holder.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Pass the comments associated with the clicked post to CommentsActivity
                    Intent intent = new Intent(v.getContext(), CommentsActivity.class);
                    // Get the comments associated with the current post
                    List<Comment> postComments = current.getComments();
                    // Convert the list of comments to an ArrayList
                    ArrayList<Comment> commentsArrayList = new ArrayList<>(postComments);
                    // Pass the ArrayList to the intent
                    intent.putParcelableArrayListExtra("comments", commentsArrayList);
                    v.getContext().startActivity(intent);
                }
            });

            holder.shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!holder.shareMode){
                        holder.share_feed.setVisibility(View.VISIBLE);
                        holder.shareMode = true;
                    } else {
                        holder.share_feed.setVisibility(View.GONE);
                        holder.shareMode = false;
                    }
                }
            });
        }
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

    public List<Post> getPosts() {
        return posts;
    }
}
