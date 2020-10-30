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
import android.widget.Button;

import com.example.boiler_commslogin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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


    /**
     * A function for parsing a JSON string and loading
     * its contents into a recycler view
     * @param json the string in json format
     * @throws JSONException
     */
    private void loadIntoRecyclerView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            username.add(obj.getString("userID"));
            topic.add(obj.getString("topicName"));
            title.add(obj.getString("postName"));
            time.add(obj.getString("postDate"));
            image.add(obj.getString("postImage"));
            body.add(obj.getString("postText"));
        }
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
        }catch (TimeoutException e) {
            e.printStackTrace();
        }

        // Try and load those posts into the recycler view
        try {
            loadIntoRecyclerView(str_result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Store them in an adapter
        final MyAdapter myAdapter = new MyAdapter(this, username, topic, title, body, image, time, votecount, postID, topicID, userID);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final Button clearPost = findViewById(R.id.clearPost);

        clearPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.clear();
                topic.clear();
                title.clear();
                body.clear();
                image.clear();
                time.clear();

                String str_result = null;
                // Temporary topic ID

                // Retrive all of the posts from the database with the associated topic id
                String topicId = "2";
                try {
                    str_result = (String) new getPostsByTopicModel(context).execute(topicId).get(2000, TimeUnit.MILLISECONDS);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }catch (TimeoutException e) {
                    e.printStackTrace();
                }

                try {
                    loadIntoRecyclerView(str_result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                myAdapter.notifyDataSetChanged();

            }
        });

    }
}