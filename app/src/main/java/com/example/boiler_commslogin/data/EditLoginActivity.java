package com.example.boiler_commslogin.data;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;


import com.example.boiler_commslogin.R;

public class EditLoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
    }
    public void login(View view) {
            String email = emailEditText.toString();
            String password = passwordEditText.toString();

            new LoginModel(this).execute(email, password);
    }
    public static LoggedInUser loggedUser(String content) {
        return new LoggedInUser(content.substring(0,1), content.substring(1));
    }
}
