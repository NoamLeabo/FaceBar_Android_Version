package com.example.facebar_android.Posts;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.example.facebar_android.Commets.Comment;
import com.example.facebar_android.R;

import java.util.ArrayList;

public class Post {
    private static int id = 1;
    private int postId;
    private String author;
    private String content;
    private String date = "date";
    private int likes;
    private boolean liked = false;
    private ArrayList<Comment> comments;
    private int numOfComments;
    private Drawable profPic;
    private String path;
    private Drawable postPic;
    private String pathPostPic;

    private boolean containsPostPic = false;

    public Post(String author, String content, String path, int likes, String pathPostPic) {
        this.author = author;
        this.content = content;
        this.path = path;
        this.likes = likes;
        this.pathPostPic = pathPostPic;
        this.comments = new ArrayList<>();
        postId = Post.id;
        Post.id++;
    }
    public Post(String author, String content, Drawable drawable, int likes, Context context) {
        this.author = author;
        this.content = content;
        this.postPic = drawable;
        this.profPic = ContextCompat.getDrawable(context, R.drawable.zukiprofile);
        this.likes = likes;
        this.comments = new ArrayList<>();
        postId = Post.id;
        Post.id++;
    }
    public Post(String author, String content, int likes, Context context) {
        this.author = author;
        this.content = content;
        this.profPic = ContextCompat.getDrawable(context, R.drawable.zukiprofile);
        this.likes = likes;
        this.comments = new ArrayList<>();
        postId = Post.id;
        Post.id++;
    }

    public String getPathPostPic() {
        return this.pathPostPic;
    }

    public boolean isContainsPostPic() {
        return containsPostPic;
    }

    public void setContainsPostPic() {
        this.containsPostPic = true;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setOppLiked() {
        this.liked = !liked;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getDate(){
        return this.date;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
        this.numOfComments = comments.size();
    }
    public Drawable getProfPic() {
        return this.profPic;
    }
    public void setProfPic(Drawable imageView){
        this.profPic = imageView;
    }
    public Drawable getPostPic() {
        return this.postPic;
    }
    public void setPostPic(Drawable imageView){
        this.postPic = imageView;
    }
    public String getPath(){
        return this.path;
    }

    public void addComment(Comment c) {
        this.comments.add(c);
        this.numOfComments++;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public int getNumOfComments() {
        return numOfComments;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}