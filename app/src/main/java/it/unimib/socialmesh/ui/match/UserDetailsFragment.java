package it.unimib.socialmesh.ui.match;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager2.widget.ViewPager2;

import android.view.View;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.adapter.InterestsAdapter;
import it.unimib.socialmesh.adapter.PhotosAdapter;
import it.unimib.socialmesh.adapter.PhotosViewPagerAdapter;
import it.unimib.socialmesh.databinding.UserDetailsFragmentBinding;
import it.unimib.socialmesh.model.User;

public class UserDetailsFragment extends Fragment {
    private AtomicInteger msgId = new AtomicInteger();
    private UserDetailsFragmentBinding userDetailsFragmentBinding;
    private boolean isLiked = false;
    private String otherUserId;

    private InterestsAdapter interestsAdapter;

    private DatabaseReference databaseReference;
    private List<String> interestsList = new ArrayList<>();
    private ViewPager2 viewPager;
    private PhotosAdapter photosAdapter;

    private ImageView profile_pic;
    private List<Uri> photoUrls = new ArrayList<>();
    int age;
    public UserDetailsFragment() {}

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userDetailsFragmentBinding = UserDetailsFragmentBinding.inflate(inflater, container, false);
        return userDetailsFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);

        if (getArguments() != null) {
            otherUserId = UserDetailsFragmentArgs.fromBundle(getArguments()).getUserId();
        }

        //loadProfileImage(otherUserId);
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(otherUserId);
        DatabaseReference currentUserLikesRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUserId)
                .child("likes");
        userDetailsFragmentBinding.buttonBack.setOnClickListener(CloseView -> {
            getParentFragmentManager().popBackStack();
        });
        userDetailsFragmentBinding.buttonLike.setOnClickListener(v -> {
            if (isLiked) {
                userDetailsFragmentBinding.buttonLike.setImageResource(R.drawable.baseline_favorite_border_black_48dp);
                removeLike(currentUserId,otherUserId);
                Snackbar.make(requireActivity().findViewById(android.R.id.content), "Like rimosso correttamente", Snackbar.LENGTH_SHORT).show();
            } else {
                userDetailsFragmentBinding.buttonLike.setImageResource(R.drawable.baseline_favorite_red_48dp);
                addLike(currentUserId,otherUserId, requireActivity().findViewById(android.R.id.content));
                Snackbar.make(requireActivity().findViewById(android.R.id.content), "Like aggiunto correttamente", Snackbar.LENGTH_SHORT).show();
            }
            isLiked = !isLiked;
        });

        currentUserLikesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(otherUserId)) {
                    userDetailsFragmentBinding.buttonLike.setImageResource(R.drawable.baseline_favorite_red_48dp);
                    isLiked = true;
                }else{
                    userDetailsFragmentBinding.buttonLike.setImageResource(R.drawable.baseline_favorite_border_black_48dp);
                    isLiked = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.child("name").getValue(String.class);
                    String dateOfBirth = dataSnapshot.child("data_di_nascita").getValue(String.class);

                    if (dateOfBirth != null) {
                        age = calculateAge(dateOfBirth);
                    }
                    userDetailsFragmentBinding.textviewAge.setText(String.valueOf(age));
                    userDetailsFragmentBinding.textviewName.setText(userName);
                    updateDescription(otherUserId);
                    updateInterests(otherUserId, requireActivity().findViewById(android.R.id.content));
                    retrieveImagesFromStorage(otherUserId, requireActivity().findViewById(android.R.id.content));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void addLike(String currentUserID, String likedUserID,View view) {
        DatabaseReference currentUserLikesRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUserID)
                .child("likes");

        currentUserLikesRef.child(likedUserID).setValue(true);
        checkForMatch(currentUserID, likedUserID,view);
    }
    private void checkForMatch(final String currentUserID, final String otherUserID,View view) {
        DatabaseReference otherUserLikesRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(otherUserID)
                .child("likes");

        otherUserLikesRef.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && (boolean) dataSnapshot.getValue()) {
                    createMatch(currentUserID, otherUserID);
                    sendMatchNotificationToUsers(currentUserID, otherUserID);
                    Snackbar.make(view, "MATCH!", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMatchNotificationToUsers(String user1ID, String user2ID) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Ottieni i token FCM dei due utenti dal tuo database
        usersRef.child(user1ID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String tokenUser1 = dataSnapshot.getValue(String.class);
                    sendNotification(tokenUser1, "Match!", "Hai un nuovo match!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gestione degli errori
            }
        });

        usersRef.child(user2ID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String tokenUser2 = dataSnapshot.getValue(String.class);
                    sendNotification(tokenUser2, "Match!", "Hai un nuovo match!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gestione degli errori
            }
        });
    }

    private void sendNotification(String token, String title, String body) {
        // Costruisci il payload della notifica
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("title", title);
        notificationData.put("body", body);

        // Invia la notifica utilizzando il token FCM
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(token)
                .setMessageId(Integer.toString(msgId.incrementAndGet()))
                .setData(notificationData)
                .build());
    }


    private void createMatch(String currentUserID, String matchedUserID) {
        DatabaseReference currentUserMatchesRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUserID)
                .child("matches");

        DatabaseReference matchedUserMatchesRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(matchedUserID)
                .child("matches");

        currentUserMatchesRef.child(matchedUserID).setValue(true);
        matchedUserMatchesRef.child(currentUserID).setValue(true);
    }

    private void removeLike(String currentUserID, String otherUserID) {
        DatabaseReference currentUserLikesRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUserID)
                .child("likes")
                .child(otherUserID);

        currentUserLikesRef.removeValue();

        DatabaseReference otherUserLikesRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(otherUserID)
                .child("likes")
                .child(currentUserID);

        otherUserLikesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DatabaseReference currentUserMatchesRef = FirebaseDatabase.getInstance().getReference()
                            .child("users")
                            .child(currentUserID)
                            .child("matches")
                            .child(otherUserID);

                    currentUserMatchesRef.removeValue();

                    DatabaseReference matchedUserMatchesRef = FirebaseDatabase.getInstance().getReference()
                            .child("users")
                            .child(otherUserID)
                            .child("matches")
                            .child(currentUserID);

                    matchedUserMatchesRef.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
    private void updateDescription(String currentUserId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUserId);

        userRef.child("descrizione").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String description = snapshot.getValue(String.class);
                    userDetailsFragmentBinding.textviewDescription.setText(description);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadProfileImage(String otherUserID) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference userRef = storageRef.child("pictures").child(otherUserID).child("profilePic.jpg");

        userRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageURL = uri.toString();
            CircularProgressDrawable drawable = new CircularProgressDrawable(getContext());
            drawable.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
            drawable.setCenterRadius(30f);
            drawable.setStrokeWidth(5f);
            drawable.start();

            if (imageURL != null && !imageURL.isEmpty()) {
                    Glide.with(this)
                            .load(imageURL)
                            .placeholder(drawable)
                            .error(R.drawable.drawable)
                            .into(profile_pic);


            } else {
                    Glide.with(this)
                            .load(com.facebook.R.drawable.com_facebook_profile_picture_blank_portrait) // Immagine di default
                            .into(profile_pic);

            }
        }).addOnFailureListener(exception -> {
            Log.e("Settings", "Errore durante il caricamento dell'immagine del profilo: " + exception.getMessage());
        });
    }

    private int calculateAge(String dateOfBirth) {
        int age = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date birthDate = sdf.parse(dateOfBirth);
            Calendar dob = Calendar.getInstance();
            Calendar today = Calendar.getInstance();

            dob.setTime(birthDate);

            int yearDiff = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            int currentMonth = today.get(Calendar.MONTH) + 1;
            int birthMonth = dob.get(Calendar.MONTH) + 1;
            int currentDay = today.get(Calendar.DAY_OF_MONTH);
            int birthDay = dob.get(Calendar.DAY_OF_MONTH);

            if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
                age = yearDiff - 1;
            } else {
                age = yearDiff;
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return age;
    }
    private void updateInterests(String currentUserId,View view) {
        userDetailsFragmentBinding.recyclerInterests.setLayoutManager(new LinearLayoutManager(getContext()));
        interestsAdapter = new InterestsAdapter(interestsList);
        userDetailsFragmentBinding.recyclerInterests.setAdapter(interestsAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("preferences");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                interestsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String interest = snapshot.getValue(String.class);
                    interestsList.add(interest);
                }
                interestsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gestisci eventuali errori di lettura dal database
            }
        });
    }
    private void retrieveImagesFromStorage(String currentUserId, View view) {
        viewPager = view.findViewById(R.id.imageview_profile); // Sostituisci con l'ID reale del ViewPager2 nel layout XML
        List<Uri> photoUrls = new ArrayList<>();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("pictures").child(currentUserId);

        storageRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    photoUrls.add(uri);

                    if (photoUrls.size() == listResult.getItems().size()) {
                        // Se abbiamo raccolto tutti gli URI, procediamo con l'ordinamento
                        Uri profilePicUri = null;

                        for (int i = 0; i < photoUrls.size(); i++) {
                            if (photoUrls.get(i).toString().contains("profilePic.jpg")) {
                                profilePicUri = photoUrls.remove(i);
                                break;
                            }
                        }

                        if (profilePicUri != null) {
                            photoUrls.add(0, profilePicUri); // Metti profilePic.jpg all'inizio della lista
                        }

                        // Crea e setta l'adapter solo dopo aver completato l'ordinamento degli URI
                        PhotosViewPagerAdapter adapter = new PhotosViewPagerAdapter(getContext(), photoUrls);
                        viewPager.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(exception -> {
                    // Gestisci eventuali errori nel recupero delle URL
                });
            }
        }).addOnFailureListener(e -> {
            // Gestisci eventuali errori nel recupero delle immagini dallo storage
        });
    }

}


