package it.unimib.socialmesh.ui.main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import it.unimib.socialmesh.R;

public class SettingsActivity extends AppCompatActivity {
    ImageView profilePic;
    Uri selectedImageUri;
    EditText descriptionEditText;
    ActivityResultLauncher<Intent> imagePickLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();
        loadProfileImage();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUserId);
        updateDescription(currentUserId);

        profilePic = findViewById(R.id.settings_profile_image);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            updateProfilePic(selectedImageUri);
                            if (selectedImageUri != null) {
                                uploadProfilePic(selectedImageUri);
                            }
                        }
                    }
                }
        );
        profilePic.setOnClickListener((v) -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                    .createIntent(intent -> {
                        imagePickLauncher.launch(intent);
                        return null;
                    });
        });
        Button closeSettingsButton = findViewById(R.id.close_settings_btn);
        closeSettingsButton.setOnClickListener(view -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        Button editDescriptionButton = findViewById(R.id.button_edit_decription);
        editDescriptionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, DescriptionActivity.class);
            startActivity(intent);

        });
    }

    private void updateDescription(String currentUserId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUserId);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        userRef.child("descrizione").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String description = snapshot.getValue(String.class);
                    // Imposta la descrizione nel TextView
                    descriptionTextView.setText(description);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestire eventuali errori
            }
        });
    }


    private void updateProfilePic(Uri selectedImageUri) {
     if (selectedImageUri != null) {
         CircularProgressDrawable drawable = new CircularProgressDrawable(this);
         drawable.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
         drawable.setCenterRadius(30f);
         drawable.setStrokeWidth(5f);
         drawable.start();

         Glide.with(this)
                 .load(selectedImageUri)
                 .apply(RequestOptions.circleCropTransform())
                 .placeholder(drawable)
                 .error(drawable)
                 .into(profilePic);
     }
 }

    private void uploadProfilePic(Uri selectedImageUri) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference userRef = storageRef.child("pictures").child(currentUserId).child("profilePic.jpg");

        if (selectedImageUri != null) {
            UploadTask uploadTask = userRef.putFile(selectedImageUri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                userRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageURL = uri.toString();
                    CircularProgressDrawable drawable = new CircularProgressDrawable(this);
                    drawable.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
                    drawable.setCenterRadius(30f);
                    drawable.setStrokeWidth(5f);
                    drawable.start();
                    Glide.with(this)
                            .load(imageURL)
                            .apply(RequestOptions.circleCropTransform())
                            .placeholder(drawable)
                            .error(drawable)
                            .into(profilePic);
                });
            }).addOnFailureListener(exception -> {
                Log.e("Settings", "Errore durante il caricamento dell'immagine");
            });
        } else {
            Log.e("Settings", "URI dell'immagine selezionata è nullo");
        }
    }
    private void loadProfileImage() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference userRef = storageRef.child("pictures").child(currentUserId).child("profilePic.jpg");

        userRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageURL = uri.toString();

            if (imageURL != null && !imageURL.isEmpty()) {
                if (!isDestroyed()) {
                    CircularProgressDrawable drawable = new CircularProgressDrawable(this);
                    drawable.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
                    drawable.setCenterRadius(30f);
                    drawable.setStrokeWidth(5f);
                    drawable.start();
                    Glide.with(this)
                            .load(imageURL)
                            .apply(RequestOptions.circleCropTransform())
                            .placeholder(drawable)
                            .error(drawable)
                            .into(profilePic);
                }
            } else {

                if (!isDestroyed()) {
                    CircularProgressDrawable drawable = new CircularProgressDrawable(this);
                    drawable.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
                    drawable.setCenterRadius(30f);
                    drawable.setStrokeWidth(5f);
                    drawable.start();
                    Glide.with(this)
                            .load(imageURL)
                            .placeholder(drawable)
                            .error(drawable)
                            .apply(RequestOptions.circleCropTransform())
                            .into(profilePic);
                }
            }
        }).addOnFailureListener(exception -> {
            Log.e("Settings", "Errore durante il caricamento dell'immagine del profilo: " + exception.getMessage());
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();
        updateDescription(currentUserId);

    }
}
