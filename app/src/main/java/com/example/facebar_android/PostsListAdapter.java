package com.example.facebar_android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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



        private PostViewHolder(View itemView) {
            super(itemView);

            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.ivPic);
            likes = itemView.findViewById(R.id.likes);
            comments = itemView.findViewById(R.id.comments);
            likeBtn = itemView.findViewById(R.id.like_btn); // Initialize the like button
            commentBtn = itemView.findViewById(R.id.comment_btn);
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
            if (current.getPic() != 0) {
                holder.ivPic.setImageResource(current.getPic());
                holder.ivPic.setVisibility(View.VISIBLE); // Show the ImageView if an image is chosen
            } else {
                holder.ivPic.setVisibility(View.GONE); // Hide the ImageView if no image is chosen
            }
            holder.likes.setText(current.getLikes() + " Likes");
            holder.comments.setText(current.getNumOfComments() + " Comments");

            // Set OnClickListener for like button
            holder.likeBtn.setOnClickListener(v -> {
                if (!holder.liked) {
                    // Increase the number of likes by 1
                    current.setLikes(current.getLikes() + 1);
                    // Update the TextView to display the updated number of likes
                    holder.likes.setText(current.getLikes() + " Likes");
                    holder.likeBtn.setBackgroundResource(R.drawable.rounded_button_pressed);
                    holder.liked = true;
                } else {
                    current.setLikes(current.getLikes() - 1);
                    holder.likes.setText(current.getLikes() + " Likes");
                    holder.likeBtn.setBackgroundResource(R.drawable.rounded_button);
                    holder.liked = false;
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

        }
    }

    public void setPosts(List<Post> s){
        posts = s;
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
