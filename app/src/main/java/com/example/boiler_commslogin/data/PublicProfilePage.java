package com.example.boiler_commslogin.data;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boiler_commslogin.R;
import com.example.boiler_commslogin.data.MainActivity;
import com.example.boiler_commslogin.data.PasswordHasher;
import com.example.boiler_commslogin.data.model.LoadUserCredentials;
import com.example.boiler_commslogin.data.model.RequestHandler;
import com.example.boiler_commslogin.data.model.SendUserCredentials;
import com.example.boiler_commslogin.delete_account.deleteUser;
import com.example.boiler_commslogin.sign_up.verifyUser;
import com.example.boiler_commslogin.ui.login.LoginViewModel;
import com.example.boiler_commslogin.ui.login.LoginViewModelFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EditUserProfile extends AppCompatActivity {
    public static final String UPLOAD_URL = "http://10.0.2.2:63343/PHP_TEST2BOYS/postUserCredentials.php";
    public static final String UPLOAD_KEY = "image";

    private int PICK_IMAGE_REQUEST = 1;

    private Button buttonChoose;
    private Button buttonUpload;

    private ImageView imageView;
    private Bitmap bitmap;
    private Uri filePath;




    public static final int PICK_IMAGE = 100;
    Uri imageURI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile);
        final EditText userNameEditText = findViewById(R.id.username4);
        final EditText emailEditText = findViewById(R.id.username);
        final EditText firstNameEditText = findViewById(R.id.username2);
        final EditText lastNameEditText = findViewById(R.id.username3);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText confirmPasswordEditText = findViewById(R.id.password2);
        final ImageView profilePicImageView = findViewById(R.id.imageView);
        final Button loginButton = findViewById(R.id.save_changes);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final Button deletebutton = findViewById(R.id.button4);

        //download initial values from user
        String str_result = null;

        try {
            str_result= (String)new LoadUserCredentials(this).execute(getIntent().getStringExtra("USERID")).get(2000, TimeUnit.MILLISECONDS);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        String userName = null;
        String email = null;
        String password = null;
        String firstName = null;
        String lastName = null;
        String img = null;


        try {
            JSONObject reader = new JSONObject(str_result);
            userName = reader.getString("userName");
            email  = reader.getString("email");
            password = reader.getString("PASSWORD");
            firstName = reader.getString("firstName");
            lastName = reader.getString("lastName");
            img = reader.getString("profilePic");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String i_username = userName;
        final String i_email = email;
        userNameEditText.setText(userName);
        emailEditText.setText(email);
        password = getIntent().getStringExtra("PASSWORD");
        passwordEditText.setText(password);
        confirmPasswordEditText.setText(password);
        firstNameEditText.setText(firstName);
        lastNameEditText.setText(lastName);

        if(!img.equals("null")) {
            String base64Image = img.split(",")[1];
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profilePicImageView.setImageBitmap(decodedByte);
        }

        EditUser_LoginViewModel.getLoginFromState().observe(this, new Observer<Edit_User_LoginFromState>() {
            @Override
            public void onChanged(@Nullable Edit_User_LoginFromState Edit_User_LoginFromState) {
                if (Edit_User_LoginFromState == null) {
                    return;
                }
                loginButton.setEnabled(Edit_User_LoginFromState.isDataValid());
                if (Edit_User_LoginFromState.getUsernameError() != null) {
                    emailEditText.setError(getString(Edit_User_LoginFromState.getUsernameError()));
                }
                if (Edit_User_LoginFromState.getPasswordError() != null) {
                    passwordEditText.setError(getString(Edit_User_LoginFromState.getPasswordError()));
                }
                if (Edit_User_LoginFromState.getConfirmPasswordError() != null) {
                    confirmPasswordEditText.setError(getString(Edit_User_LoginFromState.getConfirmPasswordError()));
                }
            }
        });

        EditUser_LoginViewModel.getLoginResult().observe(this, new Observer<EditLoginResult>() {
            @Override
            public void onChanged(@Nullable EditLoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                EditUser_LoginViewModel.loginDataChanged(emailEditText.getText().toString(),
                        passwordEditText.getText().toString(), confirmPasswordEditText.getText().toString());
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        confirmPasswordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    EditUser_LoginViewModel.login(emailEditText.getText().toString(),
                            passwordEditText.getText().toString(), confirmPasswordEditText.getText().toString());
                }
                return false;
            }
        });
        confirmPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    EditUser_LoginViewModel.login(emailEditText.getText().toString(),
                            passwordEditText.getText().toString(), confirmPasswordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText emailEditText = findViewById(R.id.username);
                final EditText usernameEditText = findViewById(R.id.username4);
                final EditText firstNameEditText = findViewById(R.id.username2);
                final EditText lastNameEditText = findViewById(R.id.username3);
                final EditText passwordEditText = findViewById(R.id.password);
                final ImageView profilePicImageView = findViewById(R.id.imageView);

                String email = emailEditText.getText().toString();
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String username = userNameEditText.getText().toString();
                String userID = getIntent().getStringExtra("USERID");
                String ver_result = "";

                //BitmapDrawable drawable = (BitmapDrawable) profilePicImageView.getDrawable();
                //Bitmap bitmap = drawable.getBitmap();
                //ByteArrayOutputStream bos = new ByteArrayOutputStream();
                //bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
                //byte[] bb = bos.toByteArray();
                //String image = Base64.encodeToString(bb,0,bb.length, Base64.DEFAULT);
                //image.replaceAll("//+", "%2B");
                //image.replaceAll("///", "%2F");
                String str_result = "";

                try {
                    ver_result = (String) new verifyUser(getApplicationContext()).execute(username, email).get(3000, TimeUnit.MILLISECONDS);
                    if (ver_result.equals("Success") || (ver_result.equals("Both that username and that email are in use") && (i_email.equals(email) && i_username.equals(username))) ||
                            (ver_result.equals("That username is already in use") && i_username.equals(username)) || (ver_result.equals("That email is already in use") && i_email.equals(email))) {
                        PasswordHasher hasher = new PasswordHasher(password);
                        String hashed_pass = hasher.hashPass();
                        if (imageURI != null) {
                            String base64image = uriToBitmap(imageURI);
                            Log.d("imageIsHere", base64image);
                            String extension = imageURI.toString().substring(imageURI.toString().lastIndexOf("."));
                            Log.d("extension2", extension);
                            str_result = (String) new SendUserCredentials(this).execute(userID, username, firstName, lastName, email, hashed_pass, base64image, extension).get(2000, TimeUnit.MILLISECONDS);
                        }
                        else {
                            str_result = (String) new SendUserCredentials(this).execute(userID, username, firstName, lastName, email, hashed_pass, "<<>>", "<<>>").get(2000, TimeUnit.MILLISECONDS);
                        }
                        if (str_result.equals("Success")) {
                            Toast.makeText(getApplicationContext(), "User information updated successfully", Toast.LENGTH_LONG).show();
                            setContentView(R.layout.activity_main);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("USERID", getIntent().getStringExtra("USERID"));
                            intent.putExtra("USERNAME", username);
                            intent.putExtra("PASSWORD", password);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error in updating user info, please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), ver_result,
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        });

        Button loadButton = (Button) findViewById(R.id.button);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open gallery
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("Delete Account?");
        builder.setMessage("Are you sure you want to delete your account? This action cannot be undone.");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String del_result = "";
                try {
                    del_result = (String) new deleteUser(getApplicationContext(), getIntent().getStringExtra("USERID")).execute().get(2000, TimeUnit.MILLISECONDS);
                }catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                if (del_result.equals("Success")) {
                    Toast.makeText(getApplicationContext(), "Bye Bye", Toast.LENGTH_LONG).show();
                    setContentView(R.layout.activity_login);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Delete user failed. Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_LONG).show();
            }
        });
        builder.setIcon(android.R.drawable.ic_dialog_alert);


        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(-1).setVisibility(View.VISIBLE);
                dialog.getButton(-2).setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateUiWithUser(EditLoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private String uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return getEncoded64ImageStringFromBitmap(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==  RESULT_OK && requestCode == PICK_IMAGE) {
            imageURI = data.getData();
            final ImageView profilePicImageView = findViewById(R.id.imageView);
            profilePicImageView.setImageURI(imageURI);
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    String getFileName(Uri uri){
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);
                data.put("name",getFileName(filePath));

                String result = rh.postRequest(UPLOAD_URL,data);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

}