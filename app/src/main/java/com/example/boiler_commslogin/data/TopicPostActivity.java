package com.example.boiler_commslogin.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.boiler_commslogin.viewpost.ViewPostActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boiler_commslogin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class TopicPostActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> username = new ArrayList<>();
    ArrayList<String> topic = new ArrayList<>();
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> body = new ArrayList<>();
    ArrayList<String> image = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> votecount = new ArrayList<>();
    ArrayList<String> userID = new ArrayList<>();
    ArrayList<String> topicID = new ArrayList<>();
    ArrayList<String> postID = new ArrayList<>();
    Context context = this;
    ArrayList<String> topicNames = new ArrayList<>();
    ArrayList<String> topicIDNumbers = new ArrayList();
    ArrayList<String> upvotedPosts = new ArrayList<>();
    ArrayList<String> downvotedPosts = new ArrayList<>();
    int currentTopicIndex = 0;
    final String anonUser = "0";
    final String deletedUser = "-1";


    public static Document loadXMLFromString(String xml) throws Exception
    {
        xml = xml.replaceAll("[^\\x20-\\x7e]","");
        xml = xml.replaceAll("[^\\u0000-\\uFFFF]", "");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_post);
        String thisUserID = getIntent().getStringExtra("USERID");
        Button followTopic = findViewById(R.id.followTopic);


        // Find and store the recycler view
        recyclerView = findViewById(R.id.topicRecyclerView);
        String str_result = null;
        String upvote_result = null;
        String downvote_result = null;
        // Temporary topic ID
        String topicId = getIntent().getStringExtra("TOPICID");

        // Retrive all of the posts from the database with the associated topic id
        try {
            str_result = (String) new getPostsByTopicModel(this).execute(topicId).get(2000, TimeUnit.MILLISECONDS);
            upvote_result = (String) new MainActivity.LoadUpvoteList(this).execute(thisUserID).get(2000, TimeUnit.MILLISECONDS);
            downvote_result = (String) new MainActivity.LoadDownvoteList(this).execute(thisUserID).get(2000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


        // UPDATE THE TEXT VIEW


        Document postXML = null;
        Document upvoteXML = null;
        Document downvoteXML = null;

        try {
            postXML = loadXMLFromString(str_result);
            upvoteXML = loadXMLFromString(upvote_result);
            downvoteXML = loadXMLFromString(downvote_result);
        }catch(Exception e){
            e.printStackTrace();
        }

        if (postXML != null) {
            postXML.getDocumentElement().normalize();
            NodeList nList = postXML.getElementsByTagName("post");
            for (int x = 0; x < nList.getLength(); x++) {
                Element Post = (Element) (nList.item(x));
                postID.add(Post.getAttribute("postID"));
                userID.add(Post.getAttribute("userID"));
                topicID.add(Post.getAttribute("topicID"));
                topic.add(Post.getAttribute("topicName"));
                title.add(Post.getAttribute("postName"));
                body.add(Post.getAttribute("postText"));
                time.add(Post.getAttribute("postDate"));
                username.add(Post.getAttribute("userName"));
                votecount.add(Post.getAttribute("voteTotal"));
                image.add(Post.getAttribute("postImage"));
            }
        }

        if (upvoteXML != null) {
            upvoteXML.getDocumentElement().normalize();
            NodeList nList = upvoteXML.getElementsByTagName("post");

            for (int x = 0; x < nList.getLength(); x++) {
                Element Post = (Element) (nList.item(x));
                upvotedPosts.add(Post.getAttribute("postID"));
            }
        }

        VoteLists.upvotedPosts = upvotedPosts;

        if (downvoteXML != null) {
            downvoteXML.getDocumentElement().normalize();
            NodeList nList = downvoteXML.getElementsByTagName("post");

            for (int x = 0; x < nList.getLength(); x++) {
                Element Post = (Element) (nList.item(x));
                downvotedPosts.add(Post.getAttribute("postID"));
            }
        }

        VoteLists.downvotedPosts = downvotedPosts;
        final MyAdapter myAdapter = new MyAdapter(this, username, topic, title, body, image, time, votecount, postID, topicID, userID);
        final int upvote_id = 0;
        final int downvote_id = 1;
        final int user_pos = 2;
        final int title_pos = 3;
        final int topic_pos = 4;
        myAdapter.setListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemSelected(int position, View view, ArrayList<Object> object) {
                if (position == upvote_id) {

                    Log.d("Upvote List", VoteLists.upvotedPosts.toString());
                    Log.d("Downvote List", VoteLists.downvotedPosts.toString());
                    // include functionality for upvote button
                    String upvote_result = "";
                    try {
                        String postID = (String) object.get(0);
                        String userID = getIntent().getStringExtra("USERID");
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
                        VoteLists.upvotedPosts.add(object.toArray()[0].toString());
                    }
                }
                else if (position == downvote_id) {

                    Log.d("Upvote List", VoteLists.upvotedPosts.toString());
                    Log.d("Downvote List", VoteLists.downvotedPosts.toString());
                    // include downvote functionality
                    String downvote_result = "";
                    try {
                        String postID = (String) object.get(0);
                        String userID = getIntent().getStringExtra("USERID");
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
                        VoteLists.downvotedPosts.add(object.toArray()[0].toString());
                    }
                } else if (position == user_pos) {
                    if (!(object.get(0).toString().equals(anonUser) || object.get(0).toString().equals(deletedUser))) {
                        setContentView(R.layout.activity_public_profile);
                        Intent intent = new Intent(getApplicationContext(), PublicProfilePage.class);
                        intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                        intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                        intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                        intent.putExtra("PUBLIC_USER", object.get(0).toString());
                        startActivity(intent);
                    }
                } else if (position == title_pos) {
                    Intent intent = new Intent(getApplicationContext(), ViewPostActivity.class);
                    intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                    intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                    intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                    intent.putExtra("POSTID", (String) object.get(0));
                    startActivity(intent);
                }
                else if (position == topic_pos) {
                    setContentView(R.layout.activity_topic_post);
                    Intent intent = new Intent(getApplicationContext(), TopicPostActivity.class);
                    intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                    intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                    intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                    intent.putExtra("TOPICID", object.get(0).toString());
                    startActivity(intent);
                }
            }
        });

        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Store them in an adapter

        // Create the autofill listner
        try {
            str_result = (String) new getTopics(this).execute().get(2000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        postXML = null;
        try {
            postXML = loadXMLFromString(str_result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (postXML != null) {
            postXML.getDocumentElement().normalize();
            NodeList nList = postXML.getElementsByTagName("topic");
            for (int x = 0; x < nList.getLength(); x++) {
                Element Post = (Element) (nList.item(x));
                topicIDNumbers.add(Post.getAttribute("topicID"));
                topicNames.add(Post.getAttribute("topicName"));
            }
        }

        // UPDATE THE TOPIC TEXT
        String topicName = "Topic";
        for (int i = 0; i < topicIDNumbers.size(); i++) {
            topicIDNumbers.get(i);
            if (topicId.equals(topicIDNumbers.get(i))) {
                System.out.println("woo");
                topicName = topicNames.get(i);
            }
        }
        TextView titleText = findViewById(R.id.topicNameTitle);
        titleText.setText(topicName);


        /***
         * CREATE CODE FOR AUTOFILL LISTENER
         * OOF
         */
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.topicSearch);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, topicNames);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String thisUserID = getIntent().getStringExtra("USERID");
            String pos;
            TextView titleText = findViewById(R.id.postNameText);

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView titleText = findViewById(R.id.topicNameTitle);
                // Get the topic Id from the selected dropdown list
                for (int i = 0; i < topicNames.size(); i++) {
                    if (parent.getItemAtPosition(position).equals(topicNames.get(i))) {
                        titleText.setText(topicNames.get(i));
                        pos = topicIDNumbers.get(i);
                        break;
                    }
                }
                // Get the new posts
                username.clear();
                topic.clear();
                title.clear();
                body.clear();
                image.clear();
                time.clear();
                votecount.clear();
                userID.clear();
                topicID.clear();
                postID.clear();


                String str_result = null;
                String upvote_result = null;
                String downvote_result = null;
                currentTopicIndex = Integer.parseInt(pos);
                try {
                    str_result = (String) new getPostsByTopicModel(context).execute(pos).get(2000, TimeUnit.MILLISECONDS);
                    upvote_result = (String) new MainActivity.LoadUpvoteList(context).execute(thisUserID).get(2000, TimeUnit.MILLISECONDS);
                    downvote_result = (String) new MainActivity.LoadDownvoteList(context).execute(thisUserID).get(2000, TimeUnit.MILLISECONDS);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }

                Document postXML = null;
                Document upvoteXML = null;
                Document downvoteXML = null;

                try {
                    postXML = loadXMLFromString(str_result);
                    upvoteXML = loadXMLFromString(upvote_result);
                    downvoteXML = loadXMLFromString(downvote_result);
                }catch(Exception e){
                    e.printStackTrace();
                }

                if (postXML != null) {
                    postXML.getDocumentElement().normalize();
                    NodeList nList = postXML.getElementsByTagName("post");
                    for (int x = 0; x < nList.getLength(); x++) {
                        Element Post = (Element) (nList.item(x));
                        postID.add(Post.getAttribute("postID"));
                        userID.add(Post.getAttribute("userID"));
                        topicID.add(Post.getAttribute("topicID"));
                        topic.add(Post.getAttribute("topicName"));
                        title.add(Post.getAttribute("postName"));
                        body.add(Post.getAttribute("postText"));
                        time.add(Post.getAttribute("postDate"));
                        username.add(Post.getAttribute("userName"));
                        votecount.add(Post.getAttribute("voteTotal"));
                        image.add(Post.getAttribute("postImage"));
                    }
                }

                if (upvoteXML != null) {
                    upvoteXML.getDocumentElement().normalize();
                    NodeList nList = upvoteXML.getElementsByTagName("post");

                    for (int x = 0; x < nList.getLength(); x++) {
                        Element Post = (Element) (nList.item(x));
                        upvotedPosts.add(Post.getAttribute("postID"));
                    }
                }

                VoteLists.upvotedPosts = upvotedPosts;

                if (downvoteXML != null) {
                    downvoteXML.getDocumentElement().normalize();
                    NodeList nList = downvoteXML.getElementsByTagName("post");

                    for (int x = 0; x < nList.getLength(); x++) {
                        Element Post = (Element) (nList.item(x));
                        downvotedPosts.add(Post.getAttribute("postID"));
                    }
                }

                VoteLists.downvotedPosts = downvotedPosts;
                myAdapter.setListener(new MyAdapter.OnItemClickListener() {
                    @Override
                    public void onItemSelected(int position, View view, ArrayList<Object> object) {
                        if (position == upvote_id) {

                            Log.d("Upvote List", VoteLists.upvotedPosts.toString());
                            Log.d("Downvote List", VoteLists.downvotedPosts.toString());
                            // include functionality for upvote button
                            String upvote_result = "";
                            try {
                                String postID = (String) object.get(0);
                                String userID = getIntent().getStringExtra("USERID");
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
                                VoteLists.upvotedPosts.add(object.toArray()[0].toString());
                            }
                        }
                        else if (position == downvote_id) {

                            Log.d("Upvote List", VoteLists.upvotedPosts.toString());
                            Log.d("Downvote List", VoteLists.downvotedPosts.toString());
                            // include downvote functionality
                            String downvote_result = "";
                            try {
                                String postID = (String) object.get(0);
                                String userID = getIntent().getStringExtra("USERID");
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
                                VoteLists.downvotedPosts.add(object.toArray()[0].toString());
                            }
                        } else if (position == user_pos) {
                            if (!(object.get(0).toString().equals(anonUser) || object.get(0).toString().equals(deletedUser))) {
                                setContentView(R.layout.activity_public_profile);
                                Intent intent = new Intent(getApplicationContext(), PublicProfilePage.class);
                                intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                                intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                                intent.putExtra("PUBLIC_USER", object.get(0).toString());
                                startActivity(intent);
                            }
                        }
                        else if (position == topic_pos) {
                            setContentView(R.layout.activity_topic_post);
                            Intent intent = new Intent(getApplicationContext(), TopicPostActivity.class);
                            intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                            intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                            intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                            intent.putExtra("TOPICID", object.get(0).toString());
                            startActivity(intent);
                        } else if (position == title_pos) {
                            Intent intent = new Intent(getApplicationContext(), ViewPostActivity.class);
                            intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                            intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                            intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                            intent.putExtra("POSTID", (String) object.get(0));
                            startActivity(intent);
                        }
                    }
                });
                myAdapter.notifyDataSetChanged();
            }
        });


        /**
         * BACK BUTTON
         */
        final Button backButton = findViewById(R.id.back_button_topic);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                startActivity(intent);
            }
        });




        followTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = null;
                try {
                    String userID = getIntent().getStringExtra("USERID");
                    String topicID = getIntent().getStringExtra("TOPICID");

                    result = (String) new FollowTopic(getApplicationContext()).execute(userID, currentTopicIndex).get(2000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (result.contains("TRUE")) {
                    Toast.makeText(getApplicationContext(), "Topic is no longer being followed", Toast.LENGTH_SHORT).show();
                }
                else if (result.contains("FALSE")) {
                    Toast.makeText(getApplicationContext(), "Topic is now being followed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Can not follow topic at this time", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }
}