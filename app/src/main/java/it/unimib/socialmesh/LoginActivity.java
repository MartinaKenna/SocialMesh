package it.unimib.socialmesh;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class LoginActivity extends AppCompatActivity {
    ImageView facebook;
    CallbackManager callbackManager;
    private TextInputLayout passTextInput;
    private TextInputLayout emailTextInput;

    private static final String EMAIL = "email";

    private static final String TAG = LoginActivity.class.getSimpleName();
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailTextInput=findViewById(R.id.email);
        passTextInput=findViewById(R.id.insertPassword);
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        Button loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(item -> {
            String email = emailTextInput.getEditText().getText().toString();
            String password = passTextInput.getEditText().getText().toString();
            if(isEmailOk(email) && isPasswordOk(password)) {
                emailTextInput.setError(null);
                passTextInput.setError(null);
            }
            else{
                Snackbar.make(findViewById(android.R.id.content), "E-mail o Password errate", Snackbar.LENGTH_SHORT).show();
            }
        });
        facebook = findViewById(R.id.Facebook);
        facebook.setOnClickListener(item -> {


            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));

        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    private boolean isEmailOk(String email) {
        // Check if the email is valid through the use of this library:
        // https://commons.apache.org/proper/commons-validator/
        if (!EmailValidator.getInstance().isValid((email))) {
            emailTextInput.setError("Email non valida");
            return false;
        } else {
            emailTextInput.setError(null);
            return true;
        }

        //TODO confrontare i dati con firebase
    }

    private boolean isPasswordOk(String password) {

        // If the password is empty return false
        if (password == null) {
            passTextInput.setError(getString(R.string.error_password_invalid));
            return false;
        }

        // Regex to check valid password.
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!()_%])(?=\\S+$).{8,20}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);

        boolean match = m.matches();

        // If password matches pattern return true
        if(!match) {
            passTextInput.setError(getString(R.string.error_password_invalid));
            return false;
        } else {
            passTextInput.setError(null);
            return true;
        }

        //TODO confrontare i dati con firebase
        }
    }


