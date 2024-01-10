package it.unimib.socialmesh.ui.welcome;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.data.repository.user.UserResponseCallback;
import it.unimib.socialmesh.databinding.FragmentProfileDetailsBinding;
import it.unimib.socialmesh.model.User;

public class  ProfileDetailsFragment extends Fragment {

    private UserResponseCallback userResponseCallback;
    private UserViewModel userViewModel;

    private FragmentProfileDetailsBinding fragmentProfileDetailsBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentProfileDetailsBinding = FragmentProfileDetailsBinding.inflate(inflater, container, false);
        return fragmentProfileDetailsBinding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);

        Bundle bundle = getArguments();
        if (bundle != null) {
            User user = bundle.getParcelable("user");
            Log.d("Profilo",user.toString());
            if(user !=null) {
                int nameOk, dateOk;
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                fragmentProfileDetailsBinding.testoDatanasc.setOnClickListener(v -> {
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            this.getContext(),
                            R.style.ThemeOverlay_App_Dialog,
                            (view2, year1, monthOfYear, dayOfMonth) -> {
                                fragmentProfileDetailsBinding.testoDatanasc.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
                            },
                            year, month, day);
                    datePickerDialog.show();
                });

                fragmentProfileDetailsBinding.buttonRegister.setOnClickListener(v -> {
                    String fullName = fragmentProfileDetailsBinding.fullName.getEditText().getText().toString().trim();
                    String birthDate = fragmentProfileDetailsBinding.datanasc.getEditText().getText().toString().trim();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference usersRef = database.getReference().child("users");
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                    usersRef.child(firebaseUser.getUid()).child("name").setValue(fullName).addOnCompleteListener(setValueTask -> {
                        if (setValueTask.isSuccessful()) {
                            Log.d("OK","PESO");
                        } else {
                            Log.d("NO","NONPESO");
                        }
                    });
                    usersRef.child(firebaseUser.getUid()).child("data_di_nascita").setValue(birthDate).addOnCompleteListener(setValueTask -> {
                        if (setValueTask.isSuccessful()) {
                            Log.d("OK","PESO");
                        } else {
                            Log.d("NO","NONPESO");
                        }
                    });

                    Navigation.findNavController(requireView()).navigate(R.id.action_profileDetailsFragment_to_preferencesFragment);

                });



                userViewModel.getProfileFullName().observe(getViewLifecycleOwner(), fullName -> {
                    fragmentProfileDetailsBinding.fullName.getEditText().setText(fullName);
                });

                userViewModel.getProfileBirthDate().observe(getViewLifecycleOwner(), birthDate -> {
                    fragmentProfileDetailsBinding.datanasc.getEditText().setText(birthDate);
                });

            }
        }
    }
}