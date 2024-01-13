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

        // Controlla se il tuo Switch è null e setta il listener solo se è diverso da null
        if (themeSwitch != null) {
            themeSwitch.setChecked(currentTheme != AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // Cambia a tema scuro
                    currentTheme = AppCompatDelegate.MODE_NIGHT_YES;
                    Log.d("Profile","night mode");
                } else {
                    // Cambia a tema chiaro
                    currentTheme = AppCompatDelegate.MODE_NIGHT_NO;
                }

                // Applica il nuovo tema all'attività corrente
                AppCompatDelegate.setDefaultNightMode(currentTheme);

                // Ricrea l'attività per applicare il nuovo tema
            });
        }
    }
}