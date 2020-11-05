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
import com.example.boiler_commslogin.sign_up.verifyUser;
import com.example.boiler_commslogin.ui.login.EditUserProfile;
import com.example.boiler_commslogin.ui.login.LoginViewModel;
import com.example.boiler_commslogin.ui.login.LoginViewModelFactory;

import org.json.JSONException;
import org.json.JSONObject;

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
                try {
                    url = new URL(Constants.FOLLOW_USER + objects[0].toString() + "_" + objects[1].toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
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
                status = 0;
                in = null;
                try {
                    status = con.getResponseCode();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                content = new StringBuilder();
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
                        content.delete(0, content.length());
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
                    return "PASS";
                }
                return "FAIL";
            }
            else {
                return "PASSFAIL";
            }
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
                else if (follow_result.equals("PASS")) {
                    Toast.makeText(getApplicationContext(), "Followed User", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "You're Already Following this User", Toast.LENGTH_SHORT).show();
                }
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
                Intent intent = new Intent(getApplicationContext(), viewMyUserComments.class);
                intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                intent.putExtra("PUBLIC_USER", getIntent().getStringExtra("PUBLIC_USER"));
                intent.putExtra("TIMELINE_TYPE", post_timeline_type);
                startActivity(intent);
            }
        });
    }
}