package com.example.boiler_commslogin.data;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boiler_commslogin.Constants;
import com.example.boiler_commslogin.R;
import com.example.boiler_commslogin.comment.viewComments;
import com.example.boiler_commslogin.comment.viewMyUserComments;
import com.example.boiler_commslogin.data.MainActivity;
import com.example.boiler_commslogin.data.PasswordHasher;
import com.example.boiler_commslogin.data.model.LoadUserCredentials;
import com.example.boiler_commslogin.data.model.RequestHandler;
import com.example.boiler_commslogin.data.model.SendUserCredentials;
import com.example.boiler_commslogin.delete_account.deleteUser;
import com.example.boiler_commslogin.directMessage.checkIfCanDM;
import com.example.boiler_commslogin.directMessage.checkIfFollowCase;
import com.example.boiler_commslogin.directMessage.loadAllDMs;
import com.example.boiler_commslogin.directMessage.viewAllDMs;
import com.example.boiler_commslogin.directMessage.viewDM;
import com.example.boiler_commslogin.sign_up.verifyUser;
import com.example.boiler_commslogin.ui.login.EditUserProfile;
import com.example.boiler_commslogin.ui.login.LoginViewModel;
import com.example.boiler_commslogin.ui.login.LoginViewModelFactory;
import com.example.boiler_commslogin.viewOtherUserComments;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class PublicProfilePage extends AppCompatActivity {
    public class CheckValidFollow extends AsyncTask {
        //private TextView statusField,roleField;
        private Context context;
        String userCredentials;

        //flag 0 means get and 1 means post.(By default it is get.)
        public CheckValidFollow(Context context) {
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
                url = new URL(Constants.CHECK_FOLLOW + objects[0].toString() + "_" + objects[1].toString());
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
            try {
                status = con.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            StringBuilder content = new StringBuilder();
            if (status <= 299) {
                try {
                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    while (true) {
                        inputLine = in.readLine();
                        if (inputLine == null) {
                            break;
                        }
                        content.append(inputLine);
                    }
                    con.disconnect();
                } catch (IOException e) {
                    con.disconnect();
                    content.delete(0,content.length());
                    content.append("ERROR");
                    e.printStackTrace();
                }
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (content.toString().equals("FALSE")) {
                    return "INS";
            }
            else if (content.toString().equals("TRUE")) {
                return "DEL";
            }
            else {
                return "FAIL";
            }
        }
    }

    public class BlockAndUnblock extends AsyncTask {
        private Context context;
        String userCredentials;

        public BlockAndUnblock(Context context) {
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
                url = new URL(Constants.BLOCK_USER + objects[0].toString() + "_" + objects[1].toString());
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
            try {
                status = con.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            StringBuilder content = new StringBuilder();
            if (status <= 299) {
                try {
                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    while (true) {
                        inputLine = in.readLine();
                        if (inputLine == null) {
                            break;
                        }
                        content.append(inputLine);
                    }
                    con.disconnect();
                } catch (IOException e) {
                    con.disconnect();
                    content.delete(0,content.length());
                    content.append("ERROR");
                    e.printStackTrace();
                }
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (content.toString().equals("FALSE")) {
                return "INS";
            }
            else if (content.toString().equals("TRUE")) {
                return "DEL";
            }
            else {
                return "FAIL";
            }
        }
    }

    public class UnfollowWhenBlock extends AsyncTask {
        private Context context;
        String userCredentials;

        public UnfollowWhenBlock(Context context) {
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
                url = new URL(Constants.DELETE_FOLLOW + objects[0].toString() + "_" + objects[1].toString());
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
            try {
                status = con.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            StringBuilder content = new StringBuilder();
            if (status <= 299) {
                try {
                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    while (true) {
                        inputLine = in.readLine();
                        if (inputLine == null) {
                            break;
                        }
                        content.append(inputLine);
                    }
                    con.disconnect();
                } catch (IOException e) {
                    con.disconnect();
                    content.delete(0,content.length());
                    content.append("ERROR");
                    e.printStackTrace();
                }
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return content.toString();
        }
    }


    public static final String UPLOAD_KEY = "image";

    private int PICK_IMAGE_REQUEST = 1;

    private Button buttonChoose;
    private Button buttonUpload;

    private ImageView imageView;
    private Bitmap bitmap;
    private Uri filePath;




    public static final int PICK_IMAGE = 100;
    Uri imageURI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile);
        final TextView profileUsername = findViewById(R.id.user);
        final TextView profileFirstName = findViewById(R.id.first_name);
        final TextView profileLastName = findViewById(R.id.last_name);
        final ImageView profilePic = findViewById(R.id.profile_pic);
        final Button backButton = findViewById(R.id.back_button);
        final Button viewPosts = findViewById(R.id.view_posts);
        final Button likedPosts = findViewById(R.id.view_likes);
        final Button comments = findViewById(R.id.view_comments);
        final Button savedPosts = findViewById(R.id.saved_posts);
        final Button followButton = findViewById(R.id.follow_button);
        final Button blockButton = findViewById(R.id.block_button);
        final ImageButton dmButton = findViewById(R.id.message_button);
        final Button viewFollowedUsers = findViewById(R.id.user_list);
        final Button viewFollowedTopics = findViewById(R.id.topic_list);
        final Button viewBlockedUsers = findViewById(R.id.block_list);
        final Button allDMButton = findViewById(R.id.AllDMButton);
        final int post_timeline_type = 0;
        final int upvote_timeline_type = 2;
        final int saved_timeline_type = 3;
        final int comment_timeline_type = 4;

        //Download public profile from user
        String str_result = null;

        //if (user != null && !user.equals("-1")) {
        try {
            str_result = (String) new LoadUserCredentials(this).execute(getIntent().getStringExtra("PUBLIC_USER")).get(2000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        //}
        //else {
            //Toast.makeText(getApplicationContext(), "Failed To Load Profile", Toast.LENGTH_LONG).show();
            //setContentView(R.layout.activity_main);
            //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            //intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
            //intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
            //intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
            //startActivity(intent);
        //}
        String userName = null;
        String firstName = null;
        String lastName = null;
        String img = null;


        try {
            JSONObject reader = new JSONObject(str_result);
            userName = reader.getString("userName");
            firstName = reader.getString("firstName");
            lastName = reader.getString("lastName");
            img = reader.getString("profilePic");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        profileUsername.setText(userName);
        profileFirstName.setText(firstName);
        profileLastName.setText(lastName);

        if (getIntent().getStringExtra("PUBLIC_USER").equals(getIntent().getStringExtra("USERID"))) {
            savedPosts.setVisibility(View.VISIBLE);
            savedPosts.setClickable(true);
            followButton.setVisibility(View.INVISIBLE);
            savedPosts.setClickable(false);
            blockButton.setVisibility(View.INVISIBLE);
            blockButton.setClickable(false);
            dmButton.setVisibility(View.INVISIBLE);
            dmButton.setClickable(false);
            viewBlockedUsers.setVisibility(View.VISIBLE);
            viewBlockedUsers.setClickable(true);
            viewFollowedTopics.setVisibility(View.VISIBLE);
            viewFollowedTopics.setClickable(true);
            viewFollowedUsers.setVisibility(View.VISIBLE);
            viewFollowedUsers.setClickable(true);
            allDMButton.setVisibility(View.VISIBLE);
            allDMButton.setClickable(true);
        }

        if (img != null) {
            if (!img.equals("null")) {
                String base64Image = img.split(",")[1];
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                profilePic.setImageBitmap(decodedByte);
            }
        }

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object[] objects = new Object[2];
                objects[0] = getIntent().getStringExtra("PUBLIC_USER");
                objects[1] = getIntent().getStringExtra("USERID");
                String follow_result = "";
                try {
                    follow_result = (String) new CheckValidFollow(getApplicationContext()).execute(objects).get(2000, TimeUnit.MILLISECONDS);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                if (follow_result.equals("FAIL")) {
                    Toast.makeText(getApplicationContext(), "Failed to follow user at this time", Toast.LENGTH_SHORT).show();
                }
                else if (follow_result.equals("INS")) {
                    Toast.makeText(getApplicationContext(), "Followed User", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "You're No Longer Following this User", Toast.LENGTH_SHORT).show();
                }
            }
        });

        blockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object[] objects = new Object[2];
                objects[0] = getIntent().getStringExtra("USERID");
                objects[1] = getIntent().getStringExtra("PUBLIC_USER");
                String block_result = "";
                try {
                    block_result = (String) new BlockAndUnblock(getApplicationContext()).execute(objects).get(3000, TimeUnit.MILLISECONDS);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                if (block_result.equals("FAIL")) {
                    Toast.makeText(getApplicationContext(), "Failed to block user at this time", Toast.LENGTH_SHORT).show();
                }
                else if (block_result.equals("INS")) {
                    try {
                        Object temp = objects[0];
                        objects[0] = objects[1];
                        objects[1] = temp;
                        String unfollow_result =  (String) new UnfollowWhenBlock(getApplicationContext()).execute(objects).get(3000, TimeUnit.MILLISECONDS);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "Blocked User", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "You're No Longer Blocking this User", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_result0 = "";
                String str_result1 = "";
                String followCase = "dick";
                String followCase2 = "dick";
                try {
                    str_result0 = (String)new checkIfFollowCase(getApplicationContext()).execute(getIntent().getStringExtra("PUBLIC_USER")).get(2000, TimeUnit.MILLISECONDS);;
                    str_result1= (String)new checkIfFollowCase(getApplicationContext()).execute(getIntent().getStringExtra("USERID")).get(2000, TimeUnit.MILLISECONDS);;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                Document postXML1 = null;
                Document postXML0 = null;
                try {
                    postXML1 = viewComments.loadXMLFromString(str_result1);
                    postXML0 = viewComments.loadXMLFromString(str_result0);
                }catch(Exception e){
                    e.printStackTrace();
                }
                postXML1.getDocumentElement().normalize();
                postXML0.getDocumentElement().normalize();
                NodeList nList1 = postXML1.getElementsByTagName("block");
                NodeList nList0 = postXML0.getElementsByTagName("block");
                Element commentElement1 = (Element)(nList1.item(0));
                Element commentElement0 = (Element)(nList0.item(0));
                Log.d("c1", commentElement1.getAttribute("is_blocked"));
                Log.d("c0", commentElement0.getAttribute("is_blocked"));
                if(commentElement1.getAttribute("is_blocked").equals("true")){
                    followCase = "1";
                }else{
                    followCase = "0";
                }
                if(commentElement0.getAttribute("is_blocked").equals("true")){
                    followCase2 = "1";
                }else{
                    followCase2 = "0";
                }
                Log.d("follow1", followCase);
                Log.d("follow2", followCase2);

                String str_result2 = "";
                String str_result3 = "";
                try {
                    str_result2= (String)new checkIfCanDM(getApplicationContext()).execute(followCase, getIntent().getStringExtra("USERID"),getIntent().getStringExtra("PUBLIC_USER")).get(2000, TimeUnit.MILLISECONDS);;
                    str_result3 = (String)new checkIfCanDM(getApplicationContext()).execute(followCase2, getIntent().getStringExtra("PUBLIC_USER"),getIntent().getStringExtra("USERID")).get(2000, TimeUnit.MILLISECONDS);;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                Document postXML2 = null;
                Document postXML3 = null;
                try {
                    postXML2 = viewComments.loadXMLFromString(str_result2);
                    postXML3 = viewComments.loadXMLFromString(str_result3);
                }catch(Exception e){
                    e.printStackTrace();
                }
                postXML2.getDocumentElement().normalize();
                postXML3.getDocumentElement().normalize();
                NodeList nList2 = postXML2.getElementsByTagName("block");
                NodeList nList3 = postXML3.getElementsByTagName("block");
                Element commentElement2 = (Element)(nList2.item(0));
                Element commentElement3 = (Element)(nList3.item(0));
                Log.d("Us to them", commentElement2.getAttribute("is_blocked"));
                Log.d("them to us", commentElement3.getAttribute("is_blocked"));
                if(commentElement2.getAttribute("is_blocked").equals("true") || commentElement3.getAttribute("is_blocked").equals("true")){
                    Toast.makeText(getApplicationContext(), "You can not DM this user", Toast.LENGTH_LONG).show();
                } else{
                    //Open DM page goes here
                    setContentView(R.layout.activity_dm_user);
                    Intent intent = new Intent(getApplicationContext(), viewDM.class);
                    intent.putExtra("USERID1", getIntent().getStringExtra("USERID"));
                    intent.putExtra("USERID2", getIntent().getStringExtra("PUBLIC_USER"));
                    intent.putExtra("DM_ID", "");
                    intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                    intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                    //Log.d("ViewPost PostID", "" + postID);
                    startActivity(intent);
                }
            }
        });

        viewBlockedUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_view_list);
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                intent.putExtra("LIST_TYPE", 2);
                startActivity(intent);
            }
        });

        viewFollowedTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_view_list);
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                intent.putExtra("LIST_TYPE", 1);
                startActivity(intent);
            }
        });

        viewFollowedUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_view_list);
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                intent.putExtra("LIST_TYPE", 0);
                startActivity(intent);
            }
        });

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

        savedPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_other_timeline);
                Intent intent = new Intent(getApplicationContext(), OtherTimeline.class);
                intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                intent.putExtra("PUBLIC_USER", getIntent().getStringExtra("PUBLIC_USER"));
                intent.putExtra("TIMELINE_TYPE", saved_timeline_type);
                startActivity(intent);
            }
        });

        likedPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_other_timeline);
                Intent intent = new Intent(getApplicationContext(), OtherTimeline.class);
                intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                intent.putExtra("PUBLIC_USER", getIntent().getStringExtra("PUBLIC_USER"));
                intent.putExtra("TIMELINE_TYPE", upvote_timeline_type);
                startActivity(intent);
            }
        });

        viewPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_other_timeline);
                Intent intent = new Intent(getApplicationContext(), OtherTimeline.class);
                intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                intent.putExtra("PUBLIC_USER", getIntent().getStringExtra("PUBLIC_USER"));
                intent.putExtra("TIMELINE_TYPE", post_timeline_type);
                startActivity(intent);
            }
        });
        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_view_comments);
                Intent intent = new Intent(getApplicationContext(), viewOtherUserComments.class);
                intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                intent.putExtra("PUBLIC_USER", getIntent().getStringExtra("PUBLIC_USER"));
                intent.putExtra("TIMELINE_TYPE", post_timeline_type);
                startActivity(intent);
            }
        });
        allDMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_view_all_dms);
                Intent intent = new Intent(getApplicationContext(), viewAllDMs.class);
                intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                intent.putExtra("USERNAME", getIntent().getStringExtra("UserName"));
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                //Log.d("ViewPost PostID", "" + postID);
                startActivity(intent);
            }
        });
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }
}