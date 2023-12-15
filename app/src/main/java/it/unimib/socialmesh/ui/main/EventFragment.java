package it.unimib.socialmesh.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.R;

import it.unimib.socialmesh.adapter.RecyclerViewEventsAdapter;
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
    private List<Event> eventsList;
    private EventsRepository eventsRepository;

    private RecyclerView recyclerViewEvents, recyclerViewEventsNearYou;
    private View rootView, listEvents, listNearyou;
    private RecyclerViewEventsAdapter recyclerViewEventsAdapterNearYou, recyclerViewEventsAdapter;

    private Button hipHopRap, latin, rock;

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
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        hipHopRap = view.findViewById(R.id.HipHopRap);
        latin = view.findViewById(R.id.Latin);
        rock = view.findViewById(R.id.Rock);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManagerNearYou = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);

        //TODO non funzionano i button, penso a causa di come ho impostato il chiamo della recycler all'interno dell'onsuccess
        hipHopRap.setOnClickListener(item -> {
            Log.d(TAG, "dentro button hiphop" );
            recyclerViewEventsAdapterNearYou = new RecyclerViewEventsAdapter(eventsList, 0,0);
            recyclerViewEventsNearYou.setLayoutManager(layoutManagerNearYou);
            recyclerViewEventsNearYou.setAdapter(recyclerViewEventsAdapterNearYou);
            recyclerViewEventsAdapter = new RecyclerViewEventsAdapter(eventsList,1, 0);
            recyclerViewEvents.setLayoutManager(layoutManager);
            recyclerViewEvents.setAdapter(recyclerViewEventsAdapter);
        });

        latin.setOnClickListener(item -> {
            recyclerViewEventsAdapterNearYou = new RecyclerViewEventsAdapter(eventsList, 0,1);
            recyclerViewEventsNearYou.setLayoutManager(layoutManagerNearYou);
            recyclerViewEventsNearYou.setAdapter(recyclerViewEventsAdapterNearYou);
            recyclerViewEventsAdapter = new RecyclerViewEventsAdapter(eventsList,1, 1);
            recyclerViewEvents.setLayoutManager(layoutManager);
            recyclerViewEvents.setAdapter(recyclerViewEventsAdapter);
        });

        rock.setOnClickListener(item -> {
            recyclerViewEventsAdapterNearYou = new RecyclerViewEventsAdapter(eventsList, 0,2);
            recyclerViewEventsNearYou.setLayoutManager(layoutManagerNearYou);
            recyclerViewEventsNearYou.setAdapter(recyclerViewEventsAdapterNearYou);
            recyclerViewEventsAdapter = new RecyclerViewEventsAdapter(eventsList,1, 2);
            recyclerViewEvents.setLayoutManager(layoutManager);
            recyclerViewEvents.setAdapter(recyclerViewEventsAdapter);
        });

        return inflater.inflate(R.layout.fragment_event, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventsRepository.fetchEvents("music", "324", "2024-06-01T08:00:00Z", "2024-06-30T08:00:00Z", 10);

    }


    @Override
    public void onSuccess(List<Event> eventsList, long lastUpdate) {
        if (eventsList != null) {
            this.eventsList.clear();
            this.eventsList.addAll(eventsList);
        }
        recyclerViewEventsNearYou = getView().findViewById(R.id.recyclerviewNearYou);
        recyclerViewEvents = getView().findViewById(R.id.recyclerviewEvents);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManagerNearYou = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerViewEventsAdapterNearYou = new RecyclerViewEventsAdapter(eventsList, 0);
        recyclerViewEventsNearYou.setLayoutManager(layoutManagerNearYou);
        recyclerViewEventsNearYou.setAdapter(recyclerViewEventsAdapterNearYou);

        recyclerViewEventsAdapter = new RecyclerViewEventsAdapter(eventsList,1);
        recyclerViewEvents.setLayoutManager(layoutManager);
        recyclerViewEvents.setAdapter(recyclerViewEventsAdapter);






        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    @Override
    public void onFailure(String errorMessage) {
        Log.d(TAG, "CHIAMATA API FALLITA");
    }
}