package it.unimib.socialmesh.ui.main;

import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.location.LocationManager;


import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static it.unimib.socialmesh.util.Constants.ALL_USA_DMAID;
import static it.unimib.socialmesh.util.Constants.SIZE_OF_EVENT_SEARCH;
import static it.unimib.socialmesh.util.Constants.TYPE_OF_EVENT_SEARCH;
import static it.unimib.socialmesh.util.Constants.WEEKS_OF_EVENT_SEARCH;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.Manifest;
import android.widget.Toast;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.adapter.RecyclerViewEventsAdapter;
import it.unimib.socialmesh.data.repository.event.IEventsRepositoryWithLiveData;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.EventApiResponse;
import it.unimib.socialmesh.model.Result;
import it.unimib.socialmesh.util.ServiceLocator;

public class EventFragment extends Fragment{

    private static final String TAG = EventFragment.class.getSimpleName();
    private TextView nearyou, lastadded;
    private List<Event> eventsList;
    private LocationCallback locationCallback;
    private RecyclerView recyclerViewEvents, recyclerViewEventsNearYou;
    private View rootView, listEvents, listNearyou, barra1, barra2, barra3;
    private SearchView searchView;
    private RecyclerViewEventsAdapter recyclerViewEventsAdapterNearYou, recyclerViewEventsAdapter;
    private EventViewModel eventViewModel;
    private Button filter, button1, button2,button3, viewAll, buttonKM, button4, button5, button6;
    private PopupWindow popupWindow, popupWindow2;

    private ProgressBar progressBar;
    private LocationRequest locationRequest;
    private Double latitude, longitude;


    //questo serve
    public EventFragment() {}

    public static EventFragment newInstance(String param1, String param2) { return new EventFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        IEventsRepositoryWithLiveData eventsRepositoryWithLiveData =
                ServiceLocator.getInstance().getEventRepository(
                        requireActivity().getApplication());


        eventViewModel = new ViewModelProvider(
                requireActivity(),
                new EventViewModelFactory(eventsRepositoryWithLiveData)).get(EventViewModel.class);

        eventsList = new ArrayList<>();
        userPositionRequest(requireContext());
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
        buttonKM = view.findViewById(R.id.buttonKM);
        Context context = requireContext();
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
        //richiamo il processo per la posizione

        //ottengo la posizione
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

        buttonKM.setOnClickListener(v ->{
            View popupView2 = inflater.inflate(R.layout.popupview2, container, false);

            popupWindow2 = new PopupWindow(popupView2, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

            button4 = popupView2.findViewById(R.id.button4);
            button5 = popupView2.findViewById(R.id.button5);
            button6 = popupView2.findViewById(R.id.button6);

            button4.setText("10km");
            button5.setText("50km");
            button6.setText("100km");

            button4.setOnClickListener(t ->{
                recyclerViewEventsAdapterNearYou.setKM(10);
                popupWindow2.dismiss();
            });
            button5.setOnClickListener(t ->{
                recyclerViewEventsAdapterNearYou.setKM(50);
                popupWindow2.dismiss();
            });
            button6.setOnClickListener(t ->{
                recyclerViewEventsAdapterNearYou.setKM(100);
                popupWindow2.dismiss();
            });
            popupWindow2.showAsDropDown(buttonKM, 0, -popupWindow2.getHeight());
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

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        int initialSize = eventsList.size();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            Log.d("FirebaseUser", "User ID: " + currentUser.getUid());
            Log.d("FirebaseUser", "User Email: " + currentUser.getEmail());
            // Aggiungi altri dati utente se necessario
        } else {
            Log.d("FirebaseUser", "User is not logged in");
        }

        initializeAdapters();

        eventViewModel.getEvents(TYPE_OF_EVENT_SEARCH, ALL_USA_DMAID, SIZE_OF_EVENT_SEARCH, getTodayDateString(), getDateInSomeWeeks(),10).observe(getViewLifecycleOwner(),
                result -> {
                    if (result.isSuccess()) {
                        EventApiResponse eventResponse = ((Result.EventResponseSuccess) result).getData();
                        List<Event> fetchedEvents = eventResponse.getEvents();

                        if (!eventViewModel.isLoading()) {
                            if (eventViewModel.isFirstLoading()) {
                                eventViewModel.setFirstLoading(false);
                                this.eventsList.addAll(fetchedEvents);
                                recyclerViewEventsAdapter.notifyItemRangeInserted(0, this.eventsList.size());
                                recyclerViewEventsAdapterNearYou.notifyItemRangeInserted(0, this.eventsList.size());
                            } else {
                                eventsList.clear();
                                eventsList.addAll(fetchedEvents);

                                recyclerViewEventsAdapter.clearFilters();
                                recyclerViewEventsAdapterNearYou.clearFilters();
                            }
                            progressBar.setVisibility(View.GONE);
                        } else {
                            eventViewModel.setLoading(false);
                            eventViewModel.setCurrentResults(eventsList.size());

                            for (int i = 0; i < eventsList.size(); i++) {
                                if (eventsList.get(i) == null) {
                                    eventsList.remove(eventsList.get(i));
                                }
                            }

                            eventsList.addAll(fetchedEvents);
                            recyclerViewEventsAdapter.notifyItemRangeInserted(initialSize, eventsList.size());
                            recyclerViewEventsAdapterNearYou.notifyItemRangeInserted(initialSize, eventsList.size());
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                });


    }

    private void initializeAdapters() {
        RecyclerView.LayoutManager layoutManagerNearYou = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewEventsAdapterNearYou = new RecyclerViewEventsAdapter(requireContext(),latitude, longitude, eventsList, 0,
                new RecyclerViewEventsAdapter.OnItemClickListener() {
                    @Override
                    public void onEventItemClick(Event event) {
                        EventFragmentDirections.ActionEventFragmentToEventDetailsFragment action =
                                EventFragmentDirections.actionEventFragmentToEventDetailsFragment(event);
                        Navigation.findNavController(requireView()).navigate(action);
                    }
                });

        recyclerViewEventsAdapter = new RecyclerViewEventsAdapter(requireContext(),latitude, longitude, eventsList, 1,
                new RecyclerViewEventsAdapter.OnItemClickListener() {
                    @Override
                    public void onEventItemClick(Event event) {
                        EventFragmentDirections.ActionEventFragmentToEventDetailsFragment action =
                                EventFragmentDirections.actionEventFragmentToEventDetailsFragment(event);
                        Navigation.findNavController(requireView()).navigate(action);
                    }
                });

        // Imposta gli adapter una volta ottenuta la posizione
        recyclerViewEventsNearYou.setLayoutManager(layoutManagerNearYou);
        recyclerViewEventsNearYou.setAdapter(recyclerViewEventsAdapterNearYou);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewEvents.setLayoutManager(layoutManager);
        recyclerViewEvents.setAdapter(recyclerViewEventsAdapter);
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
    private boolean isGPSEnabled() {
        //controllo se il GPS è on
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    //definisco la struttura per ottenere la localizzazione
    public void userPositionRequest(Context context) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //priorità della precisione della posizione
        locationRequest.setInterval(5000); //intervallo di aggiornamento
        locationRequest.setFastestInterval(2000); //altro intervallo di aggiornamento
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //i permessi sono stati accettati dall'utente
                Log.d(TAG, "permessi accettati");
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                if (isGPSEnabled()) {
                    //se il gps è on e i permessi sono stati accettati dall'utente
                    //si aggiorna la posizione tramite getFusedLocationProviderClient
                    Log.d(TAG, "GPS attivo");
                    startLocationUpdates(context);
                } else {
                    //altrimenti si richiede all'utente di attivarlo
                    turnOnGPS();
                }
            } else {// i permessi non sono stati accettati dall'utente
                //si richiede di accettare i permessi
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                Log.d(TAG, "Richiesta mandata");
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "permessi accettati, richiamo startLocationUpdates");
                    startLocationUpdates(context);
                }
            }
        }

    }


    private void turnOnGPS() {

        //definisco la richiesta e inserisco di richiedere sempre il permesso di attivare il GPS nel caso in cui è disattivato
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        //controlliamo le impostazioni del GPS e se è attivo
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(requireContext()).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(requireContext(), "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            //se il GPS è gia attivo viene richiesto di modificare le impostazioni di esso

                            try {
                                //viene richiesto all'utente di modificare le impostazioni
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(requireActivity(), 1);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Il dispositivo non ha una localizzazione
                            break;
                    }
                }
            }
        });

    }

    private void startLocationUpdates(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Log.d(TAG, "prima del if");
                    if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                        int index = locationResult.getLocations().size() - 1;
                        latitude = locationResult.getLocations().get(index).getLatitude();
                        longitude = locationResult.getLocations().get(index).getLongitude();
                        if (recyclerViewEventsAdapterNearYou != null) {
                            recyclerViewEventsAdapterNearYou.updateLocation(latitude, longitude);
                        }

                        if (recyclerViewEventsAdapter != null) {
                            recyclerViewEventsAdapter.updateLocation(latitude, longitude);
                        }

                        //salvo tutto su firebase sul campo Position
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference userLatitude = FirebaseDatabase.getInstance().getReference("users").child(userId).child("Positions").child("Latitude");
                        DatabaseReference userLongitude = FirebaseDatabase.getInstance().getReference("users").child(userId).child("Positions").child("Longitude");
                        userLongitude.setValue(longitude);
                        userLatitude.setValue(latitude);

                        Log.d(TAG, "latitudine: " + latitude.toString() + " longitudine: " + longitude.toString());
                        fusedLocationClient.removeLocationUpdates(this); // Rimuovo l'ascoltatore
                    }
                }
            };
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            if (isGPSEnabled()) {
                startLocationUpdates(requireContext());
            } else {
                turnOnGPS();
            }
        } else {
            Snackbar.make(rootView.findViewById(android.R.id.content), "I permessi sono necessari per ottenere la posizione", Snackbar.LENGTH_LONG).setAction("OK", v -> {}).show();
        }
    });
    private static String getTodayDateString() {
        LocalDateTime currentDateTime = null;
        String formattedDateTime = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

            formattedDateTime = currentDateTime.format(formatter);
        }

        return formattedDateTime;
    }

    public static String getDateInSomeWeeks() {
        LocalDateTime currentDateTime = null;
        String formattedDateTime = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDateTime = LocalDateTime.now();
            LocalDateTime futureDateTime = currentDateTime.plusWeeks(WEEKS_OF_EVENT_SEARCH);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

            formattedDateTime = futureDateTime.format(formatter);
        }

        return formattedDateTime;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationCallback != null) {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}

