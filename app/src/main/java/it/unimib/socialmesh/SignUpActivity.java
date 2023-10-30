package it.unimib.socialmesh;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private TextInputLayout textInputLayoutFullName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        textInputLayoutFullName = findViewById(R.id.textInputLayoutFullName);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        final Button signupLogin = findViewById(R.id.button_signup);

        signupLogin.setOnClickListener(v -> {

            String email = textInputLayoutEmail.getEditText().getText().toString();
            String password = textInputLayoutPassword.getEditText().getText().toString();

            // Start login if email and password are ok
            if (isEmailOk(email) & isPasswordOk(password)) {
                //TODO implementare salvataggio dati
                //saveLoginData(email, password);
            } else {
//                Snackbar.make(findViewById(android.R.id.content),
//                        R.string.check_login_data_message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Checks if the email address has a correct format.
     * @param email The email address to be validated
     * @return true if the email address is valid, false otherwise
     */
    private boolean isEmailOk(String email) {
        // Check if the email is valid through the use of this library:
        // https://commons.apache.org/proper/commons-validator/
        if (!EmailValidator.getInstance().isValid((email))) {
            textInputLayoutEmail.setError(getString(R.string.error_email));
            return false;
        } else {
            textInputLayoutEmail.setError(null);
            return true;
        }
    }

    /**
     * Checks if the password is usable.
     * Found at https://www.geeksforgeeks.org/how-to-validate-a-password-using-regular-expressions-in-java/
     * Password is accepted if all the following constraints are satisfied:
     * 1. It contains at least 8 characters and at most 20 characters
     * 2. It contains at least one digit
     * 3. It contains at least one upper case alphabet
     * 4. It contains at least one lower case alphabet
     * 5. It contains at least one special character which includes !@#$%&*()-+=^
     * 5. It doesn’t contain any white space
     * @param password The password to be checked
     * @return True if the password is acceptable, false if not
     */
    private boolean isPasswordOk(String password) {

        // If the password is empty return false
        if (password == null) {
            textInputLayoutPassword.setError(getString(R.string.error_password));
            return false;
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
            textInputLayoutPassword.setError(getString(R.string.error_password));
            return false;
        } else {
            textInputLayoutPassword.setError(null);
            return true;
        }
    }
}