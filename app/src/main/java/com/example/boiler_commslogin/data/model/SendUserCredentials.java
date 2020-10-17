package com.example.boiler_commslogin.data.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.Callable;


public class SendUserCredentials extends AsyncTask {
    private TextView statusField,roleField;
    private View.OnClickListener context;
    private int byGetOrPost;
    private String result;
    String userCredentials;

    //flag 0 means get and 1 means post.(By default it is get.)
    public SendUserCredentials(View.OnClickListener context) {
        this.context = context;
    }
    public String getUserCredentials(){
        return userCredentials;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        URL url = null;
        String userID = (String)objects[0];
        String username = (String)objects[1];
        String firstname = (String)objects[2];
        String lastname = (String)objects[3];
        String email = (String)objects[4];
        String password = (String)objects[5];
        String filetype = (String)objects[7];
        String base64Image = "data:image/;" + filetype +";base64," + ((String)objects[6]);
        try {
            url = new URL("http://10.0.2.2:63343/PHP_TEST2BOYS/uploadFile2.php?q=" + userID + "_" + username + "_" +  firstname + "_" + lastname + "_" + email + "_" + password);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection con = null;
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("imageString", base64Image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = jsonObject.toString();
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setFixedLengthStreamingMode(data.getBytes().length);
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

            OutputStream out = new BufferedOutputStream(con.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            out.close();
            con.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //con.setConnectTimeout(5000);
        //con.setReadTimeout(5000);



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
        Log.d("CONTENTLOG",content.toString());
        try {
            if(in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        userCredentials =  content.toString();
        return "Success";
    }

    protected void onPostExecute(String result){
        userCredentials = result;
        this.statusField.setText("Login Successful");
        this.roleField.setText(result);
    }

}
