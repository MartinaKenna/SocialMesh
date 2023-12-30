package it.unimib.socialmesh.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.adapter.RecyclerViewEventsAdapter;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.EventApiResponse;
import it.unimib.socialmesh.model.Result;
import it.unimib.socialmesh.repository.EventsRepository;
import it.unimib.socialmesh.repository.IEventsRepositoryWithLiveData;
import it.unimib.socialmesh.service.FirebaseEvent;
import it.unimib.socialmesh.util.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {

    private static final String TAG = EventFragment.class.getSimpleName();
    private TextView nearyou, lastadded;
    private List<Event> eventsList;

    private RecyclerView recyclerViewEvents, recyclerViewEventsNearYou;
    private View rootView, listEvents, listNearyou, barra1, barra2, barra3;
    private SearchView searchView;
    private RecyclerViewEventsAdapter recyclerViewEventsAdapterNearYou, recyclerViewEventsAdapter;
    private EventViewModel eventViewModel;

    private EventsRepository eventsRepository;
    private Button hipHopRap, latin, rock, buttonAll;

    //questo serve
    public EventFragment() {}

    public static EventFragment newInstance(String param1, String param2) { return new EventFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        eventsRepository = new EventsRepository(requireActivity().getApplication(), this);
        eventsList = new ArrayList<>();
        eventsRepository.fetchEvents("music", "324", "2024-06-01T08:00:00Z", "2024-06-30T08:00:00Z", 10);
        */
        IEventsRepositoryWithLiveData eventsRepositoryWithLiveData =
                ServiceLocator.getInstance().getEventRepository(
                        requireActivity().getApplication());
        Log.d(TAG,"repository creato");

        eventViewModel = new ViewModelProvider(
                requireActivity(),
                new EventViewModelFactory(eventsRepositoryWithLiveData)).get(EventViewModel.class);

        eventsList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        buttonAll = view.findViewById(R.id.buttonAll);
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
      /*  buttonAll.setTranslationX((1000));
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
        buttonAll.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        hipHopRap.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        latin.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1100).start();
        rock.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1400).start();
        searchView.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1700).start();
        recyclerViewEventsNearYou.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(2000).start();
        recyclerViewEvents.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(2300).start();
        barra2.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(3000).start();
        barra1.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(3000).start();
        barra3.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(3000).start();
        nearyou.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(3000).start();
        lastadded.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(3000).start();
*/
        buttonAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rimuovi tutti i filtri e mostra tutti gli eventi
                recyclerViewEventsAdapter.clearFilters();
                recyclerViewEventsAdapterNearYou.clearFilters();
            }
        });
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
        RecyclerView.LayoutManager layoutManagerNearYou = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewEventsAdapterNearYou = new RecyclerViewEventsAdapter(eventsList, 0,
                new RecyclerViewEventsAdapter.OnItemClickListener() {
                    @Override
                    public void onEventItemClick(Event event) {
                        Log.d(TAG, "onEventItemClick called: " + event.getName());
                        Log.d(TAG, "Bottone cliccato on view create near you");
                        EventFragmentDirections.ActionEventFragmentToEventDetailsFragment action =
                                EventFragmentDirections.actionEventFragmentToEventDetailsFragment(event);
                        Navigation.findNavController(view).navigate(action);
                    }
                });
        recyclerViewEventsNearYou.setLayoutManager(layoutManagerNearYou);
        recyclerViewEventsNearYou.setAdapter(recyclerViewEventsAdapterNearYou);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewEventsAdapter = new RecyclerViewEventsAdapter(eventsList, 1,
                new RecyclerViewEventsAdapter.OnItemClickListener() {
                    @Override
                    public void onEventItemClick(Event event) {
                        Log.d(TAG, "onEventItemClick called: " + event.getName());
                        Log.d(TAG, "Bottone cliccato on view create normale");
                        EventFragmentDirections.ActionEventFragmentToEventDetailsFragment action =
                                EventFragmentDirections.actionEventFragmentToEventDetailsFragment(event);
                        Navigation.findNavController(view).navigate(action);
                    }
                });
        recyclerViewEvents.setLayoutManager(layoutManager);
        recyclerViewEvents.setAdapter(recyclerViewEventsAdapter);

        return view;

    }
    public void forceRefreshFragment() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.detach(this).attach(this).commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager layoutManagerNearYou = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewEventsAdapterNearYou = new RecyclerViewEventsAdapter(eventsList, 0,
                new RecyclerViewEventsAdapter.OnItemClickListener() {
                    @Override
                    public void onEventItemClick(Event event) {
                        Log.d(TAG, "onEventItemClick called: " + event.getName());
                        Log.d(TAG, "Bottone cliccato on view created near you");
                        EventFragmentDirections.ActionEventFragmentToEventDetailsFragment action =
                                EventFragmentDirections.actionEventFragmentToEventDetailsFragment(event);
                        Navigation.findNavController(view).navigate(action);
                    }
                });
        recyclerViewEventsNearYou.setLayoutManager(layoutManagerNearYou);
        recyclerViewEventsNearYou.setAdapter(recyclerViewEventsAdapterNearYou);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewEventsAdapter = new RecyclerViewEventsAdapter(eventsList, 1,
                new RecyclerViewEventsAdapter.OnItemClickListener() {
                    @Override
                    public void onEventItemClick(Event event) {
                        Log.d(TAG, "onEventItemClick called: " + event.getName());
                        Log.d(TAG, "Bottone cliccato on view created normale");
                        EventFragmentDirections.ActionEventFragmentToEventDetailsFragment action =
                                EventFragmentDirections.actionEventFragmentToEventDetailsFragment(event);
                        Navigation.findNavController(view).navigate(action);
                    }
                });
        recyclerViewEvents.setLayoutManager(layoutManager);
        recyclerViewEvents.setAdapter(recyclerViewEventsAdapter);





        eventViewModel.getEvents("sport", "200", "2023-12-30T08:00:00Z", "2024-06-30T08:00:00Z",10).observe(getViewLifecycleOwner(),
                result -> {
                    if (result.isSuccess()) {
                    EventApiResponse eventResponse = ((Result.Success) result).getData();
                    List<Event> fetchedEvents = eventResponse.getEvents();

                    if (!eventViewModel.isLoading()) {
                        if (eventViewModel.isFirstLoading()) {
                            //eventViewModel.setTotalResults(((EventApiResponse) eventResponse).getTotalResults());
                            eventViewModel.setFirstLoading(false);
                            this.eventsList.addAll(fetchedEvents);
                            recyclerViewEventsAdapter.notifyItemRangeInserted(0,
                                    this.eventsList.size());
                            recyclerViewEventsAdapterNearYou.notifyItemRangeInserted(0,
                                    this.eventsList.size());

                        } else {
                            // Updates related to the favorite status of the news
                            eventsList.clear();
                            eventsList.addAll(fetchedEvents);

                            //recyclerViewEventsAdapter.notifyItemChanged(0, fetchedEvents.size());
                            //recyclerViewEventsAdapterNearYou.notifyItemChanged(0, fetchedEvents.size());

                            //TODO togliere sto schifo
                            recyclerViewEventsAdapter.clearFilters();
                            recyclerViewEventsAdapterNearYou.clearFilters();
                            uploadEventsToFirebase(eventsList);
                        }

                    } else {
                        eventViewModel.setLoading(false);
                        eventViewModel.setCurrentResults(eventsList.size());

                        int initialSize = eventsList.size();

                        for (int i = 0; i < eventsList.size(); i++) {
                            if (eventsList.get(i) == null) {
                                eventsList.remove(eventsList.get(i));
                            }
                        }

                        for (int i = 0; i < fetchedEvents.size(); i++) {
                            eventsList.add(fetchedEvents.get(i));
                        }
                        recyclerViewEventsAdapter.notifyItemRangeInserted(initialSize, eventsList.size());
                        recyclerViewEventsAdapterNearYou.notifyItemRangeInserted(initialSize, eventsList.size());
                    }
                }
                 else {
                        Log.d(TAG,"pane");
        }

    });

    }
    private void uploadEventsToFirebase(List<Event> apiEvents) {
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference().child("events");

        for (Event apiEvent : apiEvents) {
            // Controlla se l'evento è già presente nel database
            eventsRef.child(String.valueOf(apiEvent.getRemoteId())).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        // Se l'evento non esiste già, aggiungilo
                        FirebaseEvent firebaseEvent = new FirebaseEvent(
                                apiEvent.getRemoteId(),
                                apiEvent.getName(),
                                apiEvent.getLocalId(),
                                Arrays.asList()

                        );
                        eventsRef.child(String.valueOf(apiEvent.getRemoteId())).setValue(firebaseEvent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }





}