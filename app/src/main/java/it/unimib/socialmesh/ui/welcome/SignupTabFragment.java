package it.unimib.socialmesh.ui.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import it.unimib.socialmesh.R;

public class SignupTabFragment extends Fragment {
    TextInputLayout nome, datanasc, email, pass, confirmpass;
    Button register;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.signup_fragment, container, false);
        nome=root.findViewById(R.id.fullName);
        datanasc=root.findViewById(R.id.datanasc);
        email=root.findViewById(R.id.email);
        pass=root.findViewById(R.id.insertPassword);
        confirmpass=root.findViewById(R.id.confirmpassword_signup);
        register=root.findViewById(R.id.buttonRegister);

        nome.setTranslationX(1000);
        datanasc.setTranslationX(1000);
        email.setTranslationX(1000);
        pass.setTranslationX(1000);
        confirmpass.setTranslationX(1000);
        register.setTranslationX(1000);

        nome.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        datanasc.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        confirmpass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1100).start();
        register.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1300).start();
        return root;
    }
}
