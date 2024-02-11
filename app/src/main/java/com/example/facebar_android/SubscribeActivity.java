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

public class SubscribeActivity extends AppCompatActivity {

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
        profilePic=findViewById(R.id.profilePic);
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

        openCameraBtn.setOnClickListener(view -> {
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            launcher.launch(intent);
        });
    }
    private void registerResult(){
        launcher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
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