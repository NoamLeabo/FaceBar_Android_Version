package com.example.facebar_android;

import android.content.Intent;
import android.graphics.Bitmap;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class AddPostActivity extends AppCompatActivity {
    private static final int ADD_POST_RESULT_OK = 222;
    private ActivityResultLauncher<Intent> launcher;
    private Bitmap newPic;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        final String[] content = new String[1];
        EditText editText = findViewById(R.id.tvWrite);
        RegisterResult();
        ImageButton post_post_btn = findViewById(R.id.post_post_btn);
        ImageButton cancel_post_post_btn = findViewById(R.id.del_Btn);
        Button addImg = findViewById(R.id.add_ivPic_btn);
        Button addImgGallery = findViewById(R.id.add_ivPic_gallery);
        Button addImgCam = findViewById(R.id.add_ivPic_cam);
        LinearLayout buttons = findViewById(R.id.buttons);

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

        post_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content[0] = editText.getText().toString();
                saveContent(content[0]);
                sendResultBack();
            }
        });

        cancel_post_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    public void saveContent(String content) {
        this.content = content;
    }

    public Bitmap getNewPic() {
        return this.newPic;
    }

    private void RegisterResult() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    ImageView pic = findViewById(R.id.ivPic);
                    Bundle extras = result.getData().getExtras();
                    if (extras != null) {
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        pic.setImageBitmap(imageBitmap);
                    }
                    Uri image = result.getData().getData();
                    pic.setImageURI(image);
                    saveImg(pic);
                } catch (Exception e) {
                    Toast.makeText(AddPostActivity.this, "no picture selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void saveImg(ImageView newPic) {
        newPic.setDrawingCacheEnabled(true);
        newPic.buildDrawingCache();
        this.newPic = Bitmap.createBitmap(newPic.getDrawingCache());
    }

    // Method to set the result and finish the activity
    private void sendResultBack() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("content", this.content);
        resultIntent.putExtra("newPic", this.newPic);
        setResult(ADD_POST_RESULT_OK, resultIntent);
        finish(); // Finish the current activity and return to the previous one
    }

    private void cancel() {
        finish(); // Finish the current activity and return to the previous one
    }
}
