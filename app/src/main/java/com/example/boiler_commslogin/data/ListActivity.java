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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
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

public class ListActivity extends AppCompatActivity {

    RecyclerView recyclerViewList;
    ArrayList<String> itemname = new ArrayList<>();
    ArrayList<String> itemID = new ArrayList<>();

    public class UnfollowOrUnblock extends AsyncTask {
        private Context context;
        public UnfollowOrUnblock(Context context) {
            this.context = context;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            URL url = null;
            try {
                if (objects[2].toString().equals("0")) {
                    url = new URL(Constants.CHECK_FOLLOW + objects[0].toString() + "_" + objects[1].toString());
                }
                else if (objects[2].toString().equals("1")) {
                    url = new URL(Constants.GETFOLLOWTOPIC + objects[1].toString() + "_" + objects[0].toString());
                }
                else if (objects[2].toString().equals("2")) {
                    url = new URL(Constants.BLOCK_USER + objects[1].toString() + "_" + objects[0].toString());
                }
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
            if (content.toString().equals("TRUE")) {
                return "DEL";
            }
            else {
                return "FAIL";
            }
        }
    }

    public class LoadList extends AsyncTask {
        //private TextView statusField,roleField;
        private Context context;

        //flag 0 means get and 1 means post.(By default it is get.)
        public LoadList(Context context) {
            this.context = context;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            URL url = null;
            try {
                if (objects[1].toString().equals("0")) {
                    url = new URL(Constants.FOLLOW_LIST + objects[0].toString());
                }
                else if (objects[1].toString().equals("1")) {
                    url = new URL(Constants.TOPIC_LIST + objects[0].toString());
                }
                else if (objects[1].toString().equals("2")) {
                    url = new URL(Constants.BLOCK_LIST + objects[0].toString());
                }
                else {
                    return "How did you even get here?";
                }
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
            BufferedReader in = null;
            StringWriter content = new StringWriter();
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
            return content.toString();
        }
    }

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
        setContentView(R.layout.activity_view_list);
        final TextView listTitle = findViewById(R.id.list_title);
        //setContentView(R.layout.activity_login);

        final int list_type = getIntent().getIntExtra("LIST_TYPE", 0);

        recyclerViewList = findViewById(R.id.recyclerViewList);
        String str_result = null;

        Object[] args = new Object[2];
        args[0] = getIntent().getStringExtra("USERID");
        args[1] = (Integer) list_type;

        try {
            str_result = (String) new LoadList(this).execute(args).get(5000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        Document listXML = null;

        try {
            listXML = loadXMLFromString(str_result);
        }catch(Exception e){
            e.printStackTrace();
        }

        if (listXML != null) {
            listXML.getDocumentElement().normalize();
            if (list_type == 0) {
                NodeList nList = listXML.getElementsByTagName("user");
                for (int x = 0; x < nList.getLength(); x++) {
                    Element Item = (Element) (nList.item(x));
                    itemID.add(Item.getAttribute("userID"));
                    itemname.add(Item.getAttribute("userName"));
                }
                listTitle.setText("Followed Users");
            }
            else if (list_type == 1) {
                NodeList nList = listXML.getElementsByTagName("topic");
                for (int x = 0; x < nList.getLength(); x++) {
                    Element Item = (Element) (nList.item(x));
                    itemID.add(Item.getAttribute("topicID"));
                    itemname.add(Item.getAttribute("topicName"));
                }
                listTitle.setText("Followed Topics");
            }
            else if (list_type == 2) {
                NodeList nList = listXML.getElementsByTagName("blocked");
                for (int x = 0; x < nList.getLength(); x++) {
                    Element Item = (Element) (nList.item(x));
                    itemID.add(Item.getAttribute("userID"));
                    itemname.add(Item.getAttribute("userName"));
                }
                listTitle.setText("Blocked Users");
            }
            else {
                Log.d("IMPOSSIBLE_LIST", "Seriously, how?");
            }
        }

        ListAdapter listAdapter = new ListAdapter(this, itemname, itemID);

        final int username_id = 0;
        final int unfollow_id = 1;

        listAdapter.setListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemSelected(int position, View view, ArrayList<Object> object) {
                if (position == username_id) {
                    if (list_type == 0 || list_type == 2) {
                        setContentView(R.layout.activity_public_profile);
                        Intent intent = new Intent(getApplicationContext(), PublicProfilePage.class);
                        intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                        intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                        intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                        intent.putExtra("PUBLIC_USER", object.get(0).toString());
                        startActivity(intent);
                    }
                    else if (list_type == 1) {
                        setContentView(R.layout.activity_topic_post);
                        Intent intent = new Intent(getApplicationContext(), TopicPostActivity.class);
                        intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                        intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                        intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
                        intent.putExtra("TOPICID", object.get(0).toString());
                        startActivity(intent);
                    }
                }
                else if (position == unfollow_id) {
                    Object[] objects = new Object[3];
                    objects[0] = object.get(0).toString();
                    objects[1] = getIntent().getStringExtra("USERID");
                    objects[2] = (Integer) list_type;
                    String unfollow_result = "";
                    try {
                        unfollow_result = (String) new UnfollowOrUnblock(getApplicationContext()).execute(objects).get(3000, TimeUnit.MILLISECONDS);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                    if (unfollow_result.equals("DEL")) {
                        Toast.makeText(getApplicationContext(), "Sucessfully Unfollwed/Unblocked", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Failed to Unfollow/Unblock at this time", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        recyclerViewList.setAdapter(listAdapter);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this));

        final Button homeButton = findViewById(R.id.go_home_list);

        homeButton.setOnClickListener(new View.OnClickListener() {
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