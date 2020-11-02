package com.example.boiler_commslogin.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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


    public static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_post);


        // Find and store the recycler view
        recyclerView = findViewById(R.id.topicRecyclerView);
        String str_result = null;
        // Temporary topic ID

        // Retrive all of the posts from the database with the associated topic id
        String topicId = "1";
        try {
            str_result = (String) new getPostsByTopicModel(this).execute(topicId).get(2000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


        // Try and load those posts into the recycler view
        Document postXML = null;
        try {
            postXML = loadXMLFromString(str_result);
        } catch (Exception e) {
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
        final MyAdapter myAdapter = new MyAdapter(this, username, topic, title, body, image, time, votecount, postID, topicID, userID);
        final int upvote_id = 0;
        final int downvote_id = 1;
        final int user_pos = 2;
        myAdapter.setListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemSelected(int position, View view, ArrayList<Object> object) {
                if (position == upvote_id) {
                    // include functionality for upvote button
                    String upvote_result = "";
                    try {
                        upvote_result = (String) new UpvoteTask(getApplicationContext()).execute(object.toArray()).get(2000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (upvote_result.equals("")) {
                        Toast.makeText(getApplicationContext(), "Failed To Upvote Post At This Time", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Successfully Upvoted Post", Toast.LENGTH_SHORT).show();
                    }
                } else if (position == downvote_id) {
                    // include downvote functionality
                    String downvote_result = "";
                    try {
                        downvote_result = (String) new DownvoteTask(getApplicationContext()).execute(object.toArray()).get(2000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (downvote_result.equals("")) {
                        Toast.makeText(getApplicationContext(), "Failed To Downvote Post At This Time", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Successfully Downvoted Post", Toast.LENGTH_SHORT).show();
                    }
                } else if (position == user_pos) {
                    setContentView(R.layout.activity_public_profile);
                    Intent intent = new Intent(getApplicationContext(), PublicProfilePage.class);
                    intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                    intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                    intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                    intent.putExtra("PUBLIC_USER", object.get(0).toString());
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


        /***
         * CREATE CODE FOR AUTOFILL LISTENER
         * OOF
         */
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.topicSearch);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, topicNames);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String pos;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the topic Id from the selected dropdown list
                for (int i = 0; i < topicNames.size(); i++) {
                    if (parent.getItemAtPosition(position).equals(topicNames.get(i))) {
                        System.out.println(topicNames.get(i));
                        ;
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
                try {
                    str_result = (String) new getPostsByTopicModel(context).execute(pos).get(2000, TimeUnit.MILLISECONDS);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }

                // Try and load those posts into the recycler view
                Document postXML = null;
                try {
                    postXML = loadXMLFromString(str_result);
                } catch (Exception e) {
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
                myAdapter.setListener(new MyAdapter.OnItemClickListener() {
                    @Override
                    public void onItemSelected(int position, View view, ArrayList<Object> object) {
                        if (position == upvote_id) {
                            // include functionality for upvote button
                            String upvote_result = "";
                            try {
                                upvote_result = (String) new UpvoteTask(getApplicationContext()).execute(object.toArray()).get(2000, TimeUnit.MILLISECONDS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            if (upvote_result.equals("")) {
                                Toast.makeText(getApplicationContext(), "Failed To Upvote Post At This Time", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Successfully Upvoted Post", Toast.LENGTH_SHORT).show();
                            }
                        } else if (position == downvote_id) {
                            // include downvote functionality
                            String downvote_result = "";
                            try {
                                downvote_result = (String) new DownvoteTask(getApplicationContext()).execute(object.toArray()).get(2000, TimeUnit.MILLISECONDS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            if (downvote_result.equals("")) {
                                Toast.makeText(getApplicationContext(), "Failed To Downvote Post At This Time", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Successfully Downvoted Post", Toast.LENGTH_SHORT).show();
                            }
                        } else if (position == user_pos) {
                            setContentView(R.layout.activity_public_profile);
                            Intent intent = new Intent(getApplicationContext(), PublicProfilePage.class);
                            intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                            intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                            intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                            intent.putExtra("PUBLIC_USER", object.get(0).toString());
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


    }
}