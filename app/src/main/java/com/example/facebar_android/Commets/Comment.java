package com.example.facebar_android.Commets;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class representing a Comment in the Room database.
 */
@Entity
public class Comment {

    @PrimaryKey(autoGenerate = true)
    private int commentId;
    private final String author;
    private String content;
    private String date = "date";

    /**
     * Constructor for creating a new Comment.
     *
     * @param author the author of the comment
     * @param content the content of the comment
     */
    public Comment(String author, String content) {
        this.author = author;
        this.content = content;
    }

    /**
     * Sets the date of the comment.
     *
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the date of the comment.
     *
     * @return the date of the comment
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the ID of the comment.
     *
     * @return the comment ID
     */
    public int getCommentId() {
        return commentId;
    }

    /**
     * Sets the ID of the comment.
     *
     * @param id the ID to set
     */
    public void setCommentId(int id) {
        this.commentId = id;
    }

    /**
     * Gets the author of the comment.
     *
     * @return the author of the comment
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the content of the comment.
     *
     * @return the content of the comment
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the comment.
     *
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
}