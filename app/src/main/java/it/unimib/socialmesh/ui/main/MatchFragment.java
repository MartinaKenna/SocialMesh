
package it.unimib.socialmesh.ui.main;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import it.unimib.socialmesh.adapter.SimpleEventsAdapter;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.adapter.RecyclerViewEventsAdapter;
import it.unimib.socialmesh.model.Event;

/**
 * A simple {@link Fragment} subclass.
 factory method to
 * create an instance of this fragment.
 */
public class MatchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SimpleEventsAdapter myEventsAdapter;
    private List<Event> myEventsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match, container, false);
        recyclerView = view.findViewById(R.id.RecyclerviewMyevents);
        myEventsList = new ArrayList<>();
        myEventsAdapter = new SimpleEventsAdapter(myEventsList, new SimpleEventsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                //aprire i dettagli dell'evento
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myEventsAdapter);

        retrieveMyEventsFromFirebase();

        return view;
    }

    private void retrieveMyEventsFromFirebase() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userEventsRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("events");
        userEventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myEventsList.clear();

                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    String eventId = eventSnapshot.getKey();
                    Log.d("MatchFragment", "Event ID: " + eventId);
                    retrieveEventDetailsFromFirebase(eventId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MatchFragment", "Error retrieving user events: " + error.getMessage());
            }
        });
    }

    private void retrieveEventDetailsFromFirebase(String eventId) {
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("events").child(eventId);
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Event event = snapshot.getValue(Event.class);
                    if (event != null) {
                        Log.d("MatchFragment", "Event retrieved: " + event.getName1());
                        myEventsList.add(event);
                        myEventsAdapter.notifyDataSetChanged();
                        Log.d("MatchFragment", "List size: " + myEventsList.size());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MatchFragment", "Error retrieving event details: " + error.getMessage());
            }
        });
    }
}