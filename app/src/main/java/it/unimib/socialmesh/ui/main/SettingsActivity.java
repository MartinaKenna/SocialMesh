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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    FirebaseDatabase database;
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    StorageReference profilePicsRef = storageRef.child("profilePictures");
    StorageReference userPicRef = profilePicsRef.child(userId); // Creazione della cartella per l'utente
    User currentUserModel;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /*
        loadProfileImage();
        profilePic = findViewById(R.id.settings_profile_image);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            setProfilePic(this, selectedImageUri, profilePic);
                        }
                    }
                }
        );
        // ...
        profilePic.setOnClickListener((v) -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });
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
            if (selectedImageUri != null) {
                database = FirebaseDatabase.getInstance();
                long time = new Date().getTime();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                StorageReference reference = storage.getReference().child("Profiles").child(userId).child(time + "");
                reference.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if (task.isSuccessful()) {
                                        reference.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                                            String filePath = downloadUri.toString();

                                            // Aggiornamento del repository con il nuovo URL dell'immagine del profilo
                                            ServiceLocator.getInstance().getUserRepository(getApplication()).
                                                    .addOnSuccessListener(aVoid -> {
                                                        // Invia l'URL dell'immagine come risultato all'Activity chiamante
                                                        Intent resultIntent = new Intent();
                                                        resultIntent.putExtra("image_url", filePath);
                                                        setResult(Activity.RESULT_OK, resultIntent);
                                                        Toast.makeText(getApplicationContext(), "Immagine caricata correttamente", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        // Gestione degli errori durante l'aggiornamento del repository
                                                        // Potresti voler gestire un eventuale rollback qui
                                                    });
                                        });
                                    }
                                }
                            });
                        }
                    }
                });


            }


        });
    }

    private void loadProfileImage() {
        UserRepository.getInstance().getUserProfileImageUrl()
                .addOnSuccessListener(imageUrlSnapshot -> {
                    if (imageUrlSnapshot.exists()) {
                        String imageUrl = imageUrlSnapshot.getValue(String.class);
                        // Visualizza l'immagine nel ImageView
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(this)
                                    .load(imageUrl)
                                    .apply(RequestOptions.circleCropTransform())
                                    .placeholder(R.drawable.baseline_lock_clock_black_24dp)
                                    .error(R.drawable.baseline_error_black_24dp)
                                    .into(profilePic);
                        } else {
                            // Se l'URL dell'immagine non è disponibile, visualizza un'immagine di default
                            profilePic.setImageResource(R.drawable.baseline_person_outline_24);
                        }
                    } else {
                        // Se lo snapshot non contiene dati, visualizza un'immagine di default
                        profilePic.setImageResource(R.drawable.baseline_person_outline_24);
                    }
                })
                .addOnFailureListener(e -> {
                    // Gestisci eventuali errori nel caricamento dell'URL dell'immagine del profilo
                });
    }


    public static void setProfilePic (Context context, Uri imageUri, ImageView imageView){
            Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
        }

         */

    }}