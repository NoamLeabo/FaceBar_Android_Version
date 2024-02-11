package com.example.facebar_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn, createAccBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn=findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(view -> {
            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
        });
        createAccBtn.setOnClickListener(view -> {
            Intent intent=new Intent(LoginActivity.this,SubscribeActivity.class);
            startActivity(intent);
        });

    }
}