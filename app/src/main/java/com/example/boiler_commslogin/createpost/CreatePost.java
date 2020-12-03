package com.example.boiler_commslogin.createpost;

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

public class CreatePost extends AsyncTask {
    private TextView statusField,roleField;
    private Context context;
    private int byGetOrPost;
    private String result;
    String userCredentials;

    //flag 0 means get and 1 means post.(By default it is get.)
    public CreatePost(Context context) {
        this.context = context;
    }
    public String getUserCredentials(){
        return userCredentials;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        /* Get the parameters and store them */
        URL url = null;
        String userID = (String) objects[0];
        String postID = (String) objects[1];
        String topicID = (String) objects[2];
        String postName = (String) objects[3];
        String postText = (String) objects[4];
        String postDate = (String) objects[5];
        String filetype = (String) objects[7];
        String base64Image = "data:image/;" + filetype + ";base64," + ((String) objects[6]);
        Log.d("b64", base64Image);
        try {
            char delim = 157;
            url = new URL(Constants.CREATE_POST + userID + delim + postID + delim + topicID + delim + postName + delim + postText + delim + postDate);
            Log.d("URL", url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("URL", url.toString());
            return "Error";
        }
        HttpURLConnection con = null;
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("imageString", base64Image);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("IMAGE", base64Image.toString());
            return "Error";
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
            Log.d("WRITE", "Writing error");
            return "Error";
        }
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
        /*
        userCredentials = result;
        this.statusField.setText("Login Successful");
        this.roleField.setText(result);
        */
    }
}
