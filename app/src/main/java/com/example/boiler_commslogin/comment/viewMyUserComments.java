package com.example.boiler_commslogin.comment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boiler_commslogin.R;
import com.example.boiler_commslogin.data.MainActivity;
import com.example.boiler_commslogin.data.PublicProfilePage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class viewMyUserComments extends AppCompatActivity {
    RecyclerView recyclerView;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users_comments);
        recyclerView = findViewById(R.id.userCommentsRecyclerView);
        back = findViewById(R.id.CommentsBackButton);
        String str_result = null;
        try {
            str_result= (String)new loadMyComments(this).execute(getIntent().getStringExtra("USERID")).get(2000, TimeUnit.MILLISECONDS);;

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        Document postXML = null;
        try {
            postXML = viewComments.loadXMLFromString(str_result);
        }catch(Exception e){
            e.printStackTrace();
        }
        postXML.getDocumentElement().normalize();
        NodeList nList = postXML.getElementsByTagName("comment");
        ArrayList<String> commentIDs = new ArrayList<>();
        ArrayList<String> postIDs = new ArrayList<>();
        ArrayList<String> parentCommentIDs = new ArrayList<>();
        ArrayList<String> bodys = new ArrayList<>();
        ArrayList<String> userNames = new ArrayList<>();
        ArrayList<String> userIDs = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> topics = new ArrayList<>();
        for(int x = 0; x <nList.getLength(); x++){
            Element commentElement = (Element)(nList.item(x));
            commentIDs.add(commentElement.getAttribute("commentID"));
            postIDs.add(commentElement.getAttribute("postID"));
            if(!(commentElement.getAttribute("parentCommentID").equals(""))) {
                parentCommentIDs.add(commentElement.getAttribute("parentCommentID"));
            }else{
                parentCommentIDs.add("-1");
            }
            bodys.add(commentElement.getAttribute("commentText"));
            userNames.add(commentElement.getAttribute("userName"));
            userIDs.add(commentElement.getAttribute("userID"));
            dates.add(commentElement.getAttribute("dateSubmitted"));
            titles.add(commentElement.getAttribute("postName"));
            topics.add(commentElement.getAttribute("topicName"));
        }
        MyUserCommentsAdapter myAdapter = new MyUserCommentsAdapter(this, commentIDs, postIDs, parentCommentIDs, bodys, userNames, userIDs, dates, titles, topics);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_public_profile);
                Intent intent = new Intent(getApplicationContext(), PublicProfilePage.class);
                intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                intent.putExtra("PUBLIC_USER", getIntent().getStringExtra("PUBLIC_USER"));
                startActivity(intent);
            }
        });
    }

}
