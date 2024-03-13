package com.example.facebar_android.Screens;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facebar_android.R;
import com.example.facebar_android.API.UsersAPI;

public class MainActivity extends AppCompatActivity {
    Button loginBtn, createAccBtn;
    EditText userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UsersAPI usersAPI=new UsersAPI();
        if (FeedActivity.NIGHT_MODE == 0)
            setContentView(R.layout.activity_login);
        else
            setContentView(R.layout.activity_login_dark);

        // we connect the xmls' objects
        createAccBtn = findViewById(R.id.createAccount);
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);

        // logging in
        loginBtn.setOnClickListener(view -> {
            usersAPI.getUser(userName.getText().toString(), password.getText().toString(), new UsersAPI.AddUserCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                    usersAPI.getToken(userName.getText().toString(), password.getText().toString(), new UsersAPI.AddUserCallback() {
                        @Override
                        public void onSuccess(){
                            Toast.makeText(MainActivity.this, "Token got", Toast.LENGTH_SHORT).show();
                            userName.getText().clear();
                            password.getText().clear();
                            Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                            startActivityForResult(intent, 999);
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(MainActivity.this, "Error getting token", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, "Username or Password is incorrect", Toast.LENGTH_SHORT).show();

                }
            });

        });
        // create new account => send to subscribe page
        createAccBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SubscribeActivity.class);
            startActivity(intent);
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        recreate();
    }
}