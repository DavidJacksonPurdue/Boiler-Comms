package com.example.boiler_commslogin.data;

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

public class getTopics extends AsyncTask {

    String topics;
    Context context;
    public getTopics(Context context) {this.context = context;}
    public String getTopics() { return topics; }

    @Override
    protected Object doInBackground(Object[] objects) {
        URL url = null;
        try {
            url = new URL(Constants.GET_TOPICS);

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
        StringBuilder content = new StringBuilder();
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
        topics = content.toString();
        return content.toString();
    }
}
