package com.example.facebar_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder> {

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvContent;

        private CommentViewHolder(View itemView) {
            super(itemView);

            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }

    private final LayoutInflater mInflater;
    private List<Comment> comments;

    public CommentListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
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
        }
    }

    public void setComments(List<Comment> c){
        comments = c;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount(){
        if (comments != null)
            return comments.size();
        else return 0;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
