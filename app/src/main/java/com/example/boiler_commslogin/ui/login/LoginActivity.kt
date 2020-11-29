package com.example.boiler_commslogin.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.boiler_commslogin.R
import com.example.boiler_commslogin.data.LoginModel
import com.example.boiler_commslogin.data.MainActivity
import com.example.boiler_commslogin.sign_up.SignUpActivity
import java.util.concurrent.TimeUnit


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val signUp = findViewById<Button>(R.id.sign_up)

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
                .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                        username.text.toString(),
                        password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                                username.text.toString(),
                                password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {

                val result = LoginModel(applicationContext).execute(username.getText().toString(), password.getText().toString()).get()
                Log.d("resultoutputIguess", result.toString())
                Log.d("password_value", password.getText().toString())
                val hash = PasswordHasher(password.getText().toString())
                val hashed_pass = hash.hashPass()
                val actualpass = result.toString().substring(result.toString().lastIndexOf(' ') + 1)
                Log.d("actualpassword", actualpass)
                if (hashed_pass.equals(actualpass)) {
                    Toast.makeText(applicationContext, "Login Success. Welcome", Toast.LENGTH_LONG).show()
                    val userID = result.toString().substring(0, result.toString().indexOf(' '))
                    val username = result.toString().substring(result.toString().indexOf(' ') + 1, result.toString().lastIndexOf(' '))
                    setContentView(R.layout.activity_main)
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.putExtra("USERID", userID)
                    intent.putExtra("USERNAME", username)
                    intent.putExtra("PASSWORD", password.getText().toString())
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_LONG).show()
                }
            }

            signUp.setOnClickListener {
                setContentView(R.layout.activity_signup)
                val intent = Intent(applicationContext, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        Toast.makeText(
                applicationContext,
                "$welcome $displayName",
                Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}