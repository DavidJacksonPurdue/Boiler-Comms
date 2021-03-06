package com.example.boiler_commslogin.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boiler_commslogin.Constants;
import com.example.boiler_commslogin.R;
import com.example.boiler_commslogin.createpost.CreatePostActivity;
import com.example.boiler_commslogin.ui.login.EditUserProfile;
import com.example.boiler_commslogin.ui.login.LoginActivity;
import com.example.boiler_commslogin.viewpost.ViewPostActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class OtherTimeline extends AppCompatActivity {

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
    final int post_timeline_type = 0;
    final int topic_timeline_type = 1;
    final int upvote_timeline_type = 2;
    final int saved_timeline_type = 3;
    final int comment_timeline_type = 4;
    final String anonUser = "0";
    final String deletedUser = "-1";

    public class LoadUserCredentialsPost extends AsyncTask {
        //private TextView statusField,roleField;
        private Context context;
        String userCredentials;
        //flag 0 means get and 1 means post.(By default it is get.)
        public LoadUserCredentialsPost(Context context) {
            this.context = context;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            URL url = null;
            String userID = (String)objects[0];
            try {
                url = new URL(Constants.GETPOST + objects[0].toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                con.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            int status = 0;
            BufferedReader in = null;
            StringWriter content = new StringWriter();
            try {
                int n = 0;
                char[] buffer = new char[1];
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while (-1 != (n = in.read(buffer))) {
                    content.write(buffer,0,n);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            userCredentials = content.toString();
            return content.toString();
        }
        protected void onPostExecute(String result){
            userCredentials = result;
        }
    }

    public class LoadUserSavedPost extends AsyncTask {
        //private TextView statusField,roleField;
        private Context context;
        String userCredentials;
        //flag 0 means get and 1 means post.(By default it is get.)
        public LoadUserSavedPost(Context context) {
            this.context = context;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            URL url = null;
            String userID = (String)objects[0];
            try {
                url = new URL(Constants.GETSAVED + objects[0].toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                con.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            int status = 0;
            BufferedReader in = null;
            StringWriter content = new StringWriter();
            try {
                int n = 0;
                char[] buffer = new char[1];
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while (-1 != (n = in.read(buffer))) {
                    content.write(buffer,0,n);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            userCredentials = content.toString();
            return content.toString();
        }
        protected void onPostExecute(String result){
            userCredentials = result;
        }
    }

    public class LoadUserLikedPost extends AsyncTask {
        //private TextView statusField,roleField;
        private Context context;
        String userCredentials;
        //flag 0 means get and 1 means post.(By default it is get.)
        public LoadUserLikedPost(Context context) {
            this.context = context;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            URL url = null;
            String userID = (String)objects[0];
            try {
                url = new URL(Constants.GETLIKED + objects[0].toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                con.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            int status = 0;
            BufferedReader in = null;
            StringWriter content = new StringWriter();
            try {
                int n = 0;
                char[] buffer = new char[1];
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while (-1 != (n = in.read(buffer))) {
                    content.write(buffer,0,n);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            userCredentials = content.toString();
            return content.toString();
        }
        protected void onPostExecute(String result){
            userCredentials = result;
        }
    }
    //private void loadIntoRecyclerView(String json) throws JSONException {

    //JSONArray jsonArray = new JSONArray(json);

    //Log.d("json", jsonArray.toString());

    //for (int i = 0; i < jsonArray.length(); i++) {
    //    JSONObject obj = jsonArray.getJSONObject(i);
    //     username.add(obj.getString("userName"));
    //    topic.add(obj.getString("topicName"));
    //   title.add(obj.getString("postName"));
    //    time.add(obj.getString("postDate"));
    //   image.add(obj.getString("postImage"));
    //   body.add(obj.getString("postText"));
    //   votecount.add(obj.getString("voteTotal"));
    //   userID.add(obj.getString("userID"));
    //   topicID.add(obj.getString("topicID"));
    //   postID.add(obj.getString("postID"));
    // }


    //}


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
        setContentView(R.layout.activity_other_timeline);
        //setContentView(R.layout.activity_login);

        recyclerView = findViewById(R.id.recyclerView);
        final TextView timelineTitle = findViewById(R.id.timeline_title);
        final Button back_button = findViewById(R.id.go_home);

        String str_result = null;
        try {
            if (getIntent().getIntExtra("TIMELINE_TYPE", 0) == post_timeline_type) {
                String userID = getIntent().getStringExtra("PUBLIC_USER");
                str_result = (String) new LoadUserCredentialsPost(this).execute(userID).get(2000, TimeUnit.MILLISECONDS);
            }
            else if (getIntent().getIntExtra("TIMELINE_TYPE", 0) == saved_timeline_type) {
                String userID = getIntent().getStringExtra("PUBLIC_USER");
                str_result = (String) new LoadUserSavedPost(this).execute(userID).get(2000, TimeUnit.MILLISECONDS);
            }
            else if (getIntent().getIntExtra("TIMELINE_TYPE", 0) == upvote_timeline_type) {
                String userID = getIntent().getStringExtra("PUBLIC_USER");
                str_result = (String) new LoadUserLikedPost(this).execute(userID).get(2000, TimeUnit.MILLISECONDS);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        System.out.println(str_result);

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

        //try {
        //loadIntoRecyclerView(str_result);
        //} catch (JSONException e) {
        //e.printStackTrace();
        //}

        MyAdapter myAdapter = new MyAdapter(this, username, topic, title, body, image, time, votecount, postID, topicID, userID);
        final int upvote_id = 0;
        final int downvote_id = 1;
        final int user_pos = 2;
        final int title_pos = 3;
        final int topic_pos = 4;
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
                    }
                    else if (upvote_result.equals("upvoted")) {
                        Toast.makeText(getApplicationContext(), "You have already upvoted this post.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Successfully Upvoted Post", Toast.LENGTH_SHORT).show();
                        VoteLists.upvotedPosts.add(object.toArray()[0].toString());
                    }
                }
                else if (position == downvote_id) {
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
                    }
                    else if (downvote_result.equals("downvoted")) {
                        Toast.makeText(getApplicationContext(), "You have already downvoted this post.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Successfully Downvoted Post", Toast.LENGTH_SHORT).show();
                        VoteLists.downvotedPosts.add(object.toArray()[0].toString());
                    }
                }
                else if (position == user_pos) {
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
                else if (position == title_pos) {
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

        if (getIntent().getIntExtra("TIMELINE_TYPE", 0) == post_timeline_type) {
            if (username.size() > 0) {
                String title_string = username.get(0) + "'s Posts";
                timelineTitle.setText(title_string);
            }
            else {
                timelineTitle.setText("This user has no posts");
            }
        }
        else if (getIntent().getIntExtra("TIMELINE_TYPE", 0) == topic_timeline_type) {
            if (topic.size() > 0) {
                String title_string = topic.get(0);
                timelineTitle.setText(title_string);
            }
            else {
                timelineTitle.setText("There are no posts with this topic");
            }
        }
        else if (getIntent().getIntExtra("TIMELINE_TYPE", 0) == upvote_timeline_type) {
            if (username.size() > 0) {
                String title_string = "Upvoted Posts";
                timelineTitle.setText(title_string);
            }
            else {
                timelineTitle.setText("This user has not upvoted any posts");
            }
        }
        else if (getIntent().getIntExtra("TIMELINE_TYPE", 0) == saved_timeline_type) {
            timelineTitle.setText("Your Saved Posts");
        }
        else if (getIntent().getIntExtra("TIMELINE_TYPE", 0) == comment_timeline_type) {
            if (username.size() > 0) {
                String title_string = username.get(0) + "'s Comments";
                timelineTitle.setText(title_string);
            }
            else {
                timelineTitle.setText("This user has no comments");
            }
        }

        back_button.setOnClickListener(new View.OnClickListener() {
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

        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }
}