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
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;

import com.example.facebar_android.ActiveUser;
import com.example.facebar_android.Commets.Comment;
import com.example.facebar_android.Commets.CommentsActivity;
import com.example.facebar_android.MyApplication;
import com.example.facebar_android.PostRepository;
import com.example.facebar_android.PostViewModel;
import com.example.facebar_android.ProfilePageActivity;
import com.example.facebar_android.Screens.FeedActivity;
import com.example.facebar_android.R;
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
    private String visitingUser;
    private ActiveUser activeUser;

    public PostsListAdapter(Activity activity, PostViewModel viewModel, String visitingUser, ActiveUser activeUser) {
        mInflater = LayoutInflater.from(activity);
        mActivity = activity;
        this.viewModel = viewModel;
        this.activeUser = activeUser;
        this.visitingUser = visitingUser;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (FeedActivity.NIGHT_MODE == 0) {
            View itemView = mInflater.inflate(R.layout.post_layout, parent, false);
            return new PostViewHolder(itemView);
        }
        else {
            View itemView = mInflater.inflate(R.layout.post_dark, parent, false);
            return new PostViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        if (posts != null) {

            /* we set all post's content fields*/

            final Post current = posts.get(position);

            holder.profPic.setImageDrawable(current.getProfPic());
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MyApplication.context, ProfilePageActivity.class);
                    i.putExtra("userProfile", holder.tvAuthor.getText());
                    i.putExtra("visitingUser", holder.tvAuthor.getText());
                    mActivity.startActivityForResult(i, ADD_POST_TEXT_ONLY);
                }
            });

            holder.tvContent.setText(current.getContent());
            if (current.getDate().equals("date")) {
                current.setDate(FeedActivity.getCurrentTime());
                holder.tvDate.setText(FeedActivity.getCurrentTime());
            } else
                holder.tvDate.setText(current.getDate());

            int liked = activeUser.getLikedPosts().indexOf(current.getPostId());
            if (liked != -1) {
                holder.likeBtn.setBackgroundResource(R.drawable.rounded_button_pressed);
            } else {
                if (FeedActivity.NIGHT_MODE == 0)
                    holder.likeBtn.setBackgroundResource(R.drawable.rounded_button);
                else
                    holder.likeBtn.setBackgroundResource(R.drawable.rounded_button_dark);
            }
            if (current.isContainsPostPic()) {
                holder.ivPic.setImageDrawable(current.getPostPic());
                holder.ivPic.setVisibility(View.VISIBLE); // show the ImageView if an image is chosen
            } else {
                holder.ivPic.setVisibility(View.GONE); // hide the ImageView if no image is chosen
            }
            holder.likes.setText(current.getLikes() + " Likes");
            holder.comments.setText(current.getNumOfCommentsInt() + " Comments");

            holder.shareMode = false;


            // Set OnClickListener for like button
            holder.likeBtn.setOnClickListener(v -> {
                final int isLiked = activeUser.getLikedPosts().indexOf(current.getPostId());
                if (isLiked == -1) {
                    activeUser.getLikedPosts().add(current.getPostId());
                    Post nowLiked = current;
                    nowLiked.setLikes(current.getLikes() + 1);
                    // Increase the number of likes by 1
                    // Update the TextView to display the updated number of likes
                    holder.likes.setText(current.getLikes() + " Likes");
                    holder.likeBtn.setBackgroundResource(R.drawable.rounded_button_pressed);
                    nowLiked.setOppLiked();
                    viewModel.edit(nowLiked);
                } else {
                    activeUser.getLikedPosts().remove(current.getPostId());
                    Post nowLiked = current;
                    nowLiked.setLikes(current.getLikes() - 1);
                    holder.likes.setText(current.getLikes() + " Likes");
                    if (FeedActivity.NIGHT_MODE == 0)
                        holder.likeBtn.setBackgroundResource(R.drawable.rounded_button);
                    else
                        holder.likeBtn.setBackgroundResource(R.drawable.rounded_button_dark);
                    nowLiked.setOppLiked();
                    viewModel.edit(nowLiked);
                }
            });
            if (!holder.tvAuthor.getText().equals(visitingUser))
                holder.editBtn.setVisibility(View.GONE);

            // change between the edit and noEdit mode
            holder.editBtn.setOnClickListener(v -> {
                if (!holder.editTMode){
                    holder.teContent.setText(holder.tvContent.getText());
                    holder.teContent.setVisibility(View.VISIBLE);
                    holder.tvContent.setVisibility(View.GONE);
                    holder.editBtn.setImageResource(R.drawable.done_sign);
                    holder.editTMode = true;
                } else {
                    Post edited = current;
                    edited.setContent(String.valueOf(holder.teContent.getText()));
                    edited.setDate(FeedActivity.getCurrentTime() + " edited");
                    viewModel.edit(edited);
                    holder.tvContent.setText(holder.teContent.getText());
                    holder.teContent.setVisibility(View.GONE);
                    holder.tvContent.setVisibility(View.VISIBLE);
                    holder.editTMode = false;
                    holder.editBtn.setImageResource(android.R.drawable.ic_menu_edit);
                    holder.tvDate.setText(FeedActivity.getCurrentTime() + " edited");
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

            if (!holder.tvAuthor.getText().equals(visitingUser))
                holder.deleteBtn.setVisibility(View.GONE);

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
        ArrayList<Integer> comments = new ArrayList<>();
        comments.add(0);
        if (posts.get(position).getLikes() == 358) {
            comments.add(1);
            comments.add(2);
            comments.add(3);
            comments.add(4);
        } else {
            comments.add(20);
            comments.add(22);
            comments.add(23);
            comments.add(24);
            comments.add(25);
            comments.add(26);
            comments.add(27);


        }
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
