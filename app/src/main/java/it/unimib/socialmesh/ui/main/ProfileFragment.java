package it.unimib.socialmesh.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.User;
import it.unimib.socialmesh.repository.UserRepository;
import it.unimib.socialmesh.ui.welcome.LoginActivity;

public class ProfileFragment extends Fragment {
    int REQUEST_CODE;
    TextView fullName, userEmail;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;
    ImageView profile_image_view;
    UserRepository userRepository;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository();
        refreshProfileImage();

    }
    ActivityResultLauncher<Intent> settingsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("image_url")) {
                        String imageUrl = data.getStringExtra("image_url");
                        // Carica l'immagine nell'ImageView del profilo utilizzando l'URL
                        Glide.with(this)
                                .load(imageUrl)
                                .apply(RequestOptions.circleCropTransform())
                                .placeholder(R.drawable.baseline_lock_clock_black_24dp) // Immagine di caricamento
                                .error(R.drawable.baseline_error_black_24dp) // Immagine di errore
                                .into(profile_image_view); // profileImageView è l'ImageView del profilo
                    }
                }
            }
    );
    @Override
    public void onResume() {
        super.onResume();
        refreshProfileImage();
    }

    private void refreshProfileImage() { userRepository.getUserProfileImageUrl().addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
            DataSnapshot dataSnapshot = task.getResult();
            if (dataSnapshot != null && dataSnapshot.exists()) {
                String imageUrl = dataSnapshot.getValue(String.class);
                if (imageUrl != null && isAdded()) {
                    Glide.with(this)
                            .load(imageUrl)
                            .apply(RequestOptions.circleCropTransform())
                            .placeholder(R.drawable.baseline_lock_clock_black_24dp)
                            .error(R.drawable.baseline_error_black_24dp)
                            .into(profile_image_view);
                }
            }
        } else {
            // Gestisci eventuali errori durante il recupero dell'URL dell'immagine
        }
    });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_profile, container, false);
         ImageView profileImageView;
        fullName = root.findViewById(R.id.userName); // Aggiunto il riferimento alla TextView del nome
        userEmail = root.findViewById(R.id.userEmail); // Aggiunto il riferimento alla TextView dell'email
        profile_image_view = root.findViewById(R.id.profile_image_view);
        Button buttonLogout = root.findViewById(R.id.buttonLogout);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null && getActivity() != null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        buttonLogout.setOnClickListener(view -> logoutUser());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    fullName.setText(user.fullName);
                    userEmail.setText(user.email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestione dell'errore
            }
        });
        AppCompatImageView buttonSettings = root.findViewById(R.id.button_settings); // Assumi che l'ID corrisponda a un AppCompatImageView

        buttonSettings.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), SettingsActivity.class);
            settingsLauncher.launch(intent);
        });
        return root;
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
