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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boiler_commslogin.Constants;
import com.example.boiler_commslogin.R;
import com.example.boiler_commslogin.createpost.CreatePostActivity;
import com.example.boiler_commslogin.ui.login.EditUserProfile;
import com.example.boiler_commslogin.ui.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

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

    public class LoadUserCredentialsPost extends AsyncTask {
        //private TextView statusField,roleField;
        private Context context;
        String userCredentials;
        //flag 0 means get and 1 means post.(By default it is get.)
        public LoadUserCredentialsPost(Context context) {
            this.context = context;
        }
        public String getUserCredentials(){
            return userCredentials;
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
            StringBuilder content = new StringBuilder();
            try {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                while (true) {
                    inputLine = in.readLine();
                    if(inputLine == null){
                        break;
                    }
                    content.append(inputLine);
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
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.activity_login);

        recyclerView = findViewById(R.id.recyclerView);
        String str_result = null;
        try {
            String userID = getIntent().getStringExtra("USERID");
            str_result = (String) new LoadUserCredentialsPost(this).execute(userID).get(2000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

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
                    else {
                        Toast.makeText(getApplicationContext(), "Successfully Upvoted Post", Toast.LENGTH_SHORT).show();
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
                    else {
                        Toast.makeText(getApplicationContext(), "Successfully Downvoted Post", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (position == user_pos) {
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

        final Button settings = findViewById(R.id.settings);
        final Button logout = findViewById(R.id.logout);
        final Button createPost = findViewById(R.id.createPost);
        final Button profileButton = findViewById(R.id.view_profile);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_editprofile);
                Intent intent = new Intent(getApplicationContext(), EditUserProfile.class);
                intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                startActivity(intent);
            }
        });

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_createpost);
                Intent intent = new Intent(getApplicationContext(), CreatePostActivity.class);
                intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                startActivity(intent);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_public_profile);
                Intent intent = new Intent(getApplicationContext(), PublicProfilePage.class);
                intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                intent.putExtra("PUBLIC_USER", getIntent().getStringExtra("USERID"));
                startActivity(intent);
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("Logout?");
        builder.setMessage("This will bring you back to the login page.");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setContentView(R.layout.activity_login);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Canceled Logout", Toast.LENGTH_LONG).show();
            }
        });
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(-1).setVisibility(View.VISIBLE);
                dialog.getButton(-2).setVisibility(View.VISIBLE);
            }
        });
    }
}