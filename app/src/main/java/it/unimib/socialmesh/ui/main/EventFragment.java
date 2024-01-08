package it.unimib.socialmesh.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.adapter.RecyclerViewEventsAdapter;
import it.unimib.socialmesh.data.repository.event.IEventsRepositoryWithLiveData;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.EventApiResponse;
import it.unimib.socialmesh.model.Result;
import it.unimib.socialmesh.util.ServiceLocator;

public class EventFragment extends Fragment {

    private static final String TAG = EventFragment.class.getSimpleName();
    private TextView nearyou, lastadded;
    private List<Event> eventsList;

    private RecyclerView recyclerViewEvents, recyclerViewEventsNearYou;
    private View rootView, listEvents, listNearyou, barra1, barra2, barra3;
    private SearchView searchView;
    private RecyclerViewEventsAdapter recyclerViewEventsAdapterNearYou, recyclerViewEventsAdapter;
    private EventViewModel eventViewModel;
    private Button filter, button1, button2,button3, viewAll;
    private PopupWindow popupWindow;

    private ProgressBar progressBar;


    //questo serve
    public EventFragment() {}

    public static EventFragment newInstance(String param1, String param2) { return new EventFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        recyclerViewEventsNearYou = view.findViewById(R.id.recyclerviewNearYou);
        recyclerViewEvents = view.findViewById(R.id.recyclerviewEvents);
        searchView = view.findViewById(R.id.searchView);
        barra1 = view.findViewById(R.id.barra);
        barra2 = view.findViewById(R.id.barra2);
        barra3 = view.findViewById(R.id.barra3);
        nearyou = view.findViewById(R.id.nearYou);
        lastadded = view.findViewById(R.id.lastAdded);
        viewAll = view.findViewById(R.id.viewAll);
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
        filter = view.findViewById(R.id.button);

        filter.setOnClickListener(v -> {
            View popupView = inflater.inflate(R.layout.popupview, container, false);

            button1 = popupView.findViewById(R.id.button1);
            button2 = popupView.findViewById(R.id.button2);
            button3 = popupView.findViewById(R.id.button3);
            //dichiaro la tendina
            popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

            // Imposto il testo per i bottoni
            switch(getMostFrequentGenres().size()){
                case 1: button1.setText(getMostFrequentGenres().get(0));
                    button2.setVisibility(View.GONE);
                    button3.setVisibility(View.GONE);
                    break;
                case 2: button1.setText(getMostFrequentGenres().get(0));
                    button2.setText(getMostFrequentGenres().get(1));
                    button3.setVisibility(View.GONE);
                    break;
                case 3: button1.setText(getMostFrequentGenres().get(0));
                    button2.setText(getMostFrequentGenres().get(1));
                    button3.setText(getMostFrequentGenres().get(2));
                    break;
                default:button1.setVisibility(View.GONE);
                    button2.setVisibility(View.GONE);
                    button3.setVisibility(View.GONE);
                    break;
            }

            button1.setOnClickListener(t -> {
                String buttonText = ((Button) t).getText().toString();
                recyclerViewEventsAdapter.filterByGenre(buttonText);
                recyclerViewEventsAdapterNearYou.filterByGenre(buttonText);
                popupWindow.dismiss();
            });
            button2.setOnClickListener(t -> {
                String buttonText = ((Button) t).getText().toString();
                recyclerViewEventsAdapter.filterByGenre(buttonText);
                recyclerViewEventsAdapterNearYou.filterByGenre(buttonText);
                popupWindow.dismiss();
            });
            button3.setOnClickListener(t -> {
                String buttonText = ((Button) t).getText().toString();
                recyclerViewEventsAdapter.filterByGenre(buttonText);
                recyclerViewEventsAdapterNearYou.filterByGenre(buttonText);
                popupWindow.dismiss();
            });

            popupWindow.showAsDropDown(filter, 0, -popupWindow.getHeight());

        });

        viewAll.setOnClickListener(v -> {
            recyclerViewEventsAdapter.setItems(eventsList);
            recyclerViewEventsAdapterNearYou.setItems(eventsList);
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

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            Log.d("FirebaseUser", "User ID: " + currentUser.getUid());
            Log.d("FirebaseUser", "User Email: " + currentUser.getEmail());
            // Aggiungi altri dati utente se necessario
        } else {
            Log.d("FirebaseUser", "User is not logged in");
        }
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





        eventViewModel.getEvents("", "214", 50, "2023-12-30T08:00:00Z", "2024-06-30T08:00:00Z",10).observe(getViewLifecycleOwner(),
                result -> {
                    if (result.isSuccess()) {
                    EventApiResponse eventResponse = ((Result.EventResponseSuccess) result).getData();
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
                        }
                        progressBar.setVisibility(View.GONE);

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
                        progressBar.setVisibility(View.GONE);
        }

    });

    }



    public List<String> getMostFrequentGenres() {
        Map<String, Integer> genreOccurrences = new HashMap<>();

        // Conto le occorrenze di ciascun genere
        for (Event event : eventsList) {
            String genreName = event.getGenreName();
            genreOccurrences.put(genreName, genreOccurrences.getOrDefault(genreName, 0) + 1);
        }

        // Trovo i tre generi piu frequenti diversi tra loro
        List<String> mostFrequentGenres = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int maxOccurrences = 0;
            String mostFrequentGenre = "";

            for (Map.Entry<String, Integer> entry : genreOccurrences.entrySet()) {
                if (!mostFrequentGenres.contains(entry.getKey()) && entry.getValue() > maxOccurrences) {
                    maxOccurrences = entry.getValue();
                    mostFrequentGenre = entry.getKey();
                }
            }

            if (!mostFrequentGenre.isEmpty()) {
                mostFrequentGenres.add(mostFrequentGenre);
            }
        }

        return mostFrequentGenres;
    }


}