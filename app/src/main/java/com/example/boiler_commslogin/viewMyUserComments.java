package com.example.boiler_commslogin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.multilevelview.MultiLevelRecyclerView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.Boolean.FALSE;

public class viewMyUserComments extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users_comments);
        recyclerView = findViewById(R.id.userCommentsRecyclerView);

        String str_result = null;
        try {
            str_result= (String)new loadMyComments(this).execute("0").get(2000, TimeUnit.MILLISECONDS);;

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


    }

}
