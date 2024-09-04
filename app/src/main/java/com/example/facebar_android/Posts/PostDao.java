package com.example.facebar_android.Posts;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostDao {
    /**
     * Retrieves all posts from the database, ordered by postId in descending order.
     *
     * @return A list of all posts.
     */
    @Query("SELECT * FROM post ORDER BY postId DESC")
    List<Post> index();

    /**
     * Retrieves all posts by a specific user, ordered by postId in descending order.
     *
     * @param username The username of the author.
     * @return A list of posts by the specified user.
     */
    @Query("SELECT * FROM post WHERE author = :username ORDER BY postId DESC")
    List<Post> userPosts(String username);

    /**
     * Retrieves a post by its ID.
     *
     * @param id The ID of the post.
     * @return The post with the specified ID.
     */
    @Query("SELECT * FROM post WHERE postId = :id")
    Post get(int id);

    /**
     * Inserts one or more posts into the database.
     *
     * @param posts The posts to insert.
     */
    @Insert
    void insert(Post... posts);

    /**
     * Inserts a list of posts into the database.
     *
     * @param posts The list of posts to insert.
     */
    @Insert
    void insertList(List<Post> posts);

    /**
     * Updates one or more posts in the database.
     *
     * @param posts The posts to update.
     */
    @Update
    void update(Post... posts);

    /**
     * Deletes one or more posts from the database.
     *
     * @param posts The posts to delete.
     */
    @Delete
    void delete(Post... posts);

    /**
     * Deletes all records from the post table.
     */
    @Query("DELETE FROM post")
    void clear();
}