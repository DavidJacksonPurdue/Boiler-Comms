package com.example.boiler_commslogin.savepost;

import android.content.Context;
import android.os.AsyncTask;

import com.example.boiler_commslogin.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class SavePost extends AsyncTask {
    private Context context;
    private String result;

    public SavePost(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        URL url = null;
        String userID = (String) objects[0];
        String postID = (String) objects[1];
        try {
            url = new URL(Constants.SAVE_POST + userID + "_" + postID);
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
            con.setRequestMethod("POST");
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

        return "Success";
    }
}
