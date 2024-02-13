package com.example.facebar_android;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
        setContentView(R.layout.activity_subscribe);

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
                Toast.makeText(this, "nicee", Toast.LENGTH_SHORT).show();
                // write the user to json file name
                JSONObject user = new JSONObject();
                try {
                    user.put("username", userName.getText().toString());
                    user.put("password", password.getText().toString());
                    user.put("profilePic", profilePic);
                    writeObject(user);
                } catch (JSONException | IOException e) {
                    Toast.makeText(this, "problem occurred", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
                Intent intent=new Intent(SubscribeActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }
    // writing a JSON object to file users
    private void writeObject(JSONObject user) throws IOException {
        // Convert JsonObject to String Format
        String userString = user.toString();
        // Define the File Path and its Name
        File file = new File(this.getFilesDir(),"users");
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(userString);
        bufferedWriter.close();
    }

    //check if user inputed all fields and they're correct
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
        return true;
    }

    // handling getting the picture to screen
    private void registerResult(){
        launcher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            profilePic=findViewById(R.id.profilePic);
                            // get picture from gallery
                            Uri image=result.getData().getData();
                            profilePic.setImageURI(image);
                        }catch (Exception e){
                            Toast.makeText(SubscribeActivity.this, "No picture selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}