package com.example.boiler_commslogin.sign_up;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.boiler_commslogin.R;
import com.example.boiler_commslogin.ui.login.LoginActivity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText, firstNameEditText, lastNameEditText, emailEditText,
            passwordEditText, passwordConfirmEditText;
    Button signUpButton;

    private String validEmail, validPassword, validPasswordConfirm, validUsername, validFirstName,
            validLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /* Assign the input fields to a variable */
        usernameEditText = findViewById(R.id.username);
        firstNameEditText = findViewById(R.id.first_name);
        lastNameEditText = findViewById(R.id.last_name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        passwordConfirmEditText = findViewById(R.id.password_confirm);

        /* Create the listener for the signup button */
        signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpActivity.this.signUp();
            }
        });

        initializeListeners();

    }

    public void signUp() {

        /* Get inputs from text fields */
        String username = usernameEditText.getText().toString();
        String first_name = firstNameEditText.getText().toString();
        String last_name = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String str_result = "failure";
        String ver_result = "";

        /* Check to see if inputs are valid */
        if ((validEmail != null) && (validPassword != null) && (validPasswordConfirm != null) &&
                (validFirstName != null) && (validLastName != null) && (validUsername != null)) {

            /* Create a new signup model and execute the doInBackground function */

            // Hash the password
            PasswordHasher p = new PasswordHasher(password);
            password = p.hashPass();

            try {
                ver_result = (String) new verifyUser(this).execute(username, email).get(2000, TimeUnit.MILLISECONDS);
                if (ver_result.equals("Success")) {
                    str_result = (String) new SignUpModel(this).execute(username, first_name,
                            last_name, email, password).get(2000, TimeUnit.MILLISECONDS);
                    if (str_result.equals("Success")) {
                        Toast.makeText(this, "Successfully created account!",
                                Toast.LENGTH_SHORT).show();
                        setContentView(R.layout.activity_login);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Failed to create account!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), ver_result,Toast.LENGTH_LONG).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "Failed: Ensure inputs are valid!",
                    Toast.LENGTH_LONG).show();
        }
    }





    /**
     * Initialize UI to create listeners
     */
    private void initializeListeners() {
        usernameEditText = (EditText) findViewById(R.id.username);
        usernameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                isValidUser(usernameEditText);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void isValidUser(EditText username) {
                if (username.getText().toString().length() == 0) {
                    username.setError("Required Field!");
                    validUsername = null;
                } else if (!isUserValid(username.getText().toString())) {
                    username.setError("Required Field!");
                    validUsername = null;
                } else {
                    validUsername = username.getText().toString();
                }
            }

            boolean isUserValid(CharSequence user) {
                return user.length() != 0;
            }
        });


        firstNameEditText = (EditText) findViewById(R.id.first_name);
        firstNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                isValidFirst(firstNameEditText);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void isValidFirst(EditText first) {
                if (first.getText().toString().length() == 0) {
                    first.setError("Required Field!");
                    validFirstName = null;
                } else if (!isFirstValid(first.getText().toString())) {
                    first.setError("Required Field!");
                    validFirstName = null;
                } else {
                    validFirstName = first.getText().toString();
                }
            }

            boolean isFirstValid(CharSequence first) {
                return first.length() != 0;
            }
        });

        lastNameEditText = (EditText) findViewById(R.id.last_name);
        lastNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                isValidLast(lastNameEditText);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void isValidLast(EditText last) {
                if (last.getText().toString().length() == 0) {
                    last.setError("Required Field!");
                    validLastName = null;
                } else if (!isLastValid(last.getText().toString())) {
                    last.setError("Required Field!");
                    validLastName = null;
                } else {
                    validLastName = last.getText().toString();
                }
            }

            boolean isLastValid(CharSequence last) {
                return last.length() != 0;
            }
        });



        emailEditText = (EditText) findViewById(R.id.email);
        emailEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                Is_Valid_Email(emailEditText);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void Is_Valid_Email(EditText edt) {
                if (edt.getText().toString().length() == 0) {
                    edt.setError("Invalid Email Address");
                    validEmail = null;
                } else if (!isEmailValid(edt.getText().toString())) {
                    edt.setError("Invalid Email Address");
                    validEmail = null;
                } else {
                    validEmail = edt.getText().toString();
                }
            }

            boolean isEmailValid(CharSequence email) {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches();
            }
        });


        /*
         * Create a listener for password confirmation
         */

        passwordEditText = (EditText) findViewById(R.id.password);
        passwordEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                Is_Valid_Password(passwordEditText);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }


            public void Is_Valid_Password(EditText psw) {
                if (psw.getText().toString().length() == 0) {
                    psw.setError("Invalid Password");
                    validPassword = null;
                } else if (!isPasswordValid(psw.getText().toString())) {
                    psw.setError("Invalid Password");
                    validPassword = null;
                } else {
                    validPassword = psw.getText().toString();
                }
            }

            boolean isPasswordValid(CharSequence password) {
                if (password == null || password.length() < 9) {
                    return false;
                }
                String password2 = (String) password;
                String numRegex = (".*[0-9].*");
                String alphaRegex = (".*[A-Z].*");
                String alphaRegex2 = (".*[a-z].*");
                if (!password2.matches(numRegex) || !(password2.matches(alphaRegex) ||
                        password2.matches(alphaRegex2))) {
                    return false;
                }
                return true;
            }
        });



        /*
         * Create a listener to check if the confirmed password is valid.
         */
        passwordConfirmEditText = (EditText) findViewById(R.id.password_confirm);
        passwordEditText = (EditText) findViewById(R.id.password);
        passwordConfirmEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                Is_Valid_PasswordConfirm(passwordEditText, passwordConfirmEditText);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void Is_Valid_PasswordConfirm(EditText password, EditText passwordConfirm) {
                if (passwordConfirm.getText().toString().length() == 0) {
                    passwordConfirm.setError("Passwords must match!");
                    validPasswordConfirm = null;
                } else if (!isPasswordValid(password.getText().toString(),
                        passwordConfirm.getText().toString())) {
                    passwordConfirm.setError("Passwords must match!");
                    validPasswordConfirm = null;
                } else {
                    validPasswordConfirm = passwordConfirm.getText().toString();
                }
            }

            boolean isPasswordValid(CharSequence password, CharSequence passwordConfirm) {
                return password.equals(passwordConfirm);
            }
        });

    }
}