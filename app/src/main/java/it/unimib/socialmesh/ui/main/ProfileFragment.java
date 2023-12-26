package it.unimib.socialmesh.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.User;
import it.unimib.socialmesh.ui.welcome.LoginActivity;

public class ProfileFragment extends Fragment {
    int REQUEST_CODE;
    TextView fullName, userEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        fullName = root.findViewById(R.id.userName); // Aggiunto il riferimento alla TextView del nome
        userEmail = root.findViewById(R.id.userEmail); // Aggiunto il riferimento alla TextView dell'email

        Button buttonLogout = root.findViewById(R.id.buttonLogout);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null && getActivity() != null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        buttonLogout.setOnClickListener(view -> logoutUser());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    fullName.setText(user.fullName);
                    userEmail.setText(user.email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestione dell'errore
            }
        });
        AppCompatImageView buttonSettings = root.findViewById(R.id.button_settings); // Assumi che l'ID corrisponda a un AppCompatImageView

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Se l'utente ha premuto "Update" e ha inviato dati aggiornati
                if (data != null && data.hasExtra("UPDATED_DATA_KEY")) {
                    String updatedData = data.getStringExtra("UPDATED_DATA_KEY");

                    // Usa i dati aggiornati come necessario (aggiorna UI, salva nel database, ecc.)
                    // Ad esempio:
                    // textView.setText(updatedData);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // L'utente ha annullato l'operazione nell'Activity Settings
            }
        }
    }


    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
