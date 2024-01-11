package it.unimib.socialmesh.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.data.repository.user.IUserRepository;
import it.unimib.socialmesh.databinding.FragmentProfileBinding;
import it.unimib.socialmesh.ui.welcome.UserViewModel;
import it.unimib.socialmesh.ui.welcome.UserViewModelFactory;
import it.unimib.socialmesh.util.FireBaseUtil;
import it.unimib.socialmesh.util.ServiceLocator;

public class ProfileFragment extends Fragment {
    ImageView profile_image_view;
    private UserViewModel userViewModel;

    private FragmentProfileBinding fragmentProfileBinding;

    public ProfileFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false);

        profile_image_view = fragmentProfileBinding.profileImageView;
        String userId = FireBaseUtil.currentUserId();
        userViewModel.obtainUserData(userId);

        fragmentProfileBinding.buttonSettings.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), SettingsActivity.class);
            settingsLauncher.launch(intent);
        });

        userViewModel.getProfileFullName().observe(getViewLifecycleOwner(), name -> {
            fragmentProfileBinding.userName.setText(name);
        });

        userViewModel.getEmailLiveData().observe(getViewLifecycleOwner(), email -> {
            fragmentProfileBinding.userEmail.setText(email);
        });

        userViewModel.getProfileBirthDate().observe(getViewLifecycleOwner(), birthDate -> {
            fragmentProfileBinding.userDate.setText(birthDate);
        });
        return fragmentProfileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadProfileImage();
        fragmentProfileBinding.buttonLogout.setOnClickListener(v -> {
            /*
            userViewModel.logout().observe(getViewLifecycleOwner(), result -> {
                if (true) {
                    Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_welcomeActivity);
                } else {
                    Snackbar.make(view,
                            requireActivity().getString(R.string.unexpected_error),
                            Snackbar.LENGTH_SHORT).show();
                }
            });
             */
            userViewModel.logout();
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_welcomeActivity);
        });
    }
    private void loadProfileImage() {
        String currentUserId =FireBaseUtil.currentUserId();
        userViewModel.getProfileImageUrl(currentUserId).observe(getViewLifecycleOwner(), imageUrl -> {
            if (imageUrl != null && !imageUrl.isEmpty()) {
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
            } else {
                Glide.with(this)
                        .load(com.facebook.R.drawable.com_facebook_profile_picture_blank_portrait) // Immagine di default
                        .apply(RequestOptions.circleCropTransform())
                        .into(profile_image_view);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        loadProfileImage();
    }
}
