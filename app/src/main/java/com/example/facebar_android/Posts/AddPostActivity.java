package com.example.facebar_android.Posts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.facebar_android.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class AddPostActivity extends AppCompatActivity {
    public static final int ADD_POST_BITMAP = 222;
    public static final int ADD_POST_URI = 333;
    public static final int ADD_POST_TEXT = 444;
    private ActivityResultLauncher<Intent> launcher;
    private Bitmap bitmap;
    private String content;
    private Uri uri;
    private boolean isUri = false;
    private boolean isImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        // retrieving all necessary fields from xml
        final String[] content = new String[1];
        EditText editText = findViewById(R.id.tvWrite);
        RegisterResult();
        ImageButton post_post_btn = findViewById(R.id.post_post_btn);
        ImageButton cancel_post_post_btn = findViewById(R.id.del_Btn);
        Button addImg = findViewById(R.id.add_ivPic_btn);
        Button addImgGallery = findViewById(R.id.add_ivPic_gallery);
        Button addImgCam = findViewById(R.id.add_ivPic_cam);
        LinearLayout buttons = findViewById(R.id.buttons);
        // setting all btns' listeners
        addImg.setOnClickListener(v -> {
            buttons.setVisibility(View.VISIBLE);
            addImg.setVisibility(View.GONE);
        });

        addImgCam.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            launcher.launch(intent);
        });

        addImgGallery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcher.launch(intent);
        });

        post_post_btn.setOnClickListener(v -> {
            content[0] = editText.getText().toString();
            saveContent(content[0]);
            if (!Objects.equals(content[0], "")) {
                sendResultBack();
            } else {
                Toast.makeText(getContext(), "A new Post can not be empty!", Toast.LENGTH_SHORT).show();
            }
        });
        cancel_post_post_btn.setOnClickListener(v -> cancel());
    }
    private Context getContext(){
        return this;
    }

    public void saveContent(String content) {
        this.content = content;
    }

    private void RegisterResult() {
        // the open camera intent
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            try {
                ImageView pic = findViewById(R.id.ivPic);
                Bundle extras = result.getData().getExtras();
                if (extras != null && extras.get("data") != null) {
                    // image is captured from camera
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    saveBitMap(imageBitmap);
                    pic.setImageBitmap(imageBitmap);
                    isImage = true;
                } else {
                    // image is selected from gallery
                    Uri imageUri = result.getData().getData();
                    saveUri(imageUri);
                    isUri = true;
                    isImage = true;
                    try {
                        // Convert URI to Bitmap
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        pic.setImageBitmap(bitmap);
                        saveBitMap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(AddPostActivity.this, "No picture selected", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void saveBitMap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public void saveUri(Uri uri) {
        this.uri = uri;
    }


    private void sendResultBack() {
        // method that sets the result and finish the activity
        Intent resultIntent = new Intent();
        if (isImage) {
            if (!isUri) {
                // sends back the results with img by bitmap
                resultIntent.putExtra("content", this.content);
                resultIntent.putExtra("newPic", this.bitmap);
                setResult(ADD_POST_BITMAP, resultIntent);
                finish(); // finish the current activity and return to the previous one
            } else {
                // sends back the results with img by uri
                resultIntent.putExtra("content", this.content);
                resultIntent.putExtra("newPic", this.uri);
                setResult(ADD_POST_URI, resultIntent);
                finish();
            }
        } else {
            // sends back the results with no img
            resultIntent.putExtra("content", this.content);
            setResult(ADD_POST_TEXT, resultIntent);
            finish();
        }
    }

    private void cancel() {
        finish(); // finish the current activity and return to the previous one
    }
}
