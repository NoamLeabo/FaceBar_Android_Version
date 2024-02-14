package com.example.facebar_android;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Post implements Parcelable {

    private int id;
    private String author;
    private String content;
    private int likes;
    private int numOfComments = 0;
    private int pic;
    private ImageView iv;
    private List<Comment> comments;
    private boolean editMode = false;
    private boolean shareMode = false;
    private boolean liked = false;
    public boolean isLiked() {
        return liked;
    }

    public void setOppLiked() {
        this.liked = !liked;
    }


    public boolean isEditMode() {
        return editMode;
    }

    public void changeEditMode() {
        this.editMode = !editMode;
    }

    public boolean isShareMode() {
        return shareMode;
    }

    public void setShareMode(boolean shareMode) {
        this.shareMode = shareMode;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.numOfComments += comments.size();
    }

    // Default constructor
    public Post() {
        this.pic = R.drawable.pic1;
    }

    // Constructor with parameters
    public Post(String author, String content, int pic, int likes) {
        this.author = author;
        this.content = content;
        this.pic = pic;
        this.likes = likes;
        this.comments = new ArrayList<>();
    }

    public Post(String author, String content, ImageView pic, int likes) {
        this.author = author;
        this.content = content;
        this.iv = pic;
        this.likes = likes;
        this.comments = new ArrayList<>();
    }

    public void addComment(Comment c) {
        this.comments.add(c);
        this.numOfComments++;
    }

    public List<Comment> getComments() {
        return comments;
    }

    // Parcelable constructor
    protected Post(Parcel in) {
        id = in.readInt();
        author = in.readString();
        content = in.readString();
        likes = in.readInt();
        pic = in.readInt();
    }

    public int getNumOfComments() {
        return numOfComments;
    }

    public void setNumOfComments(int numOfComments) {
        this.numOfComments = numOfComments;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    // Implementing Parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeInt(likes);
        dest.writeInt(pic);
    }

    public ImageView getIv() {
        return iv;
    }
    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}