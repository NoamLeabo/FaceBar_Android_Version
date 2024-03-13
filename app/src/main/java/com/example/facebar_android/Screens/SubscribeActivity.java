package com.example.facebar_android.Screens;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.facebar_android.Users.ActiveUser;
import com.example.facebar_android.R;
import com.example.facebar_android.API.UsersAPI;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubscribeActivity extends AppCompatActivity {

    final int EMPTY=0;
    private int status;
    private UsersAPI usersAPI;
    private ActiveUser activeUser;
    EditText fName,lName,password,userName,passwordCheck;
    Button openCameraBtn,subscribeBtn,galleryBtn;
    ImageView profilePic;
    // launches the intent to open gallery
    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersAPI=new UsersAPI();

        if (FeedActivity.NIGHT_MODE == 0)
            setContentView(R.layout.activity_subscribe);
        else
            setContentView(R.layout.activity_subscribe_dark);

        status = getIntent().getIntExtra("edit", 0);
        userName=findViewById(R.id.userName);
        profilePic=findViewById(R.id.ivPic);
        fName=findViewById(R.id.fName);
        lName=findViewById(R.id.lName);
        password=findViewById(R.id.password);
        passwordCheck=findViewById(R.id.passwordCheck);
        openCameraBtn=findViewById(R.id.openCameraBtn);
        subscribeBtn=findViewById(R.id.subscribeBtn);
        galleryBtn=findViewById(R.id.galleryBtn);
        registerResult();

        if (status == 404) {
            activeUser = ActiveUser.getInstance();
            userName.setText(activeUser.getUsername());
            userName.setEnabled(false);
            lName.setText(activeUser.getlName());
            lName.setEnabled(false);
            fName.setText(activeUser.getfName());
            fName.setEnabled(false);
            subscribeBtn.setText("Update Values");
            password.setText(activeUser.getPassword());
            passwordCheck.setText(activeUser.getPassword());
            byte[] bytes= android.util.Base64.decode(activeUser.getProfileImage(), android.util.Base64.DEFAULT);
            // Initialize bitmap
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            // set bitmap on imageView
            profilePic.setImageBitmap(bitmap);
        }

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
                profilePic.destroyDrawingCache(); // Clear the drawing cache
                profilePic.setDrawingCacheEnabled(true); // Enable drawing cache
                profilePic.buildDrawingCache(); // Build the drawing cache
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bitmap = Bitmap.createBitmap(profilePic.getDrawingCache());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                String image = Base64.getEncoder().encodeToString(bytes);
                if (status == 404) {

                    usersAPI.updateUser(activeUser.getUsername(), password.getText().toString(), image, new UsersAPI.AddUserCallback() {
                        @Override
                        public void onSuccess() {
                            usersAPI.getUser(userName.getText().toString(), password.getText().toString(), new UsersAPI.AddUserCallback() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(SubscribeActivity.this, "Values were updated!", Toast.LENGTH_SHORT).show();
                                    setResult(404);
                                    finish();
                                }

                                @Override
                                public void onError(String message) {
                                    Toast.makeText(SubscribeActivity.this, "ERROR", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(SubscribeActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    usersAPI.addUser(fName.getText().toString(), lName.getText().toString(), userName.getText().toString(), password.getText().toString(), image, new UsersAPI.AddUserCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(SubscribeActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(SubscribeActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
            Toast.makeText(this, "\"First& Last Name must contain letters only and be at least 2 characters long!\"", Toast.LENGTH_SHORT).show();
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