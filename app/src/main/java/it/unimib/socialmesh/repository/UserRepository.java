package it.unimib.socialmesh.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRepository {
    private static UserRepository instance;
    private DatabaseReference databaseReference; // Riferimento al nodo dell'utente

    public UserRepository() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        }
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public Task<Void> setUserProfileImageUrl(String imageUrl) {
        return databaseReference.child("profileImageUrl").setValue(imageUrl);
    }

    public Task<DataSnapshot> getUserProfileImageUrl() {
        return databaseReference.child("profileImageUrl").get();
    }
}