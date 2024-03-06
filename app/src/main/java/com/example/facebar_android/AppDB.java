package com.example.facebar_android;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.facebar_android.Posts.Post;
import com.example.facebar_android.Commets.Comment;


@Database(entities = {Post.class}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public abstract PostDao postDao();
    //public abstract CommentDao commentDao();

}
