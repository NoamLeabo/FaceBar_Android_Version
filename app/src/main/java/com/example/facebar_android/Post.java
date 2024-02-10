package com.example.facebar_android;

public class Post {
    String author;
    String date;
    String postText;
    int profileImage;
    int postImg;

    public Post(String author, String postText, String date, int profileImage, int postImg) {
        this.author = author;
        this.date = date;
        this.profileImage = profileImage;
        this.postImg = postImg;
        this.postText = postText;
    }

    public String getPostText() {
        return postText;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public int getPostImg() {
        return postImg;
    }

    public void select() {
        author = author + " selected";
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPostImg(int postImg) {
        this.postImg = postImg;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }
}
