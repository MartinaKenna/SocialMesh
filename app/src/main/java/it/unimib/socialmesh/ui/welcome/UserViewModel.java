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
import it.unimib.socialmesh.util.FireBaseUtil;

public class UserViewModel extends ViewModel {
    private static final String TAG = UserViewModel.class.getSimpleName();

    private final IUserRepository userRepository;
    DatabaseReference userPreferencesRef;
    private MutableLiveData<Result> userMutableLiveData;
    private MutableLiveData<String> profileFullName = new MutableLiveData<>();
    private MutableLiveData<String> profileBirthDate = new MutableLiveData<>();
    private final MutableLiveData<String> emailLiveData = new MutableLiveData<>();
    private MutableLiveData<List<String>> userPreferencesLiveData = new MutableLiveData<>();
    private MutableLiveData<String> imageUrlLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Uri>> photosUrlLiveData = new MutableLiveData<>();
    private MutableLiveData<String> userDescription = new MutableLiveData<>();
    private MutableLiveData<List<String>> interestsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> matchLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> likeLiveData = new MutableLiveData<>();

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
    public MutableLiveData<List<String>> getUserInterestsLiveData() {
        return interestsLiveData;
    }
    private void savePreferencesToFirebase(List<String> currentPreferences) {
        String userId = FireBaseUtil.currentUserId();
        userPreferencesRef = FireBaseUtil.getUserRef(userId).child("preferences");
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
        StorageReference userRef = FireBaseUtil.getCurrentProfilePicStorageRef(userId).child("profilePic.jpg");
        userRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageURL = uri.toString();
            imageUrlLiveData.setValue(imageURL);
        }).addOnFailureListener(exception -> {
            Log.e("Settings", "Errore durante il caricamento dell'immagine del profilo: " + exception.getMessage());
        });

        return imageUrlLiveData;
    }

    public MutableLiveData<List<Uri>> getPhotosUrlLiveData() {
        return photosUrlLiveData;
    }

    public void retrieveUserImages(String userId) {
        List<Uri> photoUrls = new ArrayList<>();
        StorageReference storageRef = FireBaseUtil.getCurrentProfilePicStorageRef(userId);

        storageRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    if (uri.toString().contains("profilePic.jpg")) {
                        photoUrls.add(0, uri);
                    } else {
                        photoUrls.add(uri);
                    }
                    photosUrlLiveData.setValue(photoUrls);
                }).addOnFailureListener(exception -> {
                });
            }
        }).addOnFailureListener(e -> {
        });
    }

    public MutableLiveData<String> getUserDescriptionLiveData() {
        return userDescription;
    }

    public User getLoggedUser() {
        return userRepository.getLoggedUser();
    }
    public void saveDescription(String description) {
        if (!description.isEmpty()) {
            String userId = FireBaseUtil.currentUserId();
            DatabaseReference userDescriptionRef = FireBaseUtil.getUserRef(userId)
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
        DatabaseReference userRef = FireBaseUtil.getUserRef(userId)
                .child("descrizione");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        userRef.setValue("");
                        userDescription.setValue("");
                    } else {
                        String description = snapshot.getValue(String.class);
                        userDescription.setValue(description);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
    public void updateInterests(String userId) {
        DatabaseReference databaseReference =FireBaseUtil.getUserRef(userId)
                .child("preferences");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> interestsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String interest = snapshot.getValue(String.class);
                    interestsList.add(interest);
                }
                interestsLiveData.setValue(interestsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public MutableLiveData<String> getEmailLiveData() {
        return emailLiveData;
    }

    public MutableLiveData<Result> logout() {
        if (userMutableLiveData == null) {
            userMutableLiveData = userRepository.logout();
        } else {
            userRepository.logout();
        }

        return userMutableLiveData;
    }
    public void obtainUserData(String userId) {
        DatabaseReference userRef = FireBaseUtil.getUserRef(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot nameSnapshot = snapshot.child("name");
                    DataSnapshot emailSnapshot = snapshot.child("email");
                    DataSnapshot dateSnapshot = snapshot.child("data_di_nascita");

                    String name = nameSnapshot.exists() ? nameSnapshot.getValue(String.class) : "";
                    String email = emailSnapshot.exists() ? emailSnapshot.getValue(String.class) : "";
                    String birthDate = dateSnapshot.exists() ? dateSnapshot.getValue(String.class) : "";


                    profileFullName.setValue(name);
                    emailLiveData.setValue(email);
                    profileBirthDate.setValue(birthDate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestione errori
            }
        });
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
    private void notifyMatch() {
        matchLiveData.setValue(true);
    }

    public void addLikeAndCheckForMatch(String currentUserID, String likedUserID) {
        DatabaseReference currentUserLikesRef = FireBaseUtil.getUserRef(currentUserID).child("likes");
        likeLiveData.setValue(true);
        currentUserLikesRef.child(likedUserID).setValue(true);
        checkForMatch(currentUserID, likedUserID);
    }

    public MutableLiveData<Boolean> getMatchLiveData() {
        return matchLiveData;
    }

    private void checkForMatch(final String currentUserID, final String otherUserID) {
        DatabaseReference otherUserLikesRef = FireBaseUtil.getUserRef(otherUserID).child("likes");
        otherUserLikesRef.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && Boolean.TRUE.equals(dataSnapshot.getValue())) {
                    createMatch(currentUserID, otherUserID);
                    //sendMatchNotificationToUsers(currentUserID, otherUserID);
                    notifyMatch();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void createMatch(String currentUserID, String matchedUserID) {
        DatabaseReference currentUserMatchesRef = FireBaseUtil.getUserRef(currentUserID).child("matches");
        DatabaseReference matchedUserMatchesRef = FireBaseUtil.getUserRef(matchedUserID).child("matches");

        currentUserMatchesRef.child(matchedUserID).setValue(true);
        matchedUserMatchesRef.child(currentUserID).setValue(true);
    }
    public MutableLiveData<Boolean> getLikeLiveData() {
        return likeLiveData;
    }


    public void removeLike(String currentUserID, String otherUserID) {
        DatabaseReference currentUserLikesRef = FireBaseUtil.getUserRef(currentUserID).child("likes").child(otherUserID);

        currentUserLikesRef.removeValue();

        DatabaseReference otherUserLikesRef = FireBaseUtil.getUserRef(otherUserID).child("likes").child(currentUserID);

        otherUserLikesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    removeMatch(currentUserID, otherUserID);
                    likeLiveData.setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestire eventuali errori
            }
        });
    }

    private void removeMatch(String currentUserID, String otherUserID) {
        DatabaseReference currentUserMatchesRef = FireBaseUtil.getUserRef(currentUserID).child("matches").child(otherUserID);

        currentUserMatchesRef.removeValue();

        DatabaseReference matchedUserMatchesRef =FireBaseUtil.getUserRef(otherUserID)
                .child("matches")
                .child(currentUserID);

        matchedUserMatchesRef.removeValue();
    }

}

