package com.example.facebar_android;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.facebar_android.Commets.Comment;

import java.util.ArrayList;
import java.util.List;
@Dao
public interface CommentDao {
    @Query("SELECT * FROM comment WHERE commentId IN (:ids)")
    List<Comment> postComments(ArrayList<Integer> ids);
    @Query("SELECT * FROM comment WHERE commentId = :id")
    Comment get(int id);
    @Query("SELECT * FROM comment")
    List<Comment> test();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Comment comment); // or List<Long> insert(Comment... comments);
    @Update
    void update(Comment... comments);

    @Delete
    void delete(Comment... comments);

    @Query("DELETE FROM comment")
    void clear(); // Method to delete all records from the post table
}