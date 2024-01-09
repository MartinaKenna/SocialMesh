package it.unimib.socialmesh.ui.main;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.adapter.InterestsAdapter;
import it.unimib.socialmesh.adapter.PhotosAdapter;
import it.unimib.socialmesh.data.repository.user.IUserRepository;
import it.unimib.socialmesh.ui.welcome.UserViewModel;
import it.unimib.socialmesh.ui.welcome.UserViewModelFactory;
import it.unimib.socialmesh.util.ServiceLocator;

public class SettingsActivity extends AppCompatActivity {
    ImageView profilePic;
    Uri selectedImageUri;
    ActivityResultLauncher<Intent> imagePickLauncher;
    private UserViewModel userViewModel;
    private RecyclerView recyclerView;
    private InterestsAdapter interestsAdapter;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView2;
    private PhotosAdapter photosAdapter;
    private List<String> interestsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();
        loadProfileImage();
        updateDescription(currentUserId);
        updateInterests(currentUserId);
        retrieveImagesFromStorage(currentUserId);

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
        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            uploadPhoto(selectedImageUri);
                            }
                        }

                });
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

        Button editDescriptionButton = findViewById(R.id.button_edit_description);
        editDescriptionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, DescriptionActivity.class);
            startActivity(intent);

        });
        Button editInterestsButton = findViewById(R.id.button_edit_interests);
        editInterestsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditPreferencesActivity.class);
            startActivity(intent);

        });

        Button editPhotosButton = findViewById(R.id.button_edit_photos);
        editPhotosButton.setOnClickListener(v -> {

            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                    .createIntent(intent -> {
                        imagePickerLauncher.launch(intent);
                        return null;     });
        });
    }

    private void retrieveImagesFromStorage(String currentUserId) {
        List<Uri> photoUrls = new ArrayList<>();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("pictures").child(currentUserId);

        storageRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    if (!uri.toString().contains("profilePic.jpg")) {
                        photoUrls.add(uri);
                        photosAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(exception -> {
                });
            }
        }).addOnFailureListener(e -> {
        });

        recyclerView2 = findViewById(R.id.recyclerPhotos);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        photosAdapter = new PhotosAdapter(this, photoUrls);
        recyclerView2.setAdapter(photosAdapter);
    }


    private void uploadPhoto(Uri imageUri) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference userPhotosRef = storageRef.child("pictures").child(currentUserId);

        if (imageUri != null) {
            StorageReference photoRef = userPhotosRef.child(imageUri.getLastPathSegment());

            userViewModel.uploadImage(imageUri, photoRef);
        }
    }
    private void updateInterests(String currentUserId) {
        recyclerView = findViewById(R.id.recyclerInterests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        interestsAdapter = new InterestsAdapter(interestsList);
        recyclerView.setAdapter(interestsAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("preferences");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                interestsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String interest = snapshot.getValue(String.class);
                    interestsList.add(interest);
                }
                interestsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void updateDescription(String currentUserId) {
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(getApplication());
        userViewModel = new ViewModelProvider(
                this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);
       TextView descriptionTextView = findViewById(R.id.descriptionTextView);

        userViewModel.fetchUserDescription(currentUserId);

        userViewModel.getUserDescriptionLiveData().observe(this, description -> {
            if (description != null) {
                descriptionTextView.setText(description);
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
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(getApplication());
        userViewModel = new ViewModelProvider(
                this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference userRef = storageRef.child("pictures").child(currentUserId).child("profilePic.jpg");
        userViewModel.uploadImage(selectedImageUri, userRef);

        userViewModel.getImageUrlLiveData().observe(this, imageUrl -> {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                CircularProgressDrawable drawable = new CircularProgressDrawable(this);
                drawable.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
                drawable.setCenterRadius(30f);
                drawable.setStrokeWidth(5f);
                drawable.start();

                Glide.with(this)
                        .load(imageUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .placeholder(drawable)
                        .error(drawable)
                        .into(profilePic);
            }
        });
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
        updateInterests(currentUserId);
        retrieveImagesFromStorage(currentUserId);

    }
}
