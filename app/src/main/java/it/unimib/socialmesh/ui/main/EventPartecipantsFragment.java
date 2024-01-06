package it.unimib.socialmesh.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.adapter.PartecipantsAdapter;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.User;

public class EventPartecipantsFragment extends Fragment {
    private static final String TAG = EventPartecipantsFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private PartecipantsAdapter usersAdapter;
    private List<String> participantsList;
    private List<String> idList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_partecipants, container, false);
        recyclerView = view.findViewById(R.id.RecyclerviewPartecipants);
        participantsList = new ArrayList<>();
        idList = new ArrayList<>();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUser = auth.getCurrentUser().getUid();
        usersAdapter = new PartecipantsAdapter(idList, participantsList, userId -> {
            DatabaseReference currentUserMatchRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser)
                    .child("likes");

            currentUserMatchRef.child(userId).setValue(true);
            DatabaseReference clickedUserLikesRef = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(userId).child("likes");

            clickedUserLikesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(currentUser)) {
                        addMatch(currentUser,userId);
                        Toast.makeText(requireContext(), "MATCH", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Gestisci eventuali errori durante la lettura dei dati
                }
            });

        });
        Button closeSettingsButton = view.findViewById(R.id.back_button);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(usersAdapter);
        closeSettingsButton.setOnClickListener(CloseView -> {
            getParentFragmentManager().popBackStack();
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {

            Event commonEvent = EventPartecipantsFragmentArgs.fromBundle(getArguments()).getCommonEvent();
            Log.d(TAG, "Evento ricevuto con successo");
            //TODO aggiungere cosa fare con l'eveto ricevuto
            retrieveParticipantsFromFirebase(commonEvent); // Recupera i partecipanti dall'evento nel database Firebase

        } else {
            Log.d(TAG, "Errore, evento passato da Match a Partecipants vuoto");
        }


    }

    private void retrieveParticipantsFromFirebase(Event commonEvent) {

        String eventId = commonEvent.getRemoteId();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUser = auth.getCurrentUser().getUid();

        DatabaseReference eventParticipantsRef = FirebaseDatabase.getInstance().getReference()
                .child("events").child(eventId).child("participants");

        eventParticipantsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                participantsList.clear();

                for (DataSnapshot participantSnapshot : snapshot.getChildren()) {

                    String userId = participantSnapshot.getKey();

                    // Recupera i dettagli dell'utente partecipante
                    retrieveUserDetailsFromFirebase(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Errore nel recupero dei partecipanti", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveUserDetailsFromFirebase(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUser = auth.getCurrentUser().getUid();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null && !userId.equals(currentUser)) { // Verifica se l'ID non è uguale all'ID dell'utente attuale
                        String currentId = snapshot.getKey();
                        participantsList.add(user.getName());
                        idList.add(currentId);
                        Log.d(TAG, currentId);
                        usersAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Errore nel recupero dei dettagli utente", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addMatch(String currentUserUid, String matchedUserId) {
        DatabaseReference currentUserMatchRef = FirebaseDatabase.getInstance()
                .getReference().child("users").child(currentUserUid).child("matches");
        DatabaseReference matchedUserMatchRef = FirebaseDatabase.getInstance()
                .getReference().child("users").child(matchedUserId).child("matches");

        currentUserMatchRef.child(matchedUserId).setValue(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Match aggiunto");
                        Toast.makeText(requireContext(), "Match aggiunto", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Errore nell'aggiunta del match");
                    }
                });

        matchedUserMatchRef.child(currentUserUid).setValue(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Match aggiunto");
                    } else {
                        Log.e(TAG, "Errore nell'aggiunta del match");
                    }
                });
    }

}

