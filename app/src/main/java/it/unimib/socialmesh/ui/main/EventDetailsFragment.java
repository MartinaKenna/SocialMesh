package it.unimib.socialmesh.ui.main;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.Event;

public class EventDetailsFragment extends Fragment {

    private Event currentEvent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        //Log.d("DETAILSFRAGMENT",  currentEvent.getRemoteId());
        // Recupera l'evento dalla navigazione
        if (getArguments() != null) {
            currentEvent = EventDetailsFragmentArgs.fromBundle(getArguments()).getEvent();
        }

        if (currentEvent != null) {
            // Inizializza gli elementi del layout con i dati dell'evento
            ImageView imageViewEvent = view.findViewById(R.id.imageview_event);
            TextView textViewEventTitle = view.findViewById(R.id.textview_event_title);
            TextView textViewEventDate = view.findViewById(R.id.textview_event_date);
            TextView textViewEventDetails = view.findViewById(R.id.textview_event_details);
            Button joinButton = view.findViewById(R.id.join_button);
            Button closeSettingsButton = view.findViewById(R.id.back_btn);
            joinButton.setText("JOIN");
            joinButton.requestLayout();

            // Imposta i dati dell'evento
            Glide.with(requireContext()).load(currentEvent.getUrlImages()).into(imageViewEvent);
            textViewEventTitle.setText(currentEvent.getName1());
            textViewEventDate.setText(currentEvent.getDates1());
            textViewEventDetails.setText(currentEvent.getDescription());


            // Aggiungi un listener per il pulsante di partecipazione
            closeSettingsButton.setOnClickListener(CloseView -> {
                getParentFragmentManager().popBackStack();
            });
            joinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Aggiungi qui la logica per partecipare all'evento

                    // Verifica se l'utente è autenticato (puoi utilizzare il tuo sistema di autenticazione)
                    if (userIsAuthenticated()) {
                        // Ottieni l'ID dell'utente corrente (sostituisci con il metodo appropriato per ottenere l'ID dell'utente)
                        String userId = getCurrentUserId();

                        // Verifica che l'ID dell'utente non sia nullo o vuoto
                        if (userId != null && !userId.isEmpty()) {
                            // Ottieni l'ID dell'evento corrente
                            String eventId = currentEvent.getRemoteId(); // Assumi che l'ID dell'evento sia il localId

                            // Assicurati che l'ID dell'evento non sia nullo o vuoto
                            if (eventId != null && !eventId.isEmpty()) {
                                // Ottieni il riferimento all'evento nel database
                                DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("events").child(eventId);

                                // Aggiungi l'ID dell'utente tra i partecipanti solo se non è già presente
                                eventRef.child("participants").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (!snapshot.exists()) {
                                            // L'utente non è ancora tra i partecipanti, aggiungi l'ID
                                            eventRef.child("participants").child(userId).setValue(true);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Gestisci eventuali errori di lettura dal database
                                    }
                                });

                                // Ora puoi eseguire altre azioni dopo che l'utente è stato aggiunto come partecipante
                                // Ad esempio, puoi navigare a un'altra schermata o mostrare un messaggio di conferma
                                Navigation.findNavController(v).popBackStack(); // Naviga all'indietro
                            }
                        }
                    }
                }
            });



        }
        return view;
    }
    private String getCurrentUserId() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }
    private boolean userIsAuthenticated() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        return user != null;
    }
}
