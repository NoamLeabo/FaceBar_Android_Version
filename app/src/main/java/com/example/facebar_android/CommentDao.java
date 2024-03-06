package com.example.facebar_android;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.facebar_android.Commets.Comment;
import com.example.facebar_android.Commets.CommentListAdapter;
import com.example.facebar_android.Posts.Post;

import java.util.ArrayList;
import java.util.List;
//@Dao
//public interface CommentDao {
//    @Query("SELECT * FROM comment WHERE id IN (:ids)")
//    List<Comment> get(ArrayList<Integer> ids);
//
//    @Insert
//    void insert(Comment... comments);
//    @Update
//    void update(Comment... comments);
//
//    @Delete
//    void delete(Comment... comments);
//
//    @Query("DELETE FROM comment")
//    void clear(); // Method to delete all records from the post table
//}