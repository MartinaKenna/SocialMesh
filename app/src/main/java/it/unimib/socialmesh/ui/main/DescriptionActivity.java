package it.unimib.socialmesh.ui.main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import it.unimib.socialmesh.R;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.appcompat.app.AppCompatActivity;

public class DescriptionActivity extends AppCompatActivity {

    private DatabaseReference userDescriptionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_description);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        userDescriptionRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUserId);
        userDescriptionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("descrizione").exists()) {
                    userDescriptionRef.setValue("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userDescriptionRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUserId)
                .child("descrizione");

        // Trova il pulsante e gestisci il clic
        Button confirmButton = findViewById(R.id.button_confirm_description);
        confirmButton.setOnClickListener(v -> {
            saveDescription();
            finish();
        });

        ImageButton backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void saveDescription() {

        EditText descriptionEditText = findViewById(R.id.descriptionEditText);
        String description = descriptionEditText.getText().toString().trim();
        if (!description.isEmpty()) {
            userDescriptionRef.setValue(description)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getApplicationContext(), "Descrizione salvata", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {

                        Toast.makeText(getApplicationContext(), "Errore durante il salvataggio", Toast.LENGTH_SHORT).show();
                    });
        } else {

            Toast.makeText(getApplicationContext(), "La descrizione non può essere vuota", Toast.LENGTH_SHORT).show();
        }
    }
}
