package com.example.facebar_android.Posts;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostDao {
    @Query("SELECT * FROM post ORDER BY postId DESC")
    List<Post> index();
    @Query("SELECT * FROM post WHERE author = :username ORDER BY postId DESC")
    List<Post> userPosts(String username);
    @Query("SELECT * FROM post WHERE postId = :id")
    Post get(int id);
    @Insert
    void insert(Post... posts);
    @Insert
    void insertList(List<Post> posts); // Modified method

    @Update
    void update(Post... posts);

    @Delete
    void delete(Post... posts);

    @Query("DELETE FROM post")
    void clear(); // Method to delete all records from the post table
}