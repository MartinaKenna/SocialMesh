package it.unimib.socialmesh.ui.main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.data.repository.user.UserRepository;
import it.unimib.socialmesh.util.ServiceLocator;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SettingsActivity extends AppCompatActivity {
    ImageView profilePic;
    Uri selectedImageUri;
    EditText descriptionEditText;
    ActivityResultLauncher<Intent> imagePickLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        loadProfileImage();
        descriptionEditText = findViewById(R.id.descriptionEditText);
        descriptionEditText.requestFocus();
        profilePic = findViewById(R.id.settings_profile_image);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            updateProfilePic(selectedImageUri);
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
        Button updateSettingsButton = findViewById(R.id.update_account_settings_btn);
        updateSettingsButton.setOnClickListener(view -> {
            if (selectedImageUri != null) {
                uploadProfilePic(selectedImageUri,view);
                Snackbar.make(view, "Immagine profilo aggiornata correttamente", Snackbar.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Seleziona un'immagine prima di aggiornare", Toast.LENGTH_SHORT).show();
            }
        });
    }


 private void updateProfilePic(Uri selectedImageUri) {
     if (selectedImageUri != null) {
         Glide.with(this)
                 .load(selectedImageUri)
                 .apply(RequestOptions.circleCropTransform())
                 .placeholder(R.drawable.baseline_error_black_24dp)
                 .error(R.drawable.baseline_error_black_24dp)
                 .into(profilePic);
     }
 }

    private void uploadProfilePic(Uri selectedImageUri, View view) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference userRef = storageRef.child("pictures").child(currentUserId).child("profilePic.jpg");

        if (selectedImageUri != null) {
            UploadTask uploadTask = userRef.putFile(selectedImageUri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                userRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageURL = uri.toString();
                    Glide.with(this)
                            .load(imageURL)
                            .apply(RequestOptions.circleCropTransform())
                            .placeholder(R.drawable.baseline_error_black_24dp)
                            .error(R.drawable.baseline_error_black_24dp)
                            .into(profilePic);
                });
                finish();
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
                    Glide.with(this)
                            .load(imageURL)
                            .apply(RequestOptions.circleCropTransform())
                            .placeholder(R.drawable.baseline_error_black_24dp)
                            .error(R.drawable.baseline_error_black_24dp)
                            .into(profilePic);
                }
            } else {

                if (!isDestroyed()) { // Assicurati che l'activity non sia distrutta
                    Glide.with(this)
                            .load(com.facebook.R.drawable.com_facebook_profile_picture_blank_portrait)
                            .apply(RequestOptions.circleCropTransform())
                            .into(profilePic);
                }
            }
        }).addOnFailureListener(exception -> {
            Log.e("Settings", "Errore durante il caricamento dell'immagine del profilo: " + exception.getMessage());
        });
    }

}
