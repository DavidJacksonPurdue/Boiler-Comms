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

public class setProfileToFollowOnly extends AsyncTask {
    private Context context;
    String userCredentials;

    //flag 0 means get and 1 means post.(By default it is get.)
    public setProfileToFollowOnly(Context context) {
        this.context = context;
    }
    public String getUserCredentials(){
        return userCredentials;
    }
    @Override
    protected Object doInBackground(Object[] objects) {
        URL url = null;
        String userID = (String)objects[0];
        try {
            url = new URL(Constants.SENDFOLLOWCASE + objects[0].toString());
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
            if(in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        userCredentials = content.toString();
        return userCredentials;
    }
    protected void onPostExecute(String result){
        userCredentials = result;
    }
}
