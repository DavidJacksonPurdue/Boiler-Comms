package com.example.boiler_commslogin.directMessage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boiler_commslogin.R;
import com.example.boiler_commslogin.comment.viewComments;
import com.example.boiler_commslogin.viewpost.ViewPostActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class viewDM extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText currentMessage;
    Button sendButton;
    String uid1;
    String dm_id;
    String uid2;
    specificDMAdapter mAdapter;
    ArrayList<specificDM> specificDMArray;
    String UserUsername;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dm_user);
        recyclerView = findViewById(R.id.recyclerview_chat_log);
        currentMessage = findViewById(R.id.edittext_chat_log);
        sendButton = findViewById(R.id.send_button_chat_log);
        uid1 = getIntent().getStringExtra("USERID1");
        dm_id = getIntent().getStringExtra("DM_ID");
        uid2 = getIntent().getStringExtra("USERID2");
        UserUsername = getIntent().getStringExtra("USERNAME");

        mContext = this;

        specificDMArray = new ArrayList<>();
        Log.d("Made it here", "0");
        if(!(dm_id.equals(""))) {
            String str_result = null;
            try {
                str_result = (String) new loadSpecificDM(this).execute(dm_id).get(2000, TimeUnit.MILLISECONDS);

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
            } catch (Exception e) {
                e.printStackTrace();
            }
            postXML.getDocumentElement().normalize();
            NodeList nList = postXML.getElementsByTagName("dm_message");

            String username;
            String body;
            String time;
            String userID;
            for (int x = 0; x < nList.getLength(); x++) {
                Element commentElement = (Element) (nList.item(x));

                username = commentElement.getAttribute("username");
                body = commentElement.getAttribute("body");
                time = commentElement.getAttribute("time");
                userID = commentElement.getAttribute("userID");
                specificDM tempDM = null;
                if (userID.equals(uid1)) {
                    tempDM = new specificDM(username, body, time, 1);
                } else {
                    tempDM = new specificDM(username, body, time, 0);
                }
                specificDMArray.add(tempDM);
            }
        }
        mAdapter = new specificDMAdapter(specificDMArray, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sendButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String tempMsg = currentMessage.getText().toString();
                if(!tempMsg.equals("")){
                    String str_result0 = "";
                    String str_result1 = "";
                    String followCase = "0";
                    String followCase2 = "0";
                    try {
                        str_result0 = (String)new checkIfFollowCase(getApplicationContext()).execute(getIntent().getStringExtra("USERID2")).get(2000, TimeUnit.MILLISECONDS);;
                        str_result1= (String)new checkIfFollowCase(getApplicationContext()).execute(getIntent().getStringExtra("USERID1")).get(2000, TimeUnit.MILLISECONDS);;
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
                        str_result2= (String)new checkIfCanDM(getApplicationContext()).execute(followCase, getIntent().getStringExtra("USERID1"),getIntent().getStringExtra("USERID2")).get(2000, TimeUnit.MILLISECONDS);;
                        str_result3 = (String)new checkIfCanDM(getApplicationContext()).execute(followCase2, getIntent().getStringExtra("USERID2"),getIntent().getStringExtra("USERID1")).get(2000, TimeUnit.MILLISECONDS);;
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
                        String str_result = null;
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        String msgTime = dtf.format(now);
                        try {
                            str_result= (String)new sendDM(getApplicationContext()).execute(dm_id, msgTime, uid1, uid2, tempMsg).get(2000, TimeUnit.MILLISECONDS);
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        postXML.getDocumentElement().normalize();
                        NodeList nList = postXML.getElementsByTagName("dm");
                        Element commentElement = (Element) (nList.item(0));

                        dm_id = commentElement.getAttribute("id");
                        currentMessage.setText("");
                        specificDM tempDM = new specificDM(UserUsername, tempMsg, msgTime, 1);
                        specificDMArray.add(tempDM);
                        mAdapter.notifyItemInserted(specificDMArray.size() - 1);
                        hideKeyboard((Activity) mContext);
                        Toast.makeText(getApplicationContext(), "message sent", Toast.LENGTH_SHORT).show();
                    }

                } else{
                    Toast.makeText(getApplicationContext(), "message must not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button backButton = findViewById(R.id.backToDMs);
        Button refreshButton = findViewById(R.id.refreshButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_view_all_dms);
                Intent intent = new Intent(getApplicationContext(), viewAllDMs.class);
                intent.putExtra("USERID", uid1);
                intent.putExtra("USERNAME", UserUsername);
                intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                //Log.d("ViewPost PostID", "" + postID);
                startActivity(intent);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_dm_user);
                Intent intent = new Intent(getApplicationContext(), viewDM.class);
                intent.putExtra("USERID1", uid1);
                intent.putExtra("USERID2", uid2);
                intent.putExtra("DM_ID", dm_id);
                intent.putExtra("USERNAME", UserUsername);
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
