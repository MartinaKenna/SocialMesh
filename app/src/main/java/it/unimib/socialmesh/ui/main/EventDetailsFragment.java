package it.unimib.socialmesh.ui.main;
import static it.unimib.socialmesh.util.Constants.FIREBASE_REALTIME_DATABASE;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.databinding.FragmentEventDetailsBinding;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.data.service.FirebaseEvent;
import it.unimib.socialmesh.util.FireBaseUtil;
import it.unimib.socialmesh.util.LocaleManager;

public class EventDetailsFragment extends Fragment {

    private Event currentEvent;
    private boolean isUserSubscribed = false;
    private FragmentEventDetailsBinding fragmentEventDetailsBinding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentEventDetailsBinding = FragmentEventDetailsBinding.inflate(inflater, container, false);
        return fragmentEventDetailsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstance) {
        // Recupera l'evento dalla navigazione
        if (getArguments() != null) {
            currentEvent = EventDetailsFragmentArgs.fromBundle(getArguments()).getEvent();
        }

        if (currentEvent != null) {
            fragmentEventDetailsBinding.joinButton.requestLayout();
            CircularProgressDrawable drawable = new CircularProgressDrawable(getContext());
            drawable.setColorSchemeColors(R.color.md_theme_light_primary, R.color.md_theme_dark_primary, R.color.md_theme_dark_inversePrimary);
            drawable.setCenterRadius(30f);
            drawable.setStrokeWidth(5f);
            drawable.start();

            Glide.with(this)
                    .load(currentEvent.getUrlImagesHD())
                    .placeholder(drawable)
                    .error(drawable)
                    .into(fragmentEventDetailsBinding.imageviewEvent);

            fragmentEventDetailsBinding.textviewEventTitle.setText(currentEvent.getName1());
            fragmentEventDetailsBinding.textviewEventDate.setText(currentEvent.getLocalDateAndTime());
            fragmentEventDetailsBinding.textviewEventPlace.setText(currentEvent.getPlaceName());
            checkSubscriptionStatus();

            fragmentEventDetailsBinding.backBtn.setOnClickListener(CloseView -> {
                getParentFragmentManager().popBackStack();
            });

            fragmentEventDetailsBinding.joinButton.setOnClickListener(v -> {
                if (userIsAuthenticated()) {
                    toggleSubscriptionStatus();
                }
            });

        }
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("detailsfragment", "uploadEventsToFirebase onCancelled", error.toException());
            }
        });
    }

    private boolean userIsAuthenticated() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        return user != null;
    }

    private void checkSubscriptionStatus() {
        String userId = FireBaseUtil.currentUserId();
        if (userId != null && !userId.isEmpty()) {
            String eventId = currentEvent.getRemoteId();
            if (eventId != null && !eventId.isEmpty()) {
                DatabaseReference userEventsRef = FireBaseUtil.getUserRef(userId).child("events").child(eventId);

                userEventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            isUserSubscribed = true;
                            fragmentEventDetailsBinding.joinButton.setText(R.string.dismiss_button_text);
                        } else {
                            isUserSubscribed = false;
                            fragmentEventDetailsBinding.joinButton.setText(R.string.join_button_text);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        }
    }
    private void toggleSubscriptionStatus() {
        String userId = FireBaseUtil.currentUserId();
        if (userId != null && !userId.isEmpty()) {
            String eventId = currentEvent.getRemoteId();
            if (eventId != null && !eventId.isEmpty()) {
                DatabaseReference userEventsRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("events").child(eventId);
                userEventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            userEventsRef.removeValue();
                        } else {
                            uploadEventsToFirebase(currentEvent, userId);
                            userEventsRef.setValue(true);
                        }

                        checkSubscriptionStatus();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        }
    }


}