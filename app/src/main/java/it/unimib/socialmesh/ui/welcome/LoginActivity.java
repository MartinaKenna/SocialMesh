package it.unimib.socialmesh.ui.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import it.unimib.socialmesh.R;

public class LoginActivity extends AppCompatActivity {
    Fragment fragment;
    float v=0;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragment=new LoginTabFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, fragment).addToBackStack(null).commit();
    }
}


