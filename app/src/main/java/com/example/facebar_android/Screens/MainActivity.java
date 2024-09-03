package com.example.facebar_android.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facebar_android.R;
import com.example.facebar_android.API.UsersAPI;

/**
 * MainActivity class handles the login and account creation functionalities.
 */
public class MainActivity extends AppCompatActivity {
    // login screen btns
    Button loginBtn, createAccBtn;
    // input fields for login screen
    EditText userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // we create an api variable (of Users type) so we could do actions with the server
        UsersAPI usersAPI = new UsersAPI();

        // determine with which theme to load the login screen
        if (FeedActivity.NIGHT_MODE == 0)
            setContentView(R.layout.activity_login);
        else
            setContentView(R.layout.activity_login_dark);

        // connect the XML objects
        createAccBtn = findViewById(R.id.createAccount);
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);

        // handle login button click
        loginBtn.setOnClickListener(view -> {
            // we try to get from the server any user that matches the inputted values
            usersAPI.getUser(userName.getText().toString(), password.getText().toString(), new UsersAPI.AddUserCallback() {
                @Override
                public void onSuccess() {
                    // in case such user does exist we log-in the user and so
                    Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                    usersAPI.getToken(userName.getText().toString(), password.getText().toString(), new UsersAPI.AddUserCallback() {
                        @Override
                        public void onSuccess() {
                            // we reset the input fields values so they will be cleared when the user disconnects
                            userName.getText().clear();
                            password.getText().clear();
                            // we switch to the feed screen
                            Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                            startActivityForResult(intent, 999);
                        }

                        @Override
                        public void onError(String message) {
                            // case of error with creating the Token
                            Toast.makeText(MainActivity.this, "Error occurred ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                // in case no such user was found in the app server we indicate that one val or more were incorrect
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, "Username or Password is incorrect", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // handle create account button click
        createAccBtn.setOnClickListener(view -> {
            // we switch to create-an-account screen
            Intent intent = new Intent(MainActivity.this, SubscribeActivity.class);
            startActivity(intent);
        });
    }

    // when we return to the login screen we wish to recreate it in case the theme has changed in the meantime
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        recreate();
    }
}