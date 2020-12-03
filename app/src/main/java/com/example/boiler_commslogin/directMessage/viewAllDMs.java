package com.example.boiler_commslogin.directMessage;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.boiler_commslogin.R;
import com.example.boiler_commslogin.comment.MyUserCommentsAdapter;
import com.example.boiler_commslogin.comment.loadMyComments;
import com.example.boiler_commslogin.comment.viewComments;
import com.example.boiler_commslogin.data.MainActivity;
import com.example.boiler_commslogin.data.PublicProfilePage;
//import com.example.boiler_commslogin.comment.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class viewAllDMs extends AppCompatActivity {

    RecyclerView recyclerView;
    String UID;
    String UserName;
    //Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_dms);
        recyclerView = findViewById(R.id.recyclerview_newmessage);
        //back = findViewById(R.id.CommentsBackButton);
        String str_result = null;
        //UID = "1";
        UID = getIntent().getStringExtra("USERID");
        UserName = getIntent().getStringExtra("UserName");
        //UserName = "User2";
        try {
            str_result= (String)new loadAllDMs(this).execute(UID).get(2000, TimeUnit.MILLISECONDS);;

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
        NodeList nList = postXML.getElementsByTagName("dm");
        ArrayList<String> dm_IDs = new ArrayList<>();
        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<String> bodys = new ArrayList<>();
        ArrayList<String> times = new ArrayList<>();
        ArrayList<String> otherUIDs = new ArrayList<>();
        String tempUID0;
        String tempUID1;
        String tempUID2;
        for(int x = 0; x <nList.getLength(); x++){
            Element commentElement = (Element)(nList.item(x));
            dm_IDs.add(commentElement.getAttribute("dm_id"));
            usernames.add(commentElement.getAttribute("username"));
            bodys.add(commentElement.getAttribute("body"));
            times.add(commentElement.getAttribute("time"));
            tempUID1 = commentElement.getAttribute("uid1");
            tempUID2 = commentElement.getAttribute("uid2");
            if(UID.equals(tempUID1)){
                otherUIDs.add(tempUID2);
            }else{
                otherUIDs.add(tempUID1);
            }
        }
        allDMAdapter myAdapter = new allDMAdapter(this, dm_IDs, usernames, bodys, times, otherUIDs, UID, UserName);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button backButton = findViewById(R.id.leaveDMs);
        Button refreshButton = findViewById(R.id.refresh2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("USERID", UID);
                intent.putExtra("USERNAME", UserName);
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                startActivity(intent);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_view_all_dms);
                Intent intent = new Intent(getApplicationContext(), viewAllDMs.class);
                intent.putExtra("USERID", UID);
                intent.putExtra("USERNAME", UserName);
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                //Log.d("ViewPost PostID", "" + postID);
                startActivity(intent);
            }
        });
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }


    public static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

}
