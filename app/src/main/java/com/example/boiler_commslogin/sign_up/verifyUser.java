package com.example.boiler_commslogin.sign_up;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import com.example.boiler_commslogin.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class verifyUser extends AsyncTask {

    private Context context;

    //flag 0 means get and 1 means post.(By default it is get.)
    public verifyUser(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            /* Get the parameters and store them */
            String username = (String)objects[0];
            String email = (String)objects[1];

            /* Create the url request string using the parameters*/
            String link = "http://10.0.2.2:63343/PHP_TEST2BOYS/verifyUser.php?q="+username+"_"+email;

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
                if (content.toString().equals("UEF")) {
                    return context.getString(R.string.username_and_email_taken);
                }
                else if (content.toString().equals("UF")) {
                    return context.getString(R.string.username_taken);
                }
                else if (content.toString().equals("EF")) {
                    return context.getString(R.string.email_taken);
                }
                else if (content.toString().equals("F")) {
                    return "Success";
                }
                else {
                    return "Error";
                }

            }
            else {
                /* If we have an error, disconnect */
                con.disconnect();
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