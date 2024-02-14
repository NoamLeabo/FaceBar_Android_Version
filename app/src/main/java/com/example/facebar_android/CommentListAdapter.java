package com.example.facebar_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder> {

    private final LayoutInflater mInflater;
    private List<Comment> comments;
    private FloatingActionButton btnAdd;
    private EditText newComment;

    public CommentListAdapter(Context context, FloatingActionButton addButton, EditText commentEditText) {
        mInflater = LayoutInflater.from(context);
        comments = new ArrayList<>(); // Initialize the comments list
        btnAdd = addButton;
        newComment = commentEditText;
        btnAdd.setOnClickListener(v -> {
            String commentText = newComment.getText().toString().trim();
            if (!commentText.isEmpty()) {
                // Create a new comment and add it to the list
                Comment newCommentObj = new Comment("Active User", commentText);
                comments.add(newCommentObj);
                newComment.getText().clear();
                notifyDataSetChanged();
            }
        });
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final TextView tvDate;
        private final FloatingActionButton editBtn;
        private final FloatingActionButton deleteBtn;
        private final EditText teContent;
        private boolean editTMode = false;


        private CommentViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            teContent = itemView.findViewById(R.id.teContent);
        }
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.comment_layout, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        if (comments != null) {
            final Comment current = comments.get(position);
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvContent.setText(current.getContent());
            if (holder.tvDate.getText().equals("date")) {
                holder.tvDate.setText(MainActivity.getCurrentTime());
            }

            holder.editBtn.setOnClickListener(v -> {
                if (!holder.editTMode){
                    holder.teContent.setText(holder.tvContent.getText());
                    holder.teContent.setVisibility(View.VISIBLE);
                    holder.tvContent.setVisibility(View.GONE);
                    holder.editBtn.setImageResource(R.drawable.done_sign);
                    holder.editTMode = true;
                } else {
                    holder.tvContent.setText(holder.teContent.getText());
                    holder.teContent.setVisibility(View.GONE);
                    holder.tvContent.setVisibility(View.VISIBLE);
                    holder.editTMode = false;
                    holder.editBtn.setImageResource(android.R.drawable.ic_menu_edit);
                    holder.tvDate.setText(MainActivity.getCurrentTime() + " edited");
                }
            });

            holder.deleteBtn.setOnClickListener(v -> {
                deleteComment(position);
                notifyDataSetChanged();
            });
        }
    }

    public void setComments(List<Comment> c) {
        comments = c;
        notifyDataSetChanged();
    }

    public void deleteComment(int position){
        this.comments.remove(position);
    }
    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
