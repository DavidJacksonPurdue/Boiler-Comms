package com.example.boiler_commslogin;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class loadComments extends AsyncTask {
    private TextView statusField,roleField;
    private Context context;
    private int byGetOrPost;
    private String result;
    String comments;

    //flag 0 means get and 1 means post.(By default it is get.)
    public loadComments(Context context) {
        this.context = context;
    }
    public String getComments(){
        return comments;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        URL url = null;
        String userID = (String)objects[0];
        try {
            url = new URL(Constants.GETCOMMENTS + objects[0].toString());
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

    protected void onPostExecute(String result){
        comments = result;
        this.statusField.setText("Login Successful");
        this.roleField.setText(result);
    }
}
