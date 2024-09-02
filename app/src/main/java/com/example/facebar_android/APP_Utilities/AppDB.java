package com.example.facebar_android.APP_Utilities;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.facebar_android.Commets.CommentDao;
import com.example.facebar_android.Posts.Post;
import com.example.facebar_android.Commets.Comment;
import com.example.facebar_android.Posts.PostDao;

/**
 * The main database class for the application.
 * This class defines the database configuration and serves as the app's main access point to the persisted data.
 */
@Database(entities = {Post.class, Comment.class}, version = 12)
//@TypeConverters({Converters.class})
public abstract class AppDB extends RoomDatabase {

    /**
     * Provides access to the Post data operations.
     *
     * @return an instance of PostDao
     */
    public abstract PostDao postDao();

    /**
     * Provides access to the Comment data operations.
     *
     * @return an instance of CommentDao
     */
    public abstract CommentDao commentDao();
}