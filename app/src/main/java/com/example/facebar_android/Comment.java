package com.example.facebar_android;


import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {

    private int id;
    private String author;
    private String content;

    // Constructor with parameters
    public Comment(String author, String content) {
        this.author = author;
        this.content = content;
    }

    protected Comment(Parcel in) {
        id = in.readInt();
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(author);
        dest.writeString(content);
    }
}
