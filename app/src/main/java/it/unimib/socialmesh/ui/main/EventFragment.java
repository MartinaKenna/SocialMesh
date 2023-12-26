package it.unimib.socialmesh.ui.main;

import android.content.res.Resources;
import android.graphics.Outline;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

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
    private TextView nearyou, lastadded;
    private List<Event> eventsList;
    private EventsRepository eventsRepository;

    private RecyclerView recyclerViewEvents, recyclerViewEventsNearYou;
    private View rootView, listEvents, listNearyou, barra1, barra2, barra3;
    private SearchView searchView;
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
        eventsRepository.fetchEvents("music", "324", "2024-06-01T08:00:00Z", "2024-06-30T08:00:00Z", 10);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        hipHopRap = view.findViewById(R.id.HipHopRap);
        latin = view.findViewById(R.id.Latin);
        rock = view.findViewById(R.id.Rock);
        recyclerViewEventsNearYou = view.findViewById(R.id.recyclerviewNearYou);
        recyclerViewEvents = view.findViewById(R.id.recyclerviewEvents);
        searchView = view.findViewById(R.id.searchView);
        barra1 = view.findViewById(R.id.barra);
        barra2 = view.findViewById(R.id.barra2);
        barra3 = view.findViewById(R.id.barra3);
        nearyou = view.findViewById(R.id.nearYou);
        lastadded = view.findViewById(R.id.lastAdded);
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        hipHopRap.setTranslationX(-1000);
        latin.setTranslationY(screenHeight);
        rock.setTranslationX(1000);
        recyclerViewEvents.setTranslationX(1000);
        recyclerViewEventsNearYou.setTranslationX(1000);
        searchView.setTranslationY(screenHeight);
        barra2.setTranslationX(-1000);
        barra1.setTranslationY(screenHeight);
        barra3.setTranslationX(1000);
        nearyou.setTranslationX(1500);
        lastadded.setTranslationX(1500);

        hipHopRap.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        latin.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        rock.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1100).start();
        searchView.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1400).start();
        recyclerViewEventsNearYou.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1700).start();
        recyclerViewEvents.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(2000).start();
        barra2.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(2700).start();
        barra1.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(2700).start();
        barra3.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(2700).start();
        nearyou.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(2700).start();
        lastadded.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(2700).start();

        rock.setOnClickListener(item -> {
            recyclerViewEventsAdapter.filterByGenre("rock");
            recyclerViewEventsAdapterNearYou.filterByGenre("rock");
        });

        hipHopRap.setOnClickListener(item -> {
            recyclerViewEventsAdapter.filterByGenre("Hip-Hop/Rap");
            recyclerViewEventsAdapterNearYou.filterByGenre("Hip-Hop/Rap");
        });

        latin.setOnClickListener(item -> {
            recyclerViewEventsAdapter.filterByGenre("latin");
            recyclerViewEventsAdapterNearYou.filterByGenre("latin");
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //TODO raga funziona il filtro ma la searchview non permette di scriverci sopra, solo di incollare  del testo. Da questo problema solo dentro il fragment_event.
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclerViewEventsAdapter.filterByQuery(query);
                recyclerViewEventsAdapterNearYou.filterByQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerViewEventsAdapter.filterByQuery(newText);
                recyclerViewEventsAdapterNearYou.filterByQuery(newText);
                recyclerViewEventsAdapter.notifyDataSetChanged();
                return true;
            }
        });



        return view;

    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onSuccess(List<Event> eventsList, long lastUpdate) {
        if (eventsList != null) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RecyclerView.LayoutManager layoutManagerNearYou = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewEventsAdapterNearYou = new RecyclerViewEventsAdapter(eventsList, 0);
                    recyclerViewEventsNearYou.setLayoutManager(layoutManagerNearYou);
                    recyclerViewEventsNearYou.setAdapter(recyclerViewEventsAdapterNearYou);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewEventsAdapter = new RecyclerViewEventsAdapter(eventsList, 1);
                    recyclerViewEvents.setLayoutManager(layoutManager);
                    recyclerViewEvents.setAdapter(recyclerViewEventsAdapter);
                }
            });
        }

        // Resto del codice, se necessario
    }


    @Override
    public void onFailure(String errorMessage) {
        Log.d(TAG, "CHIAMATA API FALLITA");
    }
}