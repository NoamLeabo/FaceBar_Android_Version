package com.example.facebar_android.Commets;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Comment  {

    @PrimaryKey(autoGenerate = true)
    private int commentId;
    private final String author;
    private String content;
    private String date = "date";


    // constructor
    public Comment(String author, String content) {
        this.author = author;
        this.content = content;
    }
//    @Ignore
//    public Comment(String author, String content, int id) {
//        this.author = author;
//        this.content = content;
//        this.commentId = id;
//    }
//@Ignore
//protected Comment(Parcel in) {
//        commentId = in.readInt();
//        author = in.readString();
//        content = in.readString();
//    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

//    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
//        @Override
//        public Comment createFromParcel(Parcel in) {
//            return new Comment(in);
//        }
//
//        @Override
//        public Comment[] newArray(int size) {
//            return new Comment[size];
//        }
//    };

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int id) {
        this.commentId = id;
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

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(commentId);
//        dest.writeString(author);
//        dest.writeString(content);
//    }
}
