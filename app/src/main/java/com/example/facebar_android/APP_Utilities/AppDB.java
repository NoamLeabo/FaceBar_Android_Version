package com.example.facebar_android.APP_Utilities;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.facebar_android.Commets.CommentDao;
import com.example.facebar_android.Posts.Post;
import com.example.facebar_android.Commets.Comment;
import com.example.facebar_android.Posts.PostDao;


@Database(entities = {Post.class, Comment.class}, version = 11)
//@TypeConverters({Converters.class})

public abstract class AppDB extends RoomDatabase {
    public abstract PostDao postDao();
    public abstract CommentDao commentDao();

}
