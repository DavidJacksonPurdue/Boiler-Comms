package com.example.boiler_commslogin.viewpost;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.boiler_commslogin.R;
import com.example.boiler_commslogin.createpost.CreatePost;
import com.example.boiler_commslogin.createpost.topicModel;
import com.example.boiler_commslogin.data.MainActivity;
import com.example.boiler_commslogin.data.VoteLists;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ViewPostActivity extends AppCompatActivity {

    String userID;
    String username;
    String password;
    String postID;
    boolean image;
    boolean upvoted;
    boolean downvoted;

    Button backButton;
    TextView postName;
    TextView postAuthor;
    TextView postTopic;
    ImageView postImage;
    TextView postText;
    TextView commentText;
    Button postCommentButton;
    Button viewCommentButton;
    TextView voteText;
    Button upvoteButton;
    Button downvoteButton;
    Button saveButton;

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
        postID = getIntent().getStringExtra("POSTID");

        if (VoteLists.upvotedPosts.contains(this.postID)) {

        }

        if (VoteLists.downvotedPosts.contains(this.postID)) {

        }

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
        voteText = (TextView) findViewById(R.id.voteText);
        upvoteButton = (Button) findViewById(R.id.upvoteButton);
        downvoteButton = (Button) findViewById(R.id.downvoteButton);
        saveButton = (Button) findViewById(R.id.saveButton);

        //Get post data and populate fields with post data
        try {
            String str_result = null;
            str_result = (String) new ViewPost(getApplicationContext()).execute(this.postID).get(2000, TimeUnit.MILLISECONDS);

            if (!str_result.equals("error")) {

                //Load image if image is attached to the post
                if (image) {

                } else {

                }

                Toast.makeText(getApplicationContext(), "Post loaded successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Post loading failed!", Toast.LENGTH_LONG).show();
            }

            Log.d("viewpost", str_result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        //Setup click listeners for buttons
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnIntent();
            }
        });

        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        upvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        downvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void returnIntent() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
        intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
        intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
        startActivity(intent);
    }
}
