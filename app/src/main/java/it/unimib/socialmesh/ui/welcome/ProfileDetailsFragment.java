package it.unimib.socialmesh.ui.welcome;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

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

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.data.repository.user.UserResponseCallback;
import it.unimib.socialmesh.model.User;

public class ProfileDetailsFragment extends Fragment {

    protected UserResponseCallback userResponseCallback;
    private UserViewModel userViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_details, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            User user = bundle.getParcelable("user");
            if(user !=null) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                Button saveButton = view.findViewById(R.id.buttonRegister);
                TextInputLayout fullNameEditText = view.findViewById(R.id.fullName);
                TextInputLayout dateTextInput = view.findViewById(R.id.datanasc);
                TextInputEditText dateInputEditText = view.findViewById(R.id.testo_datanasc);
                dateInputEditText.setOnClickListener(v -> {
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            this.getContext(),
                            R.style.ThemeOverlay_App_Dialog,
                            (view2, year1, monthOfYear, dayOfMonth) -> {
                                dateInputEditText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
                            },
                            year, month, day);
                    datePickerDialog.show();
                });

                saveButton.setOnClickListener(v -> {
                    String fullName = fullNameEditText.getEditText().getText().toString().trim();
                    String birthDate = dateTextInput.getEditText().getText().toString().trim();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference usersRef = database.getReference().child("users");
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                    usersRef.child(firebaseUser.getUid()).child("name").setValue(fullName).addOnCompleteListener(setValueTask -> {
                        if (setValueTask.isSuccessful()) {
                            userResponseCallback.onSuccessFromAuthentication(user);
                        } else {
                            userResponseCallback.onFailureFromAuthentication("Errore nel salvataggio del nome dell'utente.");
                        }
                    });
                    usersRef.child(firebaseUser.getUid()).child("data_di_nascita").setValue(birthDate).addOnCompleteListener(setValueTask -> {
                        if (setValueTask.isSuccessful()) {
                            userResponseCallback.onSuccessFromAuthentication(user);
                        } else {
                            userResponseCallback.onFailureFromAuthentication("Errore nel salvataggio del nome dell'utente.");
                        }
                    });
                    Navigation.findNavController(requireView()).navigate(R.id.navigate_to_homeActivity);
                });

                userViewModel.getProfileFullName().observe(getViewLifecycleOwner(), fullName -> {
                    fullNameEditText.getEditText().setText(fullName);
                });

                userViewModel.getProfileBirthDate().observe(getViewLifecycleOwner(), birthDate -> {
                    dateTextInput.getEditText().setText(birthDate);
                });

                }
            }


        return view;
    }
}
