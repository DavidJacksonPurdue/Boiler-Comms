package com.example.boiler_commslogin.viewpost;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.boiler_commslogin.R;
import com.example.boiler_commslogin.data.MainActivity;

public class ViewPostActivity extends AppCompatActivity {

    String userID;
    String username;
    String password;
    boolean image;

    Button backButton;
    TextView postName;
    TextView postAuthor;
    TextView postTopic;
    ImageView postImage;
    TextView postText;
    TextView commentText;
    Button postCommentButton;
    Button viewCommentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean image = getIntent().getBooleanExtra("IMAGE", false);
        if (image) {
            setContentView(R.layout.activity_viewpost_image);
            postImage = (ImageView) findViewById(R.id.postImage);
        } else {
            setContentView(R.layout.activity_viewpost);
            postImage = null;
        }

        userID = getIntent().getStringExtra("USERID");
        username = getIntent().getStringExtra("USERNAME");
        password = getIntent().getStringExtra("PASSWORD");

        //Setup layout element objects
        backButton = (Button) findViewById(R.id.backButton);
        postName = (TextView) findViewById(R.id.postName);
        postAuthor = (TextView) findViewById(R.id.postAuthor);
        postTopic = (TextView) findViewById(R.id.postTopic);
        //postImage set above
        postText = (TextView) findViewById(R.id.postText);
        commentText = (TextView) findViewById(R.id.commentText);
        postCommentButton = (Button) findViewById(R.id.postCommentButton);
        viewCommentButton = (Button) findViewById(R.id.viewCommentButton);

        //Setup click listeners for buttons

    }

    public void returnIntent() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
        intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
        intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
        startActivity(intent);
    }
}
