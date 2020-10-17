package com.example.boiler_commslogin.delete_account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
// Written by Nick Rosato
// Delete an account from the mySQL database
// Access the database through a PHP link
public class deleteUser extends AsyncTask{
    private TextView statusField,roleField;
    private Context context;
    private int byGetOrPost;
    private String userID; // the user's ID in the database
    //flag 0 means get and 1 means post.(By default it is get.)
    public deleteUser(Context context, String userID) {
        this.context = context; // instantiate as "this"
        this.userID = userID; // instantiate the user id
    }
    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            /* Create the url request string using the parameters*/
            // the link is made for a simulator device on a localhost server
            String link = "http://10.0.2.2:63343/PHP_TEST2BOYS/deleteUser.php?q=" + this.userID;
            /* Create a new url */
            URL url = new URL(link);
            /* Open a connection */
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            /* Read the response from the server */
            int status = con.getResponseCode();
            /* Check if we have an error */
            if (status <= 299) {
                /* Create a buffer to read from the input stream */
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                /* Close the connection and disconnect */
                in.close();
                con.disconnect();
                System.out.println("Success in deleting user");
                return "Success";
            }
            else {
                /* If we have an error, disconnect */
                con.disconnect();
                System.out.println("Error deleting user");
                return "Error";
            }
            /* Catch errors and print the stack trace */
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
}