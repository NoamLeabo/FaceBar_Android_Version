package com.example.facebar_android.Commets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.facebar_android.Screens.FeedActivity;
import com.example.facebar_android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder> {

    private final LayoutInflater mInflater;
    private List<Comment> comments;
    private FloatingActionButton btnAdd;
    private EditText newComment;
    private CommentViewModel viewModel;

    // an adapter that put the comment's content into a comment layout
    public CommentListAdapter(Context context, FloatingActionButton addButton, EditText commentEditText, CommentViewModel viewModel) {
        mInflater = LayoutInflater.from(context);
        this.viewModel = viewModel;
        btnAdd = addButton;
        newComment = commentEditText;
        btnAdd.setOnClickListener(v -> {
            String commentText = newComment.getText().toString().trim();
            if (!commentText.isEmpty()) {
                // Create a new comment and add it to the list
                Comment newCommentObj = new Comment("Mark Z.", commentText);
                System.out.println(newCommentObj.getCommentId() +" fir");

                this.viewModel.add(newCommentObj);
                newComment.getText().clear();
                notifyDataSetChanged();
            }
        });
    }

    public void updateComments() {
        notifyDataSetChanged();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final TextView tvDate;
        private final FloatingActionButton editBtn;
        private final FloatingActionButton deleteBtn;
        private final EditText teContent;
        private ImageView profile;
        private boolean editTMode = false;

        // constructor
        private CommentViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            teContent = itemView.findViewById(R.id.teContent);
            profile = itemView.findViewById(R.id.profile_img);
        }
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (FeedActivity.NIGHT_MODE == 0) {
            View itemView = mInflater.inflate(R.layout.comment_layout, parent, false);
            return new CommentViewHolder(itemView);
        }
        else {
            View itemView = mInflater.inflate(R.layout.comment_dark, parent, false);
            return new CommentViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        // if the comments ain't null we shall bind them with layouts

        /*attaching all comments fields */

        if (comments != null) {
            final Comment current = comments.get(position);
            holder.tvAuthor.setText(current.getAuthor());
            if (current.getAuthor().equals("Mark Z."))
                holder.profile.setImageResource(R.drawable.zukiprofile);
            else
                holder.profile.setImageResource(R.drawable.person_sign_b);
            holder.tvContent.setText(current.getContent());
            if (current.getDate().equals("date"))
                current.setDate(FeedActivity.getCurrentTime());
            holder.tvDate.setText(current.getDate());

            // switching between edit and notEdit mode
            holder.editBtn.setOnClickListener(v -> {
                if (!holder.editTMode){
                    holder.teContent.setText(holder.tvContent.getText());
                    holder.teContent.setVisibility(View.VISIBLE);
                    holder.tvContent.setVisibility(View.GONE);
                    holder.editBtn.setImageResource(R.drawable.done_sign);
                    holder.editTMode = true;
                } else {
                    Comment edited = current;
                    holder.tvContent.setText(holder.teContent.getText());
                    edited.setContent(holder.teContent.getText().toString());
                    edited.setDate(FeedActivity.getCurrentTime() + " edited");
                    viewModel.edit(edited);
                    holder.teContent.setVisibility(View.GONE);
                    holder.tvContent.setVisibility(View.VISIBLE);
                    holder.editTMode = false;
                    holder.editBtn.setImageResource(android.R.drawable.ic_menu_edit);
                    holder.tvDate.setText(FeedActivity.getCurrentTime() + " edited");
                }
            });

            // the delete comment btn
            holder.deleteBtn.setOnClickListener(v -> {
                deleteComment(position);
                notifyDataSetChanged();
            });
        }
    }

    public void setComments(List<Comment> c) {
        this.comments = c;
        notifyDataSetChanged();
    }

    public void deleteComment(int position){
        Comment toDelete = comments.remove(position);
        viewModel.delete(toDelete);
        viewModel.removeId(toDelete.getCommentId());
    }
    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
