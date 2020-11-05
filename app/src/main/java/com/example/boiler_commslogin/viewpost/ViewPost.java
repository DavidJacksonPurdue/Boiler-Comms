package com.example.boiler_commslogin.viewpost;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.boiler_commslogin.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ViewPost extends AsyncTask {
    private Context context;
    private String result;

    public ViewPost(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        URL url = null;
        String postID = (String) objects[0];
        //String userID = (String) objects[1];
        try {
            //url = new URL(Constants.GET_POST_BY_POSTID + userID + "_" + postID);
            url = new URL(Constants.GET_POST_BY_POSTID + postID);
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
        result = content.toString();
        System.out.println(content.toString());

        return result;
    }
}
