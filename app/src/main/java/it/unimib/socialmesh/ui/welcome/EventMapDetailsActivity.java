package it.unimib.socialmesh.ui.welcome;

import static it.unimib.socialmesh.util.Constants.FIREBASE_REALTIME_DATABASE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.data.service.FirebaseEvent;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.util.FireBaseUtil;

public class EventMapDetailsActivity extends AppCompatActivity {
ImageView imageViewEvent;
TextView eventTitle, eventDate, eventPlace;
Button joinButton;
ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_map_details);
        imageViewEvent=findViewById(R.id.imageview_event);
        eventTitle = findViewById(R.id.textview_event_title);
        eventDate = findViewById(R.id.textview_event_date);
        eventPlace = findViewById(R.id.textview_event_place);
        joinButton= findViewById(R.id.join_button);
        backButton = findViewById(R.id.back_btn);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("EVENT_KEY")) {
            Event currentEvent = bundle.getParcelable("EVENT_KEY");
            Log.d("MapDetails",currentEvent.getName());
            CircularProgressDrawable drawable = new CircularProgressDrawable(this);
            drawable.setColorSchemeColors(R.color.md_theme_light_primary, R.color.md_theme_dark_primary, R.color.md_theme_dark_inversePrimary);
            drawable.setCenterRadius(30f);
            drawable.setStrokeWidth(5f);
            drawable.start();
            Glide.with(this)
                    .load(currentEvent.getMainUrlImage())
                    .placeholder(drawable)
                    .error(drawable)
                    .into(imageViewEvent);

            eventTitle.setText(currentEvent.getName1());
            eventDate.setText(currentEvent.getDateAndTime());
            eventPlace.setText(currentEvent.getPlaceNameMap());
            joinButton.setOnClickListener(v -> {

                if (userIsAuthenticated()) {
                    String userId = FireBaseUtil.currentUserId();
                    if (userId != null && !userId.isEmpty()) {
                        String eventId = currentEvent.getRemoteId();
                        if (eventId != null && !eventId.isEmpty()) {
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE);
                            DatabaseReference eventRef = firebaseDatabase.getInstance().getReference().child("events").child(eventId);
                            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference userRef = firebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
                            DatabaseReference userEventsRef = userRef.child("events");
                            userEventsRef.child(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        userEventsRef.child(eventId).setValue(true);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            eventRef.child("participants").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        uploadEventsToFirebase(currentEvent, userId);
                                        //Snackbar.make(view, "Iscrizione effettuata correttamente", Snackbar.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });

                        }
                    }
                }
            });





        }
        backButton.setOnClickListener(view -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
    }
    private boolean userIsAuthenticated() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        return user != null;
    }
    private void uploadEventsToFirebase(Event apiEvent, String userId) {

        DatabaseReference eventsRef = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE).getReference().child("events");

        Log.d("detailsfragment", "sto caricando");
        DatabaseReference eventRef = eventsRef.child(String.valueOf(apiEvent.getRemoteId()));
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    FirebaseEvent firebaseEvent = new FirebaseEvent(
                            apiEvent.getRemoteId(),
                            apiEvent.getName(),
                            apiEvent.getLocalId(),
                            Arrays.asList(),
                            apiEvent.getUrlImages()
                    );
                    eventRef.setValue(firebaseEvent);
                }
                eventRef.child("participants").child(userId).setValue(true);
            }  @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("detailsfragment", "uploadEventsToFirebase onCancelled", error.toException());
            }
        });
    }
}