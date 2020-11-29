package com.example.boiler_commslogin.data;

import android.content.Context;
import android.os.AsyncTask;

import com.example.boiler_commslogin.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class getPostsByTopicModel extends AsyncTask {
    private Context context;
    String topicPosts;

    public getPostsByTopicModel(Context context) {this.context = context;}
    public String getUserCredentials() { return topicPosts; }

    @Override
    protected Object doInBackground(Object[] objects) {
        URL url = null;
        String topicID = (String)objects[0];
        try {
            url = new URL(Constants.GETPOSTTOPIC + objects[0].toString() + "_" + objects[1].toString());
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
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        topicPosts = content.toString();
        return content.toString();
    }
}
