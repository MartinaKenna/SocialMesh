package it.unimib.socialmesh;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;


public class LoginActivity extends AppCompatActivity {
    private TextInputLayout emailTextInput;
    private TextInputLayout passTextInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailTextInput=findViewById(R.id.email);
        passTextInput=findViewById(R.id.insertPassword);
        Button loginButton = findViewById(R.id.buttonLogin);

    }



}

