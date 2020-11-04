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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.boiler_commslogin.R;
import com.example.boiler_commslogin.comment.sendComment;
import com.example.boiler_commslogin.comment.viewComments;
import com.example.boiler_commslogin.comment.viewMyUserComments;
import com.example.boiler_commslogin.createpost.CreatePost;
import com.example.boiler_commslogin.createpost.topicModel;
import com.example.boiler_commslogin.data.MainActivity;
import com.example.boiler_commslogin.data.VoteLists;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    boolean upvoted = false;
    boolean downvoted = false;
    boolean saved = false;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userID = getIntent().getStringExtra("USERID");
        username = getIntent().getStringExtra("USERNAME");
        password = getIntent().getStringExtra("PASSWORD");
        postID = getIntent().getStringExtra("POSTID");

        if (VoteLists.upvotedPosts.contains(this.postID)) {
            upvoted = true;
        }

        if (VoteLists.downvotedPosts.contains(this.postID)) {
            downvoted = true;
        }

        //Get post data and populate fields with post data
        try {
            String str_result = null;
            str_result = (String) new ViewPost(getApplicationContext()).execute(this.postID).get(2000, TimeUnit.MILLISECONDS);

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

        postName.setText(name);
        postAuthor.setText(author);
        postTopic.setText(topic);
        postText.setText(text);
        voteText.setText(votes);
        postDate.setText(date);

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
                if (!commentText.getText().toString().equals("")) {

                }
            }
        });

        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //mMultiLevelRecyclerView.toggleItemsGroup(getAdapterPosition());

                String text = commentText.getText().toString();
                if (!text.equals("")) {
                    //hideKeyboard(((Activity) getApplicationContext()));
                    int newParentID = -1;
                    String textBody = commentText.getText().toString();
                    Log.d("madeIT", textBody);
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    String commentTime = dtf.format(now);
                    String str_result = null;

                    try {
                        str_result= (String)new sendComment(getApplicationContext()).execute("0",newParentID, textBody, "0", commentTime).get(2000, TimeUnit.MILLISECONDS);

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
                    Intent myIntent = new Intent(getApplicationContext(), viewComments.class);
                    //myIntent.putExtra("key", value); //Optional parameters
                    startActivity(myIntent);
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
                Log.d("ViewPost PostID", "" + postID);
                startActivity(intent);
            }
        });

        upvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!upvoted && !downvoted) {

                }
            }
        });

        downvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!upvoted && !downvoted) {

                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!saved) {
                    saved = true;
                    saveButton.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
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
