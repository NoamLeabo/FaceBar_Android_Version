//package com.example.facebar_android;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.EditText;
//
//import com.example.facebar_android.Commets.CommentListAdapter;
//import com.example.facebar_android.Screens.FeedActivity;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//import java.util.ArrayList;
//
//public class FriendsReqActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // we display the comments overall layout
////        if (FeedActivity.NIGHT_MODE == 0)
////            setContentView(R.layout.comments_page);
////        else
////            setContentView(R.layout.comments_page_dark);
//        setContentView(R.layout.activity_friends_req);
//
//        // retrieve the comments passed from the previous activity
//
//        // initialize RecyclerView
//        RecyclerView lstFriends = findViewById(R.id.lstFriends);
//
//        viewModel.getComments().observe(this, new Observer<List<Comment>>() {
//            @Override
//            public void onChanged(List<Comment> comments) {
//                adapter.setComments(comments);
//                adapter.updateComments();
//                refreshLayout.setRefreshing(false);
//            }
//        });
//
//
//        // Set comments to the adapter
//    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        sendResult();
//    }
//
//    @Override
//    public void finish() {
//        sendResult();
//        super.finish();
//    }
//
//    private void sendResult() {
//        List<Comment> newComments = viewModel.getComments().getValue();
//        ArrayList<Integer> newIds = new ArrayList<>();
//        for (int i = 0; i < newComments.size(); i++) {
//            newIds.add(newComments.get(i).getCommentId());
//        }
//
//        // we send the updated comments list back to the feed screen
//        Intent resultIntent = new Intent();
//        resultIntent.putIntegerArrayListExtra("newIds", newIds);
//        resultIntent.putExtra("position", position);
//        setResult(555, resultIntent);
//    }
//}