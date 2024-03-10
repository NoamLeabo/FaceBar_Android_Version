package com.example.facebar_android.Posts;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.core.content.ContextCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.facebar_android.Commets.Comment;
import com.example.facebar_android.Converters;
import com.example.facebar_android.R;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@TypeConverters(Converters.class)
@TypeConverters(Converters.class)
@Entity
public class Post {
    @PrimaryKey(autoGenerate = true)
    private int postId;
    private String author;
    private String content;
    private String date = "date";
    private int likes;
    private boolean liked = false;
    @ColumnInfo(name = "comments_int")
    private List<Integer> commentsInt = new ArrayList<>();
    private int numOfCommentsInt = commentsInt.size();
    @Ignore
    private ArrayList<Comment> comments;
    private int numOfComments;
    @Ignore
    private Drawable profPic;
    private String path;
    private String base;
    @Ignore
    private Drawable postPic;
    private String pathPostPic;
    private boolean containsPostPic;

    // constructor from json
    public Post(String author, String content, String path, int likes, String pathPostPic) {
        this.author = author;
        this.content = content;
        this.path = path;
        this.likes = likes;
        this.pathPostPic = pathPostPic;
        this.comments = new ArrayList<>();
    }

    // constructor of a new user's post
    public Post(String author, String content, Drawable drawable, int likes, Context context, String base) {
        this.author = author;
        this.content = content;
        this.postPic = drawable;
        this.profPic = ContextCompat.getDrawable(context, R.drawable.zukiprofile);
        this.likes = likes;
        this.comments = new ArrayList<>();
        this.base = base;
    }

    // constructor of imageLess post
    public Post(String author, String content, int likes, Context context) {
        this.author = author;
        this.content = content;
        this.profPic = ContextCompat.getDrawable(context, R.drawable.zukiprofile);
        this.likes = likes;
        this.comments = new ArrayList<>();
    }

    public String getPathPostPic() {
        return this.pathPostPic;
    }

    public boolean getContainsPostPic() {
        return containsPostPic;
    }

    public void setContainsPostPic(boolean b) {
        this.containsPostPic = b;
    }

    public boolean getLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setOppLiked() {
        this.liked = !liked;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
        this.numOfComments = comments.size();
    }

    public Drawable getProfPic() {
        return this.profPic;
    }

    public void setProfPic(Drawable imageView) {
        this.profPic = imageView;
    }

    public Drawable getPostPic() {
        return this.postPic;
    }

    public void setPostPic(Drawable imageView) {
        this.postPic = imageView;
    }

    public String getPath() {
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

    public void setNumOfComments(int numOfComments) {
        this.numOfComments = numOfComments;
    }

    // Getters and setters
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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

    public List<Integer> getCommentsInt() {
        return commentsInt;
    }

    public void setCommentsInt(List<Integer> commentsInt) {
        this.commentsInt = commentsInt;
    }
    public int getNumOfCommentsInt() {
        return commentsInt.size();
    }

    public void setNumOfCommentsInt(int numOfCommentsInt) {
        this.numOfCommentsInt = numOfCommentsInt;
    }
    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

}