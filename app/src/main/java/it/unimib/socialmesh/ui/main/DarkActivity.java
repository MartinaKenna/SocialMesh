package it.unimib.socialmesh.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;

import it.unimib.socialmesh.R;

public class DarkActivity extends AppCompatActivity {
    private Switch themeSwitch;
    private int currentTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dark_mode);
        themeSwitch = findViewById(R.id.abudabi);

        if (themeSwitch != null) {
            themeSwitch.setChecked(currentTheme != AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    currentTheme = AppCompatDelegate.MODE_NIGHT_YES;
                    Log.d("Profile","night mode");
                } else {
                    currentTheme = AppCompatDelegate.MODE_NIGHT_NO;
                }

                AppCompatDelegate.setDefaultNightMode(currentTheme);
            });
        }
    }
}