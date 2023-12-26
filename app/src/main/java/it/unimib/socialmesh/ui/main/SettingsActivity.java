package it.unimib.socialmesh.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import it.unimib.socialmesh.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        // ...

        // Quando l'utente preme il pulsante "Chiudi" nell'Activity Settings
        Button closeSettingsButton = findViewById(R.id.close_settings_btn);
        closeSettingsButton.setOnClickListener(view -> {
            // Imposta il risultato come RESULT_CANCELED e chiudi l'Activity
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
        // Quando l'utente preme il pulsante "Update" nell'Activity Settings
        Button updateSettingsButton = findViewById(R.id.update_account_settings_btn);
        updateSettingsButton.setOnClickListener(view -> {
            // Simula l'aggiornamento dei dati, prendi i nuovi dati aggiornati
            String updatedData = "Dati aggiornati"; // Aggiungi il modo in cui ottieni i dati aggiornati

            // Crea un Intent per contenere i dati aggiornati e chiudi l'Activity
            Intent returnIntent = new Intent();
            returnIntent.putExtra("UPDATED_DATA_KEY", updatedData);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });

    }
}