package com.example.facebar_android;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn, createAccBtn;
    EditText userName, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createAccBtn=findViewById(R.id.createAccount);
        userName=findViewById(R.id.userName);
        password=findViewById(R.id.password);
        loginBtn=findViewById(R.id.loginBtn);
        // logging in
        loginBtn.setOnClickListener(view -> {
            try {
                if(userInJson(userName.getText().toString(), password.getText().toString()))
                    Toast.makeText(this, "yessss", Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, "nooooo", Toast.LENGTH_SHORT).show();
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        });
        // create new account => send to subscribe page
        createAccBtn.setOnClickListener(view -> {
            Intent intent=new Intent(LoginActivity.this,SubscribeActivity.class);
            startActivity(intent);
        });

    }
    // read all objects from JSON file
    private String readJsonFile() throws IOException {
        File file = new File(this.getFilesDir(),"users");
        FileReader fileReader = new FileReader(file);
        // we are going to read lines of Objects from our file
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null){
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        fileReader.close();
        // This  will have Json Format String
        return stringBuilder.toString();
    }
    // search if user is in JSON file
    private boolean userInJson(String usernameToFind, String userPassword) throws IOException, JSONException {
        JSONObject jsonObject=new JSONObject(readJsonFile());
        // return if user in json file
        return jsonObject.get("username").toString().equals(usernameToFind) && jsonObject.get("password").equals(userPassword);
    }

}