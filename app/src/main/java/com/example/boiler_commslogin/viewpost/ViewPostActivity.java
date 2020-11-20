package com.example.boiler_commslogin.viewpost;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.boiler_commslogin.R;
import com.example.boiler_commslogin.comment.MyCommentAdapter;
import com.example.boiler_commslogin.comment.sendComment;
import com.example.boiler_commslogin.comment.viewComments;
import com.example.boiler_commslogin.comment.viewMyUserComments;
import com.example.boiler_commslogin.createpost.CreatePost;
import com.example.boiler_commslogin.createpost.topicModel;
import com.example.boiler_commslogin.data.DownvoteTask;
import com.example.boiler_commslogin.data.MainActivity;
import com.example.boiler_commslogin.data.MyAdapter;
import com.example.boiler_commslogin.data.UpvoteTask;
import com.example.boiler_commslogin.data.VoteLists;
import com.example.boiler_commslogin.savepost.SavePost;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.example.boiler_commslogin.comment.MyCommentAdapter.hideKeyboard;

public class ViewPostActivity extends AppCompatActivity {

    String userID;
    String username;
    String password;
    String postID;
    String image = "null";
    String name;
    String author;
    String topic;
    String text;
    String votes;
    String date;
    String saved = "";
    boolean isSaved = false;
    String anonUser = "0";

    Button backButton;
    TextView postName;
    TextView postAuthor;
    TextView postTopic;
    ImageView postImage;
    TextView postText;
    EditText commentText;
    TextView postDate;
    Button postCommentButton;
    Button viewCommentButton;
    TextView voteText;
    Button upvoteButton;
    Button downvoteButton;
    Button saveButton;
    CheckBox anonComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userID = getIntent().getStringExtra("USERID");
        username = getIntent().getStringExtra("USERNAME");
        password = getIntent().getStringExtra("PASSWORD");
        postID = getIntent().getStringExtra("POSTID");

        //Get post data and populate fields with post data
        try {
            String str_result = null;
            str_result = (String) new ViewPost(getApplicationContext()).execute(this.postID, this.userID).get(2000, TimeUnit.MILLISECONDS);
            //str_result = (String) new ViewPost(getApplicationContext()).execute(this.postID).get(2000, TimeUnit.MILLISECONDS);

            if (!str_result.equals("error")) {
                Document postXML = null;
                try {
                    postXML = loadXMLFromString(str_result);
                }catch(Exception e){
                    e.printStackTrace();
                }

                if (postXML != null) {
                    postXML.getDocumentElement().normalize();
                    NodeList nList = postXML.getElementsByTagName("post");
                    for (int x = 0; x < nList.getLength(); x++) {
                        Element post = (Element) (nList.item(x));
                        topic = post.getAttribute("topicName");
                        name = post.getAttribute("postName");
                        text = post.getAttribute("postText");
                        date = post.getAttribute("postDate");
                        author = post.getAttribute("userName");
                        votes = post.getAttribute("voteTotal");
                        image = post.getAttribute("postImage");
                        saved = post.getAttribute("savedPostID");
                    }
                }

                Toast.makeText(getApplicationContext(), "Post loaded successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Post loading failed!", Toast.LENGTH_LONG).show();
                return;
            }

            Log.d("viewpost", str_result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        if (!image.equals("null")) {
            setContentView(R.layout.activity_viewpost_image);
            postImage = (ImageView) findViewById(R.id.postImage);
            String base64Image = image.split(",")[1];
            Log.d("Base64 Image", base64Image);
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            postImage.setImageBitmap(decodedByte);
        } else {
            setContentView(R.layout.activity_viewpost);
            postImage = null;
        }

        //Setup layout element objects
        backButton = (Button) findViewById(R.id.backButton);
        postName = (TextView) findViewById(R.id.postName);
        postAuthor = (TextView) findViewById(R.id.postAuthor);
        postTopic = (TextView) findViewById(R.id.postTopic);
        //postImage set above
        postText = (TextView) findViewById(R.id.postText);
        commentText = (EditText) findViewById(R.id.commentText);
        postCommentButton = (Button) findViewById(R.id.postCommentButton);
        viewCommentButton = (Button) findViewById(R.id.viewCommentButton);
        voteText = (TextView) findViewById(R.id.voteText);
        upvoteButton = (Button) findViewById(R.id.upvoteButton);
        downvoteButton = (Button) findViewById(R.id.downvoteButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        postDate = (TextView) findViewById(R.id.postDate);
        anonComment = (CheckBox) findViewById(R.id.anonCommentViewCheck);

        postName.setText(name);
        postAuthor.setText(author);
        postTopic.setText(topic);
        postText.setText(text);
        voteText.setText(votes);
        postDate.setText(date);

        if (!saved.equals("false")) {
            saveButton.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
            isSaved = true;
        } else {
            saveButton.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
            isSaved = false;
        }

        //Setup click listeners for buttons
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnIntent();
            }
        });

        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //mMultiLevelRecyclerView.toggleItemsGroup(getAdapterPosition());

                String text = commentText.getText().toString();
                if (!text.equals("")) {
                    int newParentID = -1;
                    String textBody = commentText.getText().toString();
                    Log.d("madeIT", textBody);
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    String commentTime = dtf.format(now);
                    String str_result = null;
                    String tempUserID = userID;
                    if (anonComment.isChecked()) {
                        tempUserID = anonUser;
                    }

                    try {
                        str_result= (String)new sendComment(getApplicationContext()).execute(postID,newParentID, textBody, tempUserID, commentTime).get(2000, TimeUnit.MILLISECONDS);

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                    Log.d("str_result",str_result);
                    commentText.setText("");
                            /*Item newComment = new Item(mItem.getLevel() + 1);
                            newComment.setCommentID(Integer.parseInt(str_result));
                            newComment.setParentID(newParentID);
                            newComment.setParentID(0);
                            newComment.setBody(textBody);
                            newComment.setTitle("you posted:");

                            mMultiLevelRecyclerView.openTill(mItem.getLevel());
                            notifyDataSetChanged();*/
                    Intent intent = new Intent(getApplicationContext(), viewComments.class);
                    intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                    intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                    intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                    intent.putExtra("POSTID", postID);
                    //myIntent.putExtra("key", value); //Optional parameters
                    startActivity(intent);
                }
            }
        });

        viewCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_view_comments);
                Intent intent = new Intent(getApplicationContext(), viewComments.class);
                intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                intent.putExtra("POSTID", postID);
                //Log.d("ViewPost PostID", "" + postID);
                startActivity(intent);
            }
        });

        upvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!VoteLists.upvotedPosts.contains(postID) && !VoteLists.downvotedPosts.contains(postID)) {
                    upvoteButton.setEnabled(false);
                    downvoteButton.setEnabled(false);

                    if (!VoteLists.upvotedPosts.contains(postID) && !VoteLists.downvotedPosts.contains(postID)) {
                        //VoteLists.downvotedPosts.add(postId.getText().toString());
                        voteText.setText(Integer.toString(Integer.parseInt(voteText.getText().toString()) + 1));
                    }

                    upvoteButton.setEnabled(true);
                    downvoteButton.setEnabled(true);

                    String upvote_result = "";
                    try {
                        upvote_result = (String) new UpvoteTask(getApplicationContext()).execute(postID, userID).get(2000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    Log.d("Upvote Result", upvote_result);
                    if (upvote_result.equals("")) {
                        Toast.makeText(getApplicationContext(), "Failed To Upvote Post At This Time", Toast.LENGTH_SHORT).show();
                    } else if (upvote_result.equals("upvoted")) {
                        Toast.makeText(getApplicationContext(), "You have already upvoted this post.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Successfully Upvoted Post", Toast.LENGTH_SHORT).show();
                        VoteLists.upvotedPosts.add(postID);
                    }
                }
            }
        });

        downvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!VoteLists.upvotedPosts.contains(postID) && !VoteLists.downvotedPosts.contains(postID)) {
                    upvoteButton.setEnabled(false);
                    downvoteButton.setEnabled(false);

                    if (!VoteLists.upvotedPosts.contains(postID) && !VoteLists.downvotedPosts.contains(postID)) {
                        //VoteLists.downvotedPosts.add(postId.getText().toString());
                        voteText.setText(Integer.toString(Integer.parseInt(voteText.getText().toString()) - 1));
                    }

                    upvoteButton.setEnabled(true);
                    downvoteButton.setEnabled(true);

                    String downvote_result = "";
                    try {
                        downvote_result = (String) new DownvoteTask(getApplicationContext()).execute(postID, userID).get(2000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if (downvote_result.equals("")) {
                        Toast.makeText(getApplicationContext(), "Failed To Downvote Post At This Time", Toast.LENGTH_SHORT).show();
                    } else if (downvote_result.equals("downvoted")) {
                        Toast.makeText(getApplicationContext(), "You have already downvoted this post.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Successfully Downvoted Post", Toast.LENGTH_SHORT).show();
                        VoteLists.downvotedPosts.add(postID);
                    }
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSaved) {
                    String str_result = "";
                    try {
                        str_result= (String) new SavePost(getApplicationContext()).execute(userID, postID).get(2000, TimeUnit.MILLISECONDS);

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }

                    if (str_result.equals("Success")) {
                        isSaved = true;
                        saveButton.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                    }
                }
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

    public Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }
}
