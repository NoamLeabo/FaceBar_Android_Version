package com.example.facebar_android.Commets;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.facebar_android.Commets.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for the Comment entity.
 * Provides methods for interacting with the Comment table in the Room database.
 */
@Dao
public interface CommentDao {

    /**
     * Retrieves a list of comments by their IDs.
     *
     * @param ids the list of comment IDs
     * @return a list of Comment objects
     */
    @Query("SELECT * FROM comment WHERE commentId IN (:ids)")
    List<Comment> postComments(ArrayList<Integer> ids);

    /**
     * Retrieves a comment by its ID.
     *
     * @param id the ID of the comment
     * @return the Comment object
     */
    @Query("SELECT * FROM comment WHERE commentId = :id")
    Comment get(int id);

    /**
     * Retrieves all comments.
     *
     * @return a list of all Comment objects
     */
    @Query("SELECT * FROM comment")
    List<Comment> test();

    /**
     * Inserts a comment into the database.
     * If the comment already exists, it replaces it.
     *
     * @param comment the Comment object to insert
     * @return the row ID of the newly inserted comment
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Comment comment);

    /**
     * Updates one or more comments in the database.
     *
     * @param comments the Comment objects to update
     */
    @Update
    void update(Comment... comments);

    /**
     * Deletes one or more comments from the database.
     *
     * @param comments the Comment objects to delete
     */
    @Delete
    void delete(Comment... comments);

    /**
     * Deletes all comments from the database.
     */
    @Query("DELETE FROM comment")
    void clear();
}