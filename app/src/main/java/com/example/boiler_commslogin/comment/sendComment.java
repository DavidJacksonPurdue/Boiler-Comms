package com.example.boiler_commslogin.comment;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.boiler_commslogin.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class sendComment extends AsyncTask {
    private TextView statusField,roleField;
    private Context context;
    private int byGetOrPost;
    private String result;
    String comments;

    //flag 0 means get and 1 means post.(By default it is get.)
    public sendComment(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        URL url = null;
        String userID = (String)objects[0];
        Log.d("Send comment: objects 0", objects[0].toString());
        Log.d("Send comment: objects 1", objects[1].toString());
        Log.d("Send comment: objects 2", objects[2].toString());
        Log.d("Send comment: objects 3", objects[3].toString());
        Log.d("Send comment: objects 4", objects[4].toString());

        try {
            url = new URL(Constants.SENDCOMMENT + objects[0].toString() + "_" + objects[1].toString() + "_" + objects[2].toString() + "_" + objects[3].toString() + "_" + objects[4].toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d("url", url.toString());
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
        Log.d("initial",content.toString());
        try {
            if(in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        comments =  content.toString();
        Log.d("comments", comments);
        return content.toString();
    }

}
