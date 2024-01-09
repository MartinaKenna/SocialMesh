package it.unimib.socialmesh.ui.main;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.adapter.RecyclerViewEventsAdapter;
import it.unimib.socialmesh.model.Event;

public class HomeActivity extends AppCompatActivity {
    private LocationRequest locationRequest;
    RecyclerViewEventsAdapter adapter;
    private static final String TAG = EventFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.fragmentContainerView2);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navController);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //priorità della precisione della posizione
        locationRequest.setInterval(5000); //intervallo di aggiornamento
        locationRequest.setFastestInterval(2000); //altro intervallo di aggiornamento
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult();


                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                        usersRef.child(currentUserId).child("token").setValue(token)
                                .addOnSuccessListener(aVoid -> {
                                    // Token FCM salvato con successo nel database
                                })
                                .addOnFailureListener(e -> {
                                    // Gestisci errori nel salvataggio del token nel database
                                });
                    } else {
                        // Gestisci eventuali errori nell'ottenere il token
                    }
                });

        //definisco la struttura per ottenere la localizzazione
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //i permessi sono stati accettati dall'utente

                if (isGPSEnabled()) {
                    //se il gps è on e i permessi sono stati accettati dall'utente
                    //si aggiorna la posizione tramite getFusedLocationProviderClient
                    LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(HomeActivity.this).removeLocationUpdates(this);
                            boolean primo = locationResult!=null;
                            boolean secondo = !locationResult.getLocations().isEmpty();
                            Log.d(TAG, "condizione if: " + primo + "  " + secondo);
                            if(locationResult != null && !locationResult.getLocations().isEmpty()){
                                //Setto la longitudine e latitudine
                                int index = locationResult.getLocations().size() - 1;
                                double latitude = locationResult.getLocations().get(index).getLatitude();
                                double longitude = locationResult.getLocations().get(index).getLongitude();
                                Log.d(TAG, "latitudine: " + latitude + "/n longitudine: " + longitude);
                                adapter.filterByPosition(latitude, longitude);
                                //QUI POSSO SETTARE LE TEXTVIEW COSI DA OTTENERE LA POSIZIONE
                            }
                        }
                    }, Looper.getMainLooper());
                } else { //altrimenti si richiede all'utente di attivarlo
                    Log.d(TAG, "dentro richiesta di attivare il gps");
                    turnOnGPS();
                }
            } else {// i permessi non sono stati accettati dall'utente
                //si richiede di accettare i permessi
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                Log.d(TAG, "dentro alla richiesta permessi");
            }
        }
    }
    private boolean isGPSEnabled() {
        //controllo se il GPS è on
        LocationManager locationManager = (LocationManager) HomeActivity.this.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    private void turnOnGPS() {

        //definisco la richiesta e inserisco di richiedere sempre il permesso di attivare il GPS nel caso in cui è disattivato
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);builder.setAlwaysShow(true);

        //controlliamo le impostazioni del GPS e se è attivo
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(HomeActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            //se il GPS è gia attivo viene richiesto di modificare le impostazioni di esso

                            try {
                                //viene richiesto all'utente di modificare le impostazioni
                                ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                                resolvableApiException.startResolutionForResult(HomeActivity.this,1);
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
}


