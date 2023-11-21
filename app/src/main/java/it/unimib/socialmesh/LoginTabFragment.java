package it.unimib.socialmesh;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginTabFragment extends Fragment {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private TextInputLayout passTextInput, emailTextInput;
    CallbackManager callbackManager;
    TabLayout tabLayout;
    TextView forget_password;
    ImageButton fb, google, twitter;
    ViewPager viewPager;
    Button login, register;
    Intent intent;

    float v=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.login_fragment, container, false);

        Context context = requireContext();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(context,gso);
        emailTextInput = root.findViewById(R.id.email);
        passTextInput = root.findViewById(R.id.insertPassword);
        forget_password = root.findViewById(R.id.button_forgot_password);
        login = root.findViewById(R.id.buttonLogin);
        fb = root.findViewById(R.id.Facebook);
        google = root.findViewById(R.id.Google);
        twitter = root.findViewById(R.id.Twitter);
        tabLayout= root.findViewById(R.id.tablayout);
        viewPager= root.findViewById(R.id.viewpager);

        fb.setTranslationX(800);
        google.setTranslationX(800);
        twitter.setTranslationX(800);
        emailTextInput.setTranslationX(800);
        passTextInput.setTranslationX(800);
        forget_password.setTranslationX(800);
        login.setTranslationX(800);

        fb.setAlpha(v);
        google.setAlpha(v);
        twitter.setAlpha(v);
        emailTextInput.setAlpha(v);
        passTextInput.setAlpha(v);
        forget_password.setAlpha(v);
        login.setAlpha(v);


        emailTextInput.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        passTextInput.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forget_password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(600).start();
        fb.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1000).start();
        google.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1050).start();
        twitter.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1100).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(800).start();

        login.setOnClickListener(item -> {
            String email = emailTextInput.getEditText().getText().toString();
            String password = passTextInput.getEditText().getText().toString();
            navigateToSecondActivity();
            if(isEmailOk(email) && isPasswordOk(password)) {
                emailTextInput.setError(null);
                passTextInput.setError(null);


            }
            else{
                Snackbar.make(root.findViewById(android.R.id.content), "E-mail o Password errate", Snackbar.LENGTH_SHORT).show();
            }
        });
        //Facebook
        fb.setOnClickListener(item -> {


            LoginManager.getInstance().logInWithReadPermissions(LoginTabFragment.this, Arrays.asList("public_profile"));

        });

        //Google
        google.setOnClickListener(item -> {
            signIn();

        });



        return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(gsc.getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void signIn(){
        Intent signInIntent=gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    void navigateToSecondActivity(){
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
        }
    }


    private boolean isEmailOk(String email) {
        // Check if the email is valid through the use of this library:
        // https://commons.apache.org/proper/commons-validator/
        if (!EmailValidator.getInstance().isValid((email))) {
            emailTextInput.setError("Email non valida");
            return false;
        } else {
            emailTextInput.setError(null);
            return true;
        }

        //TODO confrontare i dati con firebase
    }

    private boolean isPasswordOk(String password) {

        // If the password is empty return false
        if (password == null) {
            passTextInput.setError(getString(R.string.error_password_invalid));
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
            passTextInput.setError(getString(R.string.error_password_invalid));
            return false;
        } else {
            passTextInput.setError(null);
            return true;
        }

        //TODO confrontare i dati con firebase
    }
}
