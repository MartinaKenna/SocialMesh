package it.unimib.socialmesh.ui.welcome;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreferencesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreferencesFragment extends Fragment {

    private Button register, culture, sport, cinema, aperitivo, party, smoker, traveler, lgbt,
            karaoke, nft, boxe, festival, crossfit, nature, beach, motosport, instagram,
            twitter, socialmedia, horror, action, love, cooking, photography, painting,
            hiking, streaming, gardening, writing, modelling, programming, vegetarian, vegan,
            carnivore, parent, single, engaged, married, navigate, blogging, city;


    public PreferencesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreferencesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreferencesFragment newInstance(String param1, String param2) {
        PreferencesFragment fragment = new PreferencesFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vieww = inflater.inflate(R.layout.preferences_fragment, container, false);
        culture = vieww.findViewById(R.id.button_culture);
        sport = vieww.findViewById(R.id.button_sport);
        cinema = vieww.findViewById(R.id.button_cinema);
        aperitivo = vieww.findViewById(R.id.button_aperitivo);
        party = vieww.findViewById(R.id.button_party);
        smoker = vieww.findViewById(R.id.button_smoker);
        traveler = vieww.findViewById(R.id.button_traveler);
        lgbt = vieww.findViewById(R.id.button_lgbt);
        karaoke = vieww.findViewById(R.id.button_karaoke);
        nft = vieww.findViewById(R.id.button_nft);
        boxe = vieww.findViewById(R.id.button_boxe);
        festival = vieww.findViewById(R.id.button_festival);
        crossfit = vieww.findViewById(R.id.button_crossfit);
        nature = vieww.findViewById(R.id.button_nature);
        beach = vieww.findViewById(R.id.button_beach);
        motosport = vieww.findViewById(R.id.button_motosport);
        instagram = vieww.findViewById(R.id.button_instagram);
        twitter = vieww.findViewById(R.id.button_twitter);
        socialmedia = vieww.findViewById(R.id.button_socialmedia);
        horror = vieww.findViewById(R.id.button_horror);
        action = vieww.findViewById(R.id.button_action);
        love = vieww.findViewById(R.id.button_love);
        cooking = vieww.findViewById(R.id.button_cooking);
        photography = vieww.findViewById(R.id.button_photography);
        painting = vieww.findViewById(R.id.button_painting);
        hiking = vieww.findViewById(R.id.button_hiking);
        streaming = vieww.findViewById(R.id.button_streaming);
        gardening = vieww.findViewById(R.id.button_gardening);
        programming = vieww.findViewById(R.id.button_programming);
        writing = vieww.findViewById(R.id.button_writing);
        modelling = vieww.findViewById(R.id.button_modeling);
        vegetarian = vieww.findViewById(R.id.button_vegetarian);
        vegan = vieww.findViewById(R.id.button_vegan);
        carnivore = vieww.findViewById(R.id.button_carnivore);
        parent = vieww.findViewById(R.id.button_parent);
        single = vieww.findViewById(R.id.button_single);
        married = vieww.findViewById(R.id.button_married);
        engaged = vieww.findViewById(R.id.button_engaged);
        city = vieww.findViewById(R.id.button_city);
        navigate = vieww.findViewById(R.id.button_navigate);
        blogging = vieww.findViewById(R.id.button_blogging);


        navigate.setOnClickListener(v -> {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            List<String> preferences = new ArrayList<>();

            if (culture.isSelected()) {
                preferences.add("Culture");
            }
            if (sport.isSelected()) {
                preferences.add("Sport");
            }
            if (cinema.isSelected()) {
                preferences.add("Cinema");
            }
            if (aperitivo.isSelected()) {
                preferences.add("Aperitivo");
            }
            if (party.isSelected()) {
                preferences.add("Party");
            }
            if (smoker.isSelected()) {
                preferences.add("Smoker");
            }
            if (true) {
                preferences.add("Traveler");
            }
            if (lgbt.isSelected()) {
                preferences.add("LGBTQIA+");
            }
            if (karaoke.isSelected()) {
                preferences.add("Karaoke");
            }
            if (nft.isSelected()) {
                preferences.add("NFT");
            }
            if (boxe.isSelected()) {
                preferences.add("Boxe");
            }
            if (festival.isSelected()) {
                preferences.add("Festival");
            }
            if (crossfit.isSelected()) {
                preferences.add("Crossfit");
            }
            if (nature.isSelected()) {
                preferences.add("Nature");
            }
            if (beach.isSelected()) {
                preferences.add("Beach");
            }
            if (motosport.isSelected()) {
                preferences.add("Motorsport");
            }
            if (instagram.isSelected()) {
                preferences.add("Instagram");
            }
            if (twitter.isSelected()) {
                preferences.add("Twitter");
            }
            if (socialmedia.isSelected()) {
                preferences.add("Social Media");
            }
            if (horror.isSelected()) {
                preferences.add("Horror");
            }
            if (action.isSelected()) {
                preferences.add("Action");
            }
            if (love.isSelected()) {
                preferences.add("Love");
            }
            if (cooking.isSelected()) {
                preferences.add("Cooking");
            }
            if (photography.isSelected()) {
                preferences.add("Photography");
            }
            if (painting.isSelected()) {
                preferences.add("Painting");
            }
            if (hiking.isSelected()) {
                preferences.add("Hiking");
            }
            if (streaming.isSelected()) {
                preferences.add("Streaming");
            }
            if (gardening.isSelected()) {
                preferences.add("Gardening");
            }
            if (programming.isSelected()) {
                preferences.add("Programming");
            }
            if (writing.isSelected()) {
                preferences.add("Writing");
            }
            if (modelling.isSelected()) {
                preferences.add("Modelling");
            }
            if (vegetarian.isSelected()) {
                preferences.add("Vegetarian");
            }
            if (vegan.isSelected()) {
                preferences.add("Vegan");
            }
            if (carnivore.isSelected()) {
                preferences.add("Carnivore");
            }
            if (parent.isSelected()) {
                preferences.add("Parent");
            }
            if (single.isSelected()) {
                preferences.add("Single");
            }
            if (married.isSelected()) {
                preferences.add("Married");
            }
            if (engaged.isSelected()) {
                preferences.add("Engaged");
            }
            if (city.isSelected()) {
                preferences.add("City");
            }
            if (navigate.isSelected()) {
                preferences.add("Navigate");
            }
            if (blogging.isSelected()) {
                preferences.add("Blogging");
            }


            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("preferences");
            userRef.setValue(preferences)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Preferenze salvate con successo!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Errore nel salvataggio delle preferenze", Toast.LENGTH_SHORT).show();
                        }
                    });
            Navigation.findNavController(requireView()).navigate(R.id.navigate_to_homeActivity);
        });
        return inflater.inflate(R.layout.preferences_fragment, container, false);
    }
}