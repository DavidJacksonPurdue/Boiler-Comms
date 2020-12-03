package com.example.boiler_commslogin.createpost;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boiler_commslogin.R;
import com.example.boiler_commslogin.data.MainActivity;
import com.example.boiler_commslogin.data.model.SendUserCredentials;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CreatePostActivity extends AppCompatActivity {
//Git Repository Test
    TextView postNameText;
    TextView postTopicText;
    ImageView imageView;
    TextView postText;
    TextView postLinkText;

    Button loadButton;
    Button postButton;
    CheckBox anonymousCheckbox;

    public static final int PICK_IMAGE = 100;
    final String anonUser = "0";
    Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpost);

        postNameText = (TextView) findViewById(R.id.postNameText);
        postTopicText = (TextView) findViewById(R.id.postTopicText);
        imageView = (ImageView) findViewById(R.id.imageView);
        postText = (TextView) findViewById(R.id.postText);
        postLinkText = (TextView) findViewById(R.id.postLinkEntered);

        loadButton = (Button) findViewById(R.id.loadButton);
        postButton = (Button) findViewById(R.id.postButton);
        anonymousCheckbox = (CheckBox) findViewById(R.id.anonymousCheck);

        //Load button functionality
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open gallery
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        //Post button functionality
        postButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                try {
                    String userID = getIntent().getStringExtra("USERID");
                    String postID = "2";
                    String topicID = (String) new topicModel(getApplicationContext()).execute("0", postTopicText.getText().toString()).get(2000, TimeUnit.MILLISECONDS);
                    String postName = postNameText.getText().toString();
                    //String postText = (String) postText.getText();
                    String post_text = postText.getText().toString();
                    String postLink = postLinkText.getText().toString();
                    if (postName.length() == 0 || postTopicText.getText().length() == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CreatePostActivity.this, R.style.AlertDialogTheme);
                        builder.setTitle("Error");
                        builder.setMessage("Title and Topic name cannot be empty")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // FIRE ZE MISSILES!
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        return;
                    }


                    if (postLink.equals("")) {
                        postLink = "null";
                    } else if (postLink.length() > 0) {
                        if (!postLink.startsWith("http://") && !postLink.startsWith("https://")) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(CreatePostActivity.this, R.style.AlertDialogTheme);
                            builder.setTitle("Error");
                            builder.setMessage("Link must start with 'http://' or 'https://'")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // FIRE ZE MISSILES!
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            return;
                        }
                    }
                    String postImage = "null";
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    String postDate = dtf.format(now);
                    String str_result = null;

                    /** Check to see if the anonymous post checkbox has been checked **/
                    if (anonymousCheckbox.isChecked()) {
                        // Set the id to the id of an anonymous user
                        userID = anonUser;
                    }

                    if (imageURI != null) {
                        String base64image = uriToBitmap(imageURI);
                        Log.d("imageIsHere", base64image);
                        //String extension = imageURI.getPath().toString().substring(imageURI.toString().lastIndexOf("."));
                        String extension = (String) getApplicationContext().getContentResolver().getType(imageURI);
                        extension = extension.substring(extension.lastIndexOf("/") + 1);
                        Log.d("extension", extension);
                        str_result = (String) new CreatePost(getApplicationContext()).execute(userID, postID, topicID, postName, post_text, postLink, postDate, base64image, extension).get(2000, TimeUnit.MILLISECONDS);
                    }
                    else {
                        str_result = (String) new CreatePost(getApplicationContext()).execute(userID, postID, topicID, postName, post_text, postLink, postDate, "<<>>", "<<>>").get(2000, TimeUnit.MILLISECONDS);
                    }
                    if (str_result.equals("Success")) {
                        Toast.makeText(getApplicationContext(), "Post created successfully", Toast.LENGTH_LONG).show();
                        setContentView(R.layout.activity_main);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                        intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                        intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Post creation failed", Toast.LENGTH_LONG).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return getEncoded64ImageStringFromBitmap(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode ==  RESULT_OK && requestCode == PICK_IMAGE) {
            imageURI = data.getData();
            imageView.setImageURI(imageURI);
        }
    }
}