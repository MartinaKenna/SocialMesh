package it.unimib.socialmesh.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.data.repository.user.IUserRepository;
import it.unimib.socialmesh.model.User;
import it.unimib.socialmesh.data.repository.user.UserRepository;
import it.unimib.socialmesh.ui.welcome.UserViewModel;
import it.unimib.socialmesh.ui.welcome.UserViewModelFactory;
import it.unimib.socialmesh.ui.welcome.WelcomeActivity;
import it.unimib.socialmesh.util.ServiceLocator;

public class ProfileFragment extends Fragment {
   int REQUEST_CODE;
    TextView fullName, userEmail,userDate;
    Button buttonLogout;
    ImageView profile_image_view;
    private UserViewModel userViewModel;

    public ProfileFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadProfileImage();
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(
                requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

    }
    ActivityResultLauncher<Intent> settingsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("image_url")) {
                        String imageUrl = data.getStringExtra("image_url");
                        CircularProgressDrawable drawable = new CircularProgressDrawable(getContext());
                        drawable.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
                        drawable.setCenterRadius(30f);
                        drawable.setStrokeWidth(5f);
                        drawable.start();
                        Glide.with(this)
                                .load(imageUrl)
                                .apply(RequestOptions.circleCropTransform())
                                .placeholder(drawable)
                                .error(drawable)
                                .into(profile_image_view);
                    }
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        ImageView profileImageView;
        fullName = root.findViewById(R.id.userName); // Aggiunto il riferimento alla TextView del nome
        userDate = root.findViewById(R.id.userDate);
        userEmail = root.findViewById(R.id.userEmail); // Aggiunto il riferimento alla TextView dell'email
        profile_image_view = root.findViewById(R.id.profile_image_view);
        buttonLogout = root.findViewById(R.id.buttonLogout);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        AppCompatImageView buttonSettings = root.findViewById(R.id.button_settings); // Assumi che l'ID corrisponda a un AppCompatImageView

        buttonSettings.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), SettingsActivity.class);
            settingsLauncher.launch(intent);
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot nameSnapshot = snapshot.child("name");
                    DataSnapshot emailSnapshot = snapshot.child("email");
                    DataSnapshot dateSnapshot = snapshot.child("data_di_nascita");

                    if (nameSnapshot.exists() && emailSnapshot.exists()) {
                        String name = nameSnapshot.getValue(String.class);
                        String email = emailSnapshot.getValue(String.class);
                        String data_di_nascita = dateSnapshot.getValue(String.class);
                        fullName.setText(name);
                        userEmail.setText(email);
                        userDate.setText(data_di_nascita);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonLogout.setOnClickListener(v -> {
            userViewModel.logout().observe(getViewLifecycleOwner(), result -> {
                if (true) {
                    Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_loginFragment);
                } else {
                    Snackbar.make(view,
                            requireActivity().getString(R.string.unexpected_error),
                            Snackbar.LENGTH_SHORT).show();
                }
            });
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
                CircularProgressDrawable drawable = new CircularProgressDrawable(getContext());
                drawable.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
                drawable.setCenterRadius(30f);
                drawable.setStrokeWidth(5f);
                drawable.start();
                    Glide.with(this)
                            .load(imageURL)
                            .apply(RequestOptions.circleCropTransform())
                            .placeholder(drawable)
                            .error(drawable)
                            .into(profile_image_view);

            } else {

                    Glide.with(this)
                            .load(com.facebook.R.drawable.com_facebook_profile_picture_blank_portrait) // Immagine di default
                            .apply(RequestOptions.circleCropTransform())
                            .into(profile_image_view);

            }
        }).addOnFailureListener(exception -> {
            // Gestisci eventuali errori durante il recupero dell'immagine del profilo
            Log.e("Settings", "Errore durante il caricamento dell'immagine del profilo: " + exception.getMessage());
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        loadProfileImage();
    }
}
