package it.unimib.socialmesh.ui.welcome;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import it.unimib.socialmesh.data.repository.user.IUserRepository;
import it.unimib.socialmesh.model.Result;
import it.unimib.socialmesh.model.User;

public class UserViewModel extends ViewModel {
    private static final String TAG = UserViewModel.class.getSimpleName();

    private final IUserRepository userRepository;
    DatabaseReference userPreferencesRef;
    private MutableLiveData<Result> userMutableLiveData;
    private MutableLiveData<String> profileFullName = new MutableLiveData<>();
    private MutableLiveData<String> profileBirthDate = new MutableLiveData<>();
    private MutableLiveData<List<String>> userPreferencesLiveData = new MutableLiveData<>();
    private MutableLiveData<String> imageUrlLiveData = new MutableLiveData<>();
    private MutableLiveData<String> userDescription = new MutableLiveData<>();

    private boolean authenticationError;

    public UserViewModel(IUserRepository userRepository) {
        this.userRepository = userRepository;
        authenticationError = false;
    }

    public MutableLiveData<Result> getUserMutableLiveData(
            String email, String password, boolean isUserRegistered) {
        if (userMutableLiveData == null) {
            getUserData(email, password, isUserRegistered);
        }
        return userMutableLiveData;
    }

    public MutableLiveData<Result> getGoogleUserMutableLiveData(String token) {
        if (userMutableLiveData == null) {
            getUserData(token);
        }
        return userMutableLiveData;
    }

    public MutableLiveData<String> getProfileFullName() {
        return profileFullName;
    }

    // Getter per il LiveData della data di nascita
    public MutableLiveData<String> getProfileBirthDate() {
        return profileBirthDate;
    }
    public MutableLiveData<List<String>> getUserPreferencesLiveData() {
        return userPreferencesLiveData;
    }public void addPreference(String preference) {
        List<String> currentPreferences = userPreferencesLiveData.getValue();
        if (currentPreferences == null) {
            currentPreferences = new ArrayList<>();
        }
        currentPreferences.add(preference);
        userPreferencesLiveData.setValue(currentPreferences);
        savePreferencesToFirebase(currentPreferences);
    }

    private void savePreferencesToFirebase(List<String> currentPreferences) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userPreferencesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("preferences");
        userPreferencesRef.setValue(currentPreferences)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("ViewModel","OK");
                        // Toast.makeText(getContext(), "Preferenza salvata con successo!", Toast.LENGTH_SHORT).show();
                    } else {
                       // Toast.makeText(getContext(), "Errore nel salvataggio della preferenza", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public MutableLiveData<String> getImageUrlLiveData() {
        return imageUrlLiveData;
    }

    public void setImageUrl(String imageUrl) {
        imageUrlLiveData.setValue(imageUrl);
    }
    public void uploadImage(Uri selectedImageUri, StorageReference userRef) {
        if (selectedImageUri != null) {
            UploadTask uploadTask = userRef.putFile(selectedImageUri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                userRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageURL = uri.toString();
                    setImageUrl(imageURL);
                });
            }).addOnFailureListener(exception -> {
            });
        }
    }
    public MutableLiveData<String> getProfileImageUrl(String userId) {
         StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference userRef = storageRef.child("pictures").child(userId).child("profilePic.jpg");
        userRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageURL = uri.toString();
            imageUrlLiveData.setValue(imageURL);
        }).addOnFailureListener(exception -> {
            Log.e("Settings", "Errore durante il caricamento dell'immagine del profilo: " + exception.getMessage());
        });

        return imageUrlLiveData;
    }
    public MutableLiveData<String> getUserDescriptionLiveData() {
        return userDescription;
    }

    public User getLoggedUser() {
        return userRepository.getLoggedUser();
    }
    public void saveDescription(String description) {
        if (!description.isEmpty()) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userDescriptionRef = FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(userId)
                    .child("descrizione");
            userDescription.setValue(description);
            userDescription.observeForever(newDescription -> userDescriptionRef.setValue(newDescription)
                    .addOnSuccessListener(aVoid -> {
                    })
                    .addOnFailureListener(e -> {
                    }));
        } else {
            //Toast.makeText(this, "La descrizione non può essere vuota", Toast.LENGTH_SHORT).show();
        }
    }
    public void fetchUserDescription(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userId);
        userRef.child("descrizione").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String description = snapshot.getValue(String.class);
                    userDescription.setValue(description);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestire eventuali errori
            }
        });
    }

    public MutableLiveData<Result> logout() {
        if (userMutableLiveData == null) {
            userMutableLiveData = userRepository.logout();
        } else {
            userRepository.logout();
        }

        return userMutableLiveData;
    }

    public void getUser(String email, String password, boolean isUserRegistered) {
        userRepository.getUser(email, password, isUserRegistered);
    }

    public boolean isAuthenticationError() {
        return authenticationError;
    }

    public void setAuthenticationError(boolean authenticationError) {
        this.authenticationError = authenticationError;
    }

    private void getUserData(String email, String password, boolean isUserRegistered) {
        userMutableLiveData = userRepository.getUser(email, password, isUserRegistered);
    }

    private void getUserData(String token) {
        userMutableLiveData = userRepository.getGoogleUser(token);
    }
}
