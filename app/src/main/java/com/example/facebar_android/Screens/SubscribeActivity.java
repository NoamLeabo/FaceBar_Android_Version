package com.example.facebar_android.Screens;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.facebar_android.R;
import com.example.facebar_android.usersAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubscribeActivity extends AppCompatActivity {

    final int EMPTY=0;

    EditText fName,lName,password,userName,passwordCheck;
    Button openCameraBtn,subscribeBtn,galleryBtn;
    ImageView profilePic;
    // launches the intent to open gallery
    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersAPI usersAPI=new usersAPI();

        if (FeedActivity.NIGHT_MODE == 0)
            setContentView(R.layout.activity_subscribe);
        else
            setContentView(R.layout.activity_subscribe_dark);

        userName=findViewById(R.id.userName);
        profilePic=null;
        fName=findViewById(R.id.fName);
        lName=findViewById(R.id.lName);
        password=findViewById(R.id.password);
        passwordCheck=findViewById(R.id.passwordCheck);
        openCameraBtn=findViewById(R.id.openCameraBtn);
        subscribeBtn=findViewById(R.id.subscribeBtn);
        galleryBtn=findViewById(R.id.galleryBtn);
        registerResult();

        // onclick for opening gallery
        galleryBtn.setOnClickListener(view ->  {
            // launch open gallery
            Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcher.launch(intent);
        });

        // onclick for opening camera
        openCameraBtn.setOnClickListener(view -> {
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            launcher.launch(intent);
        });

        // onclick for subscribing
        subscribeBtn.setOnClickListener(view -> {
            if(checkValidInput()) {
                Toast.makeText(this, "nice", Toast.LENGTH_SHORT).show();
                profilePic.setDrawingCacheEnabled(true); // Enable drawing cache
                profilePic.buildDrawingCache(); // Build the drawing cache
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                Bitmap bitmap=Bitmap.createBitmap(profilePic.getDrawingCache());
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte[] bytes=stream.toByteArray();
                String image= Base64.getEncoder().encodeToString(bytes);

                usersAPI.addUser(fName.getText().toString(),lName.getText().toString(),userName.getText().toString(),password.getText().toString(),image);
                finish();
            }
        });

    }

    //check if user inserted all fields and they're correct
    private boolean checkValidInput(){
        // check if everything is not left empty
        if(userName.length()==EMPTY || fName.length()==EMPTY || lName.length()==EMPTY || password.length()==EMPTY || passwordCheck.length()==EMPTY) {
            Toast.makeText(this, "not inputted", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(profilePic==null) {
            Toast.makeText(this, "no profile picture", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password.getText().toString().equals(passwordCheck.getText().toString())){
            Toast.makeText(this, "passwords don't match", Toast.LENGTH_SHORT).show();
            return false;
        }
        // finish validations
        Pattern checkPassword=Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$");
        Pattern checkName=Pattern.compile("(^[a-zA-Z][a-zA-Z\\s]{0,20}[a-zA-Z]$)");
        Pattern checkUsername=Pattern.compile("^[a-zA-Z0-9_-]{4,16}$");
        Matcher passW=checkPassword.matcher(password.getText().toString());
        Matcher userN=checkUsername.matcher(userName.getText().toString());
        Matcher fname=checkName.matcher(fName.getText().toString());
        Matcher lname=checkName.matcher(lName.getText().toString());

        if(!lname.matches() || !fname.matches()) {
            Toast.makeText(this, "\"First And Last Names must contain letters only!\"", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!passW.matches()) {
            Toast.makeText(this, "Password must contain: at least 8 characters -uppercase letters -lowercase letters -numbers", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!userN.matches()) {
            Toast.makeText(this, "Username must be between 4 and 16 characters long and contain only: letters -numbers -underscores -hyphens", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // handling getting the picture to screen
    private void registerResult(){
        launcher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        profilePic=findViewById(R.id.ivPic);
                        Bundle extras = result.getData().getExtras();
                        if (extras != null) {
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            profilePic.setImageBitmap(imageBitmap);
                        }
                        // get picture from gallery
                        Uri image=result.getData().getData();
                        profilePic.setImageURI(image);
                    }catch (Exception e){
                        Toast.makeText(SubscribeActivity.this, "No picture selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}