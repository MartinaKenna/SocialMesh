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
        View vieww = inflater.inflate(R.layout.preferences_fragment, container, false);
        culture = vieww.findViewById(R.id.button_culture);
        sport = vieww.findViewById(R.id.button_sport);
        cinema = vieww.findViewById(R.id.button_cinema);
        aperitivo = vieww.findViewById(R.id.button_aperitivo);
        party = vieww.findViewById(R.id.button_party);
        smoker = vieww.findViewById(R.id.button_smoker);
        traveler = vieww.findViewById(R.id.button_traveler);
        lgbt = vieww.findViewById(R.id.button_lgbt);
        karaoke = vieww.findViewById(R.id.button_karaoke);
        nft = vieww.findViewById(R.id.button_nft);
        boxe = vieww.findViewById(R.id.button_boxe);
        festival = vieww.findViewById(R.id.button_festival);
        crossfit = vieww.findViewById(R.id.button_crossfit);
        nature = vieww.findViewById(R.id.button_nature);
        beach = vieww.findViewById(R.id.button_beach);
        motosport = vieww.findViewById(R.id.button_motosport);
        instagram = vieww.findViewById(R.id.button_instagram);
        twitter = vieww.findViewById(R.id.button_twitter);
        socialmedia = vieww.findViewById(R.id.button_socialmedia);
        horror = vieww.findViewById(R.id.button_horror);
        action = vieww.findViewById(R.id.button_action);
        love = vieww.findViewById(R.id.button_love);
        cooking = vieww.findViewById(R.id.button_cooking);
        photography = vieww.findViewById(R.id.button_photography);
        painting = vieww.findViewById(R.id.button_painting);
        hiking = vieww.findViewById(R.id.button_hiking);
        streaming = vieww.findViewById(R.id.button_streaming);
        gardening = vieww.findViewById(R.id.button_gardening);
        programming = vieww.findViewById(R.id.button_programming);
        writing = vieww.findViewById(R.id.button_writing);
        modelling = vieww.findViewById(R.id.button_modeling);
        vegetarian = vieww.findViewById(R.id.button_vegetarian);
        vegan = vieww.findViewById(R.id.button_vegan);
        carnivore = vieww.findViewById(R.id.button_carnivore);
        parent = vieww.findViewById(R.id.button_parent);
        single = vieww.findViewById(R.id.button_single);
        married = vieww.findViewById(R.id.button_married);
        engaged = vieww.findViewById(R.id.button_engaged);
        city = vieww.findViewById(R.id.button_city);
        navigate = vieww.findViewById(R.id.button_navigate);
        blogging = vieww.findViewById(R.id.button_blogging);


        navigate.setOnClickListener(v -> {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            List<String> preferences = new ArrayList<>();

            if (culture.isSelected()) {
                preferences.add("Culture");
            }
            if (sport.isSelected()) {
                preferences.add("Sport");
            }
            if (cinema.isSelected()) {
                preferences.add("Cinema");
            }
            if (aperitivo.isSelected()) {
                preferences.add("Aperitivo");
            }
            if (party.isSelected()) {
                preferences.add("Party");
            }
            if (smoker.isSelected()) {
                preferences.add("Smoker");
            }
            if (traveler.isSelected()) {
                preferences.add("Traveler");
            }
            if (lgbt.isSelected()) {
                preferences.add("LGBT");
            }
            if (karaoke.isSelected()) {
                preferences.add("Karaoke");
            }
            if (nft.isSelected()) {
                preferences.add("NFT");
            }
            if (boxe.isSelected()) {
                preferences.add("Boxe");
            }
            if (festival.isSelected()) {
                preferences.add("Festival");
            }
            if (crossfit.isSelected()) {
                preferences.add("Crossfit");
            }
            if (nature.isSelected()) {
                preferences.add("Nature");
            }
            if (beach.isSelected()) {
                preferences.add("Beach");
            }
            if (motosport.isSelected()) {
                preferences.add("Motosport");
            }
            if (instagram.isSelected()) {
                preferences.add("Instagram");
            }
            if (twitter.isSelected()) {
                preferences.add("Twitter");
            }
            if (socialmedia.isSelected()) {
                preferences.add("Social Media");
            }
            if (horror.isSelected()) {
                preferences.add("Horror");
            }
            if (action.isSelected()) {
                preferences.add("Action");
            }
            if (love.isSelected()) {
                preferences.add("Love");
            }
            if (cooking.isSelected()) {
                preferences.add("Cooking");
            }
            if (photography.isSelected()) {
                preferences.add("Photography");
            }
            if (painting.isSelected()) {
                preferences.add("Painting");
            }
            if (hiking.isSelected()) {
                preferences.add("Hiking");
            }
            if (streaming.isSelected()) {
                preferences.add("Streaming");
            }
            if (gardening.isSelected()) {
                preferences.add("Gardening");
            }
            if (programming.isSelected()) {
                preferences.add("Programming");
            }
            if (writing.isSelected()) {
                preferences.add("Writing");
            }
            if (modelling.isSelected()) {
                preferences.add("Modelling");
            }
            if (vegetarian.isSelected()) {
                preferences.add("Vegetarian");
            }
            if (vegan.isSelected()) {
                preferences.add("Vegan");
            }
            if (carnivore.isSelected()) {
                preferences.add("Carnivore");
            }
            if (parent.isSelected()) {
                preferences.add("Parent");
            }
            if (single.isSelected()) {
                preferences.add("Single");
            }
            if (married.isSelected()) {
                preferences.add("Married");
            }
            if (engaged.isSelected()) {
                preferences.add("Engaged");
            }
            if (city.isSelected()) {
                preferences.add("City");
            }
            if (navigate.isSelected()) {
                preferences.add("Navigate");
            }
            if (blogging.isSelected()) {
                preferences.add("Blogging");
            }


            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("preferences");
            userRef.setValue(preferences)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Preferenze salvate con successo!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Errore nel salvataggio delle preferenze", Toast.LENGTH_SHORT).show();
                        }
                    });
        });



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