package com.example.facebar_android.Screens;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.facebar_android.Users.ActiveUser;
import com.example.facebar_android.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Objects;

/**
 * Activity class for adding a new post.
 * Handles the UI and interactions for creating a new post.
 */
public class AddPostActivity extends AppCompatActivity {

    /* status code for when sending results back to the feed screen */

    // adding a post with image that is saved in Bitmap format
    public static final int ADD_POST_BITMAP = 222;
    // adding a post with image that is saved in URI format
    public static final int ADD_POST_URI = 333;
    // adding a post with text only
    public static final int ADD_POST_TEXT = 444;

    private ActivityResultLauncher<Intent> launcher;
    private Bitmap bitmap;
    private String content;
    private ImageView pic;
    private String _id;
    private Uri uri;
    private boolean isUri = false;
    private boolean isImage = false;
    private String base;
    private ActiveUser activeUser;
    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // determine with which theme to load the add-post-screen screen
        if (FeedActivity.NIGHT_MODE == 0)
            setContentView(R.layout.activity_add_post);
        else
            setContentView(R.layout.activity_add_post_dark);

        // get the current active user
        activeUser = ActiveUser.getInstance();

        // we attach the XML fields to vars
        ImageButton post_post_btn = findViewById(R.id.post_post_btn);
        ImageButton cancel_post_post_btn = findViewById(R.id.del_Btn);
        Button addImg = findViewById(R.id.add_ivPic_btn);
        Button addImgGallery = findViewById(R.id.add_ivPic_gallery);
        Button addImgCam = findViewById(R.id.add_ivPic_cam);
        LinearLayout buttons = findViewById(R.id.buttons);
        EditText editText = findViewById(R.id.tvWrite);
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        ImageView profileImg = findViewById(R.id.profile_img);
        pic = findViewById(R.id.ivPic);

        /* setting all btns' listeners */

        // show the add image from gallery/cam btns
        addImg.setOnClickListener(v -> {
            buttons.setVisibility(View.VISIBLE);
            addImg.setVisibility(View.GONE);
        });

        // clicking on add-Img-from-Cam opens the device's cam
        addImgCam.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            launcher.launch(intent);
        });

        // clicking on add-Img-from-gallery opens the device's gallery to choose an image from
        addImgGallery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcher.launch(intent);
        });

        // clicking on post-a-post will publish the brand-new/edited post
        post_post_btn.setOnClickListener(v -> {
            // save the post's content in order to pass it as extra when the activity closes
            final String[] content = new String[1];
            content[0] = editText.getText().toString();
            saveContent(content[0]);
            // we make sure the content is not empty before sending post to be published since such case is not valid
            if (!Objects.equals(content[0], "")) {
                sendResultBack();
            } else {
                Toast.makeText(getContext(), "A Post's content must not be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        // clicking on cancel-post-a-post will close the screen without making any changes
        cancel_post_post_btn.setOnClickListener(v -> cancel());

        // if the screen received an extra of "author" it means there a post that needs to be edited
        if (getIntent().getStringExtra("author") != null) {
            // we "turn-up" editMode to notify that we do not create a new post rather than updating an existing one
            editMode = true;
            // we save the rest of the edited post fields value - also received as extras
            this._id = getIntent().getStringExtra("id");
            this.base = getIntent().getStringExtra("imageView");
            this.content = getIntent().getStringExtra("content");
        }

        // set the post's author as the active user
        tvAuthor.setText(activeUser.getUsername());

        /* display active user profPic at the top of the post */
        byte[] bytes = android.util.Base64.decode(activeUser.getProfileImage(), android.util.Base64.DEFAULT);
        // Initialize bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        // set bitmap on imageView
        profileImg.setImageBitmap(bitmap);

        // retrieving all necessary fields from xml
        editText.setHint("What's on your mind " + activeUser.getUsername() + "?");

        // if we edit a post we wish to present the current post's image and content if those have been given as extras
        if (editMode && !base.equals("")) {
            bytes = android.util.Base64.decode(base, android.util.Base64.DEFAULT);
            // Initialize bitmap
            this.bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // set bitmap on imageView
            pic.setImageBitmap(this.bitmap);
            // indicating the post includes an image
            isImage = true;
        }
        if (editMode)
            editText.setText(this.content);

        // wait for user to create his post and maybe add image and then we register his actions' results
        RegisterResult();
    }

    /**
     * Returns the context of the activity.
     *
     * @return the context of the activity
     */
    private Context getContext() {
        return this;
    }

    /**
     * Saves the content of the post.
     *
     * @param content the content of the post
     */
    public void saveContent(String content) {
        this.content = content;
    }

    /**
     * Registers the result launcher for handling image capture and selection.
     */
    private void RegisterResult() {

        // TODO: examine an optional removal of the whole "base" conversion and saving thing since it is not being
        //  used in this activity anyway, but in the feed activity

        // register the result launcher for starting an activity and handling the result
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            try {
                // get the extras from the result data
                Bundle extras = result.getData().getExtras();
                if (extras != null && extras.get("data") != null) {
                    // ff the extras contain image data the image is captured from the camera
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    // Save the bitmap image
                    saveBitMap(imageBitmap);
                    // Set the image to the ImageView to show the user the captured image
                    pic.setImageBitmap(imageBitmap);

                    // enable and build the drawing cache for the ImageView
                    pic.setDrawingCacheEnabled(true);
                    pic.buildDrawingCache();

                    // convert the bitmap to a byte array and encode it to a Base64 string
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap bitmap = Bitmap.createBitmap(pic.getDrawingCache());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 98, stream);
                    byte[] bytes = stream.toByteArray();
                    this.base = Base64.getEncoder().encodeToString(bytes);
                    // indicate that the post contains an image
                    isImage = true;
                } else {
                    // if the extras do not contain image data it means the image was selected from the gallery
                    Uri imageUri = result.getData().getData();
                    // save the image in URI format
                    saveUri(imageUri);
                    // indicate that the post contains an image and that it was saved as URI
                    isUri = true;
                    isImage = true;
                    try {
                        // convert the URI to a Bitmap
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        // set the image to the ImageView
                        pic.setImageBitmap(bitmap);

                        // convert the bitmap to a byte array and encode it to a Base64 string
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 98, stream);
                        byte[] bytes = stream.toByteArray();
                        this.base = Base64.getEncoder().encodeToString(bytes);
                        // Save the bitmap image
                        saveBitMap(bitmap);
                    } catch (IOException e) {
                        // catches any errors
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                // handle the case where no image was selected
                Toast.makeText(AddPostActivity.this, "No image was selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Saves the bitmap image.
     *
     * @param bitmap the bitmap image to save
     */
    public void saveBitMap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * Saves the URI of the image.
     *
     * @param uri the URI of the image to save
     */
    public void saveUri(Uri uri) {
        this.uri = uri;
    }

    /**
     * Sends the result back to the previous activity.
     */
    private void sendResultBack() {
        // method that sets the result and finish the activity
        Intent resultIntent = new Intent();
        // if the post contains an image
        if (isImage) {
            // if the image was saved in URI format
            if (isUri) {
                // in case this is an edited post we indicate it as extra
                if (editMode)
                    resultIntent.putExtra("edit", _id);
                // pass the post's image and content as extras
                resultIntent.putExtra("content", this.content);
                resultIntent.putExtra("newPic", this.uri);
                // return the status code "adding post with image in URI format"
                setResult(ADD_POST_URI, resultIntent);
                // finish the addPostActivity and sends back the results
                finish();
            }
            // else - the image was saved in Bitmap format
            else {
                // in case this is an edited post we indicate it as extra
                if (editMode)
                    resultIntent.putExtra("edit", _id);
                // pass the post's image and content as extras
                resultIntent.putExtra("content", this.content);
                resultIntent.putExtra("newPic", this.bitmap);
                // return the status code "adding post with image in Bitmap format"
                setResult(ADD_POST_BITMAP, resultIntent);
                // finish the addPostActivity and sends back the results
                finish();
            }
        }
        // if the post does not contain an image
        else {
            // in case this is an edited post we indicate it as extra
            if (editMode)
                resultIntent.putExtra("edit", _id);
            // pass the post's content as extra
            resultIntent.putExtra("content", this.content);
            // return the status code if "adding post with text content only"
            setResult(ADD_POST_TEXT, resultIntent);
            // finish the addPostActivity and sends back the results
            finish();
        }
    }

    /**
     * Cancels the current activity and returns to the previous one.
     */
    private void cancel() {
        // finish the addPostActivity and sends back the results
        finish();
    }
}