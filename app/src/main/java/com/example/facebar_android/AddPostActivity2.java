package com.example.facebar_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class AddPostActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        final String[] content = new String[1];
        EditText editText = findViewById(R.id.tvWrite);

        ImageButton post_post_btn = findViewById(R.id.post_post_btn);
        ImageButton cancel_post_post_btn = findViewById(R.id.del_Btn);

        post_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content[0] = editText.getText().toString();
                sendResultBack(content[0]);
            }
        });

        cancel_post_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    // Method to return to the previous activity
    private void sendResultBack(String content) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("content", content);
        setResult(RESULT_OK, resultIntent);
        finish(); // This finishes the current activity and returns to the previous one
    }
    private void cancel(){
        finish();
    }
}