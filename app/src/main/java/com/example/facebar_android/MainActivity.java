package com.example.facebar_android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrolled_feed);


        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        final PostsListAdapter adapter = new PostsListAdapter(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));

        List<Post> posts = new ArrayList<>();
        posts.add(new Post("Alice1", "Hello World1", R.drawable.pic1));
        posts.add(new Post("Alice2", "Hello World2", R.drawable.pic1));
        posts.add(new Post("Alice3", "Hello World3", R.drawable.pic1));
        posts.add(new Post("Alice4", "Hello World4", R.drawable.pic1));


        adapter.setPosts(posts);
    }



}