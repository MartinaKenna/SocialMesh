package it.unimib.socialmesh.ui.welcome;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.User;
import it.unimib.socialmesh.ui.main.HomeActivity;

public class NON_USARLOSignupTabFragment extends Fragment {
    TextInputLayout nome, datanasc, emailTextInput, passTextInput, confirmpass;
    private Button register, culture, sport, cinema, aperitivo, party, smoker, traveler, lgbt,
                    karaoke, nft, boxe, festival, crossfit, nature, beach, motosport, instagram,
                    twitter, socialmedia, horror, action, love, cooking, photography, painting,
                    hiking, streaming, gardening, writing, modelling, programming, vegetarian, vegan,
                    carnivore, parent, single, engaged, married, navigate, blogging, city;
    Intent intent;
    private TextInputEditText dateInputText;

    protected static final int LIMIT_AGE = 16;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_fragment, container, false);

        nome = root.findViewById(R.id.fullName);
        datanasc = root.findViewById(R.id.datanasc);
        dateInputText = root.findViewById(R.id.testo_datanasc);
        emailTextInput = root.findViewById(R.id.email);
        passTextInput = root.findViewById(R.id.insertPassword);
        confirmpass = root.findViewById(R.id.confirmpassword_signup);
        register = root.findViewById(R.id.buttonRegister);

        //configuro le preferenze




        dateInputText.setOnClickListener(v -> {
            // on below line we are getting
            // the instance of our calendar.
            final Calendar c = Calendar.getInstance();

            // on below line we are getting
            // our day, month and year.
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // on below line we are creating a variable for date picker dialog.
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    // on below line we are passing context.
                    this.getContext(),
                    R.style.ThemeOverlay_App_Dialog,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        // on below line we are setting date to our edit text.
                        dateInputText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);

                    },
                    // on below line we are passing year,
                    // month and day for selected date in our date picker.
                    year, month, day);
            // at last we are calling show to
            // display our date picker dialog.
            datePickerDialog.show();
        });



        register.setOnClickListener(v -> {
            // Esegui azioni quando il pulsante di registrazione viene cliccato
            registerUser();
        });
        nome.setTranslationX(1000);
        datanasc.setTranslationX(1000);
        emailTextInput.setTranslationX(1000);
        passTextInput.setTranslationX(1000);
        confirmpass.setTranslationX(1000);
        register.setTranslationX(1000);

        nome.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        datanasc.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        emailTextInput.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        passTextInput.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        confirmpass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1100).start();
        register.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1300).start();
        return root;
    }



    private void registerUser() {
        // Ottieni i valori dalle TextInputEditText
        String fullName = nome.getEditText().getText().toString();
        String email = emailTextInput.getEditText().getText().toString();
        String password = passTextInput.getEditText().getText().toString();
        String password2 = confirmpass.getEditText().getText().toString();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Perfavore completa tutti i campi", Toast.LENGTH_LONG).show();
            return;
        }

    }


    private boolean validFields(String fullName, String email, String password, String password2, TextInputLayout datanasc) {

            if (fullName == null || !fullName.matches("^[a-zA-Z]+(\\s[a-zA-Z]+)?$")) {
                nome.setError(getString(R.string.fullname_error));
                return false;
            } else {
                nome.setError(null);
            }
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
            confirmpass.setError(getString(R.string.error_password_mismatch));
            return false;
        } else {
            passTextInput.setError(null);
            confirmpass.setError(null);

        }
        return true;
        }


    private void navigateToSecondActivity() {
        // Intent per navigare alla HomeActivity
        if (getActivity() != null) {
            intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
        }

    }
}