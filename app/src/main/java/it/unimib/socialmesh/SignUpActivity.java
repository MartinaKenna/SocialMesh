package it.unimib.socialmesh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private TextInputLayout textInputLayoutFullName;
    private TextInputEditText dateInputText;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutPasswordConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        textInputLayoutFullName = findViewById(R.id.textInputLayoutFullName);
        dateInputText = findViewById(R.id.dateInputText);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutPasswordConfirm = findViewById(R.id.textInputLayoutPasswordRetype);
        final Button signupLogin = findViewById(R.id.signupButton);

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
                    SignUpActivity.this,
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

        signupLogin.setOnClickListener(v -> {

            String email = textInputLayoutEmail.getEditText().getText().toString();
            String password1 = textInputLayoutPassword.getEditText().getText().toString();
            String password2 = textInputLayoutPasswordConfirm.getEditText().getText().toString();

            textInputLayoutEmail.setError(null);
            textInputLayoutPassword.setError(null);
            textInputLayoutPasswordConfirm.setError(null);

            // Start login if email and password are ok
            if (isEmailOk(email) & isPasswordOk(password1) && passwordsMatch(password1, password2)) {

                textInputLayoutEmail.setError(null);
                textInputLayoutPassword.setError(null);
                textInputLayoutPasswordConfirm.setError(null);

                //TODO registrazione firebase

            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        R.string.check_login_data_message, Snackbar.LENGTH_SHORT).show();
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
     * 5. It contains at least one special character which includes @#$%^&+=!()_%
     * 5. It doesn’t contain any white space
     * @param password The password to be checked
     * @return True if the password is acceptable, false if not
     */
    private boolean isPasswordOk(String password) {

        // If the password is empty return false
        if (password == null) {
            textInputLayoutPassword.setError(getString(R.string.error_password_invalid));
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
            textInputLayoutPassword.setError(getString(R.string.error_password_invalid));
            return false;
        } else {
            textInputLayoutPassword.setError(null);
            return true;
        }
    }

    /**
     * Check if passwords matches (to avoid typing errors).
     * In case of mismatch an error is set on password text fields.
     * @param password1
     * @param password2
     * @return true if passwords match, false instead.
     */
    private boolean passwordsMatch(String password1, String password2) {

        boolean match = password1.equals(password2);

        if(!match) {
            textInputLayoutPassword.setError(getString(R.string.error_password_mismatch));
            textInputLayoutPasswordConfirm.setError(getString(R.string.error_password_mismatch));
            return false;
        } else {
            textInputLayoutPassword.setError(null);
            textInputLayoutPasswordConfirm.setError(null);
            return true;
        }
    }

    //static class for time picker
    private static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker.
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it.
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time the user picks.
        }
    }
}