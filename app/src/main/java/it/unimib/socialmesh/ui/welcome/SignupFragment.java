package it.unimib.socialmesh.ui.welcome;

import static it.unimib.socialmesh.util.Constants.USER_COLLISION_ERROR;
import static it.unimib.socialmesh.util.Constants.WEAK_PASSWORD_ERROR;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.Result;
import it.unimib.socialmesh.model.User;
import it.unimib.socialmesh.ui.main.HomeActivity;

public class SignupFragment extends Fragment {
    TextInputLayout emailTextInput, passTextInput, confirmPassTextInput;

    private UserViewModel userViewModel;

    Button register;
    Intent intent;

    protected static final int LIMIT_AGE = 16;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setAuthenticationError(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_fragment, container, false);
        emailTextInput = root.findViewById(R.id.email);
        passTextInput = root.findViewById(R.id.insertPassword);
        confirmPassTextInput = root.findViewById(R.id.confirmpassword_signup);
        register = root.findViewById(R.id.buttonRegister);


        emailTextInput.setTranslationX(1000);
        passTextInput.setTranslationX(1000);
        confirmPassTextInput.setTranslationX(1000);
        register.setTranslationX(1000);


        emailTextInput.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        passTextInput.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        confirmPassTextInput.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1100).start();
        register.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1300).start();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        register.setOnClickListener(v -> {

            String email = emailTextInput.getEditText().getText().toString().trim();
            String password = passTextInput.getEditText().getText().toString().trim();
            String confirmPassword = confirmPassTextInput.getEditText().getText().toString().trim();

            if (true) {
                if (!userViewModel.isAuthenticationError()) {
                    userViewModel.getUserMutableLiveData(email, password, false).observe(
                            getViewLifecycleOwner(), result -> {
                                if (result.isSuccess()) {
                                    User user = ((Result.UserResponseSuccess) result).getData();
                                    userViewModel.setAuthenticationError(false);
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("user", user);
                                    Navigation.findNavController(view).navigate(
                                            R.id.navigate_to_detailsFragment, bundle);
                                } else {
                                    userViewModel.setAuthenticationError(true);
                                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                            getErrorMessage(((Result.Error) result).getMessage()),
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    userViewModel.getUser(email, password, false);
                }
            } else {
                userViewModel.setAuthenticationError(true);
                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                        R.string.check_login_data_message, Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    private String getErrorMessage(String message) {
        switch(message) {
            case WEAK_PASSWORD_ERROR:
                return requireActivity().getString(R.string.error_password);
            case USER_COLLISION_ERROR:
                return requireActivity().getString(R.string.error_user_collision_message);
            default:
                return requireActivity().getString(R.string.unexpected_error);
        }
    }


    private boolean validFields(String fullName, String date, String email, String password, String password2) {
        if (!EmailValidator.getInstance().isValid((email))) {
            emailTextInput.setError(getString(R.string.error_email));
            return false;
        } else {
            emailTextInput.setError(null);
        }  if (password == null) {
            passTextInput.setError(getString(R.string.error_password_invalid));
        }

        // Regex to check valid password.
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!()_%])(?=\\S+$).{8,20}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);

        boolean match = m.matches();

        // If password matches pattern return true
        if(!match) {
            passTextInput.setError(getString(R.string.error_password_invalid));
            return false;
        } else {
            passTextInput.setError(null);

        }
        boolean match1 = password2.equals(password2);

        if(!match1) {
            passTextInput.setError(getString(R.string.error_password_mismatch));
            confirmPassTextInput.setError(getString(R.string.error_password_mismatch));
            return false;
        } else {
            passTextInput.setError(null);
            confirmPassTextInput.setError(null);

        }
        return true;
        }
}