package it.unimib.socialmesh.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.R;

import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.repository.EventsRepository;
import it.unimib.socialmesh.util.ResponseCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment implements ResponseCallback {

    private static final String TAG = EventFragment.class.getSimpleName();
    private RecyclerView recyclerViewEvents;
  //  private RecyclerViewEventsAdapter recyclerViewEventsAdapter;

    private List<Event> eventsList;
    private EventsRepository eventsRepository;

    //questo serve
    public EventFragment() {}

    public static EventFragment newInstance(String param1, String param2) {
        return new EventFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsRepository = new EventsRepository(requireActivity().getApplication(), this);
        eventsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_event, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewEvents = view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        recyclerViewEvents.setLayoutManager(layoutManager);
       // recyclerViewEvents.setAdapter(recyclerViewEventsAdapter);

        eventsRepository.fetchEvents("music", "324", "2024-06-01T08:00:00Z", "2024-06-30T08:00:00Z", 10);
    }

    @Override
    public void onSuccess(List<Event> eventsList, long lastUpdate) {
        Log.d(TAG, "CHIAMATA API SUCCESSO");
        if(eventsList == null)
            Log.d(TAG, "LISTA EVENTI NULLA");
        else
            Log.d(TAG, "GHE DETER ARGOTA");


        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //recyclerViewEventsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onFailure(String errorMessage) {
        Log.d(TAG, "CHIAMATA API FALLITA");
    }
}