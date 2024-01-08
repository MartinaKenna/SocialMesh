package it.unimib.socialmesh.ui.match;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import it.unimib.socialmesh.R;
public class UserDetailsFragment extends Fragment {

    private boolean isLiked = false;
    private String otherUserId;
    private ImageView profile_pic;
    private TextView textview_description;
    int age;
    public UserDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_details_fragment, container, false);
        profile_pic = view.findViewById(R.id.imageview_profile);
        textview_description = view.findViewById(R.id.textview_description);
        if (getArguments() != null) {
            otherUserId = UserDetailsFragmentArgs.fromBundle(getArguments()).getUserId();
        }
        loadProfileImage(otherUserId);
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        TextView nameTextView = view.findViewById(R.id.textview_name);
        TextView ageTextView = view.findViewById(R.id.textview_age);
        ImageButton backButton = view.findViewById(R.id.button_back);
        ImageButton buttonLike = view.findViewById(R.id.buttonF);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(otherUserId);
        DatabaseReference currentUserLikesRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUserId)
                .child("likes");
        backButton.setOnClickListener(CloseView -> {
            getParentFragmentManager().popBackStack();
        });
        buttonLike.setOnClickListener(v -> {
            if (isLiked) {
                buttonLike.setImageResource(R.drawable.baseline_favorite_border_black_48dp);
                removeLike(currentUserId,otherUserId);
                Snackbar.make(view, "Like rimosso correttamente", Snackbar.LENGTH_SHORT).show();
            } else {
                buttonLike.setImageResource(R.drawable.baseline_favorite_red_48dp);
                addLike(currentUserId,otherUserId,view);
                Snackbar.make(view, "Like aggiunto correttamente", Snackbar.LENGTH_SHORT).show();
            }
            isLiked = !isLiked;
        });

        currentUserLikesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(otherUserId)) {
                    buttonLike.setImageResource(R.drawable.baseline_favorite_red_48dp);
                    isLiked = true;
                }else{
                    buttonLike.setImageResource(R.drawable.baseline_favorite_border_black_48dp);
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
                    ageTextView.setText(String.valueOf(age));
                    nameTextView.setText(userName);
                    updateDescription(otherUserId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return view;
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
                    Snackbar.make(view, "MATCH!", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                    textview_description.setText(description);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadProfileImage(String otherUserID) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
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
                            .error(R.drawable.baseline_error_outline_orange_24dp)
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

}
