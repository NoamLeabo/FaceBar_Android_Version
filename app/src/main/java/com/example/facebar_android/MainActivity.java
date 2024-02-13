package com.example.facebar_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    public static String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        // Get the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
        String date = dateFormat.format(calendar.getTime());

        // Construct the time string
        String time = String.format(Locale.getDefault(), "%02d:%02d, %s", hourOfDay, minute, date);
        return time;
    }

    public String getWelcome(String username) {
        String time;

        // Get the current hour of the day
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        // Determine the time of day based on the current hour
        if (hourOfDay >= 0 && hourOfDay < 12) {
            time = "Good morning";
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            time = "Good afternoon";
        } else {
            time = "Good evening";
        }
        return time + ",  " + username + "!";
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrolled_feed);

        // username
        String username = "Noam";

        // we get the RecyclerView
        RecyclerView lstPosts = findViewById(R.id.lstPosts);

        // we append the welcome msg
        TextView textView = findViewById(R.id.con_user);
        textView.append(getWelcome(username));

        // we create a new adapter for the RecyclerView
        final PostsListAdapter adapter = new PostsListAdapter(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));

        Comment c1 = new Comment("Noam1", "goodgf gdfg fdg dfhjfhggfh trhrh rth 1");
        Comment c2 = new Comment("Noam2", "good2sd asd asds asdasdas dasd gdf df");
        Comment c3 = new Comment("Noam3", "goodsa sadsa dsa dasd asds add3");
        Comment c4 = new Comment("Noam1", "goodgf gdfg fdg dfhjfhggfh trhrh rth 1");
        Comment c5= new Comment("Noam2", "good2sd asd asds asdasdas dasd gdf df");
        Comment c6 = new Comment("Noam3", "goodsa sadsa dsa dasd asds add3");
        Comment c7 = new Comment("Noam1", "goodgf gdfg fdg dfhjfhggfh trhrh rth 1");
        Comment c8 = new Comment("Noam2", "good2sd asd asds asdasdas dasd gdf df");
        Comment c9 = new Comment("Noam3", "goodsa sadsa dsa dasd asds add3");
        Comment c10 = new Comment("Noam1", "goodgf gdfg fdg dfhjfhggfh trhrh rth 1");
        Comment c11 = new Comment("Noam2", "good2sd asd asds asdasdas dasd gdf df");
        Comment c12 = new Comment("Noam3", "goodsa sadsa dsa dasd asds add3");
        List<Comment> comments = new ArrayList<>();
        comments.add(c1);
        comments.add(c2);
        comments.add(c3);
        comments.add(c4);
        comments.add(c5);
        comments.add(c6);
        comments.add(c7);
        comments.add(c8);
        comments.add(c9);
        comments.add(c10);
        comments.add(c11);
        comments.add(c12);


        // we create a new posts list
        List<Post> posts = new ArrayList<>();
        posts.add(new Post("Alice1", "Hello World1Hello World1Hello World1Hello World1H34g34g 34 g34 3geg dgfe rgergello World1", R.drawable.pic1, 244));
        posts.add(new Post("Alice2", "Hello World2 Hello World2 Hello World2Hello World2Hello World2Hello World2 Hello World2 Hello World2Hello World2Hello World2Hello World2", 0, 24 ));
        posts.add(new Post("Alice3", "Hello Hello World3   World3 World3 World3 World3 World3 World3 v World3 wfew  ht w fwef 3 34t34g4g3g Hello World3Hello World3Hello World3", R.drawable.pic1, 247));
        posts.add(new Post("Alice4", " Hello World4 Hello World4 Hello Hello World4 Hello World4 Hello Hello World4 Hello World4 Hello World4 Hello World4 Hello World4 Hello World4", 0, 32));
        posts.add(new Post("Alice5", " Hello World4 Hello World4 Hello Hello World4 Hello World4 Hello Hello World4 Hello World4 Hello World4 Hello World4 Hello World4 Hello World4", R.drawable.pic1, 24));
        posts.add(new Post("Alice2", "Hello World2 Hello World2 Hello World2Hello World2Hello World2Hello World2 Hello World2 Hello World2Hello World2Hello World2Hello World2", 0, 24));
        posts.add(new Post("Alice3", "Hello Hello World3   World3 World3 World3 World3 World3 World3 v World3 wfew  ht w fwef 3 34t34g4g3g Hello World3Hello World3Hello World3", R.drawable.pic1, 24));
        posts.add(new Post("Alice4", " Hello World4 Hello World4 Hello Hello World4 Hello World4 Hello Hello World4 Hello World4 Hello World4 Hello World4 Hello World4 Hello World4", R.drawable.pic1, 24));
        posts.add(new Post("Alice5", " Hello World4 Hello World4 Hello Hello World4 Hello World4 Hello Hello World4 Hello World4 Hello World4 Hello World4 Hello World4 Hello World4", 0, 24));
        posts.add(new Post("Alice2", "Hello World2 Hello World2 Hello World2Hello World2Hello World2Hello World2 Hello World2 Hello World2Hello World2Hello World2Hello World2", 0, 24));
        posts.add(new Post("Alice3", "Hello Hello World3   World3 World3 World3 World3 World3 World3 v World3 wfew  ht w fwef 3 34t34g4g3g Hello World3Hello World3Hello World3", R.drawable.pic1, 24));
        posts.add(new Post("Alice4", " Hello World4 Hello World4 Hello Hello World4 Hello World4 Hello Hello World4 Hello World4 Hello World4 Hello World4 Hello World4 Hello World4", R.drawable.pic1, 24));
        posts.add(new Post("Alice5", " Hello World4 Hello World4 Hello Hello World4 Hello World4 Hello Hello World4 Hello World4 Hello World4 Hello World4 Hello World4 Hello World4", 0, 24));

        for (int i = 0; i < posts.size(); i++) {
            posts.get(i).setComments(comments);
        }

        adapter.setPosts(posts);

        Button add_post_btn = findViewById(R.id.add_post_btn);

        add_post_btn.setOnClickListener(v -> {
            Intent i = new Intent(this, AddPostActivity2.class);
            i.putParcelableArrayListExtra("posts", new ArrayList<>(posts)); // Put list of posts in intent
            startActivity(i);
            adapter.setPosts(posts);
        });

    }

}