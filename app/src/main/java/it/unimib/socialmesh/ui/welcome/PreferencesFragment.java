package it.unimib.socialmesh.ui.welcome;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
public class PreferencesFragment extends Fragment implements View.OnClickListener{

    DatabaseReference userPreferencesRef;
    private Button cinema, party, fumatore, viaggiLowCost, lgbt,
            karaoke, nft, boxe, festival, crossfit, nature, beach, motorsport, instagram,
            twitter, photography, painting, escursioni, gardening, writing, moda, programming,
            vegetarian, vegan, carnivore, single, engaged, married, blogger, cucinaItaliana,
            basket, facebook, navigate, freelance, imprenditoria, tecnologia, tennis, calcio,
            destEsotiche, serieTV, libri, rock, classica, pop, ricette, dolce, yoga, palestra,
            elettricista;
    List<String> preferences = new ArrayList<>();
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
        View view = inflater.inflate(R.layout.preferences_fragment, container, false);
        cinema = view.findViewById(R.id.button_cinema);
        party = view.findViewById(R.id.button_party);
        viaggiLowCost = view.findViewById(R.id.button_viaggi_lowCost);
        lgbt = view.findViewById(R.id.button_lgbt);
        karaoke = view.findViewById(R.id.button_karaoke);
        nft = view.findViewById(R.id.button_nft);
        boxe = view.findViewById(R.id.button_boxe);
        festival = view.findViewById(R.id.button_festival);
        crossfit = view.findViewById(R.id.button_crossfit);
        nature = view.findViewById(R.id.button_nature);
        beach = view.findViewById(R.id.button_beach);
        motorsport = view.findViewById(R.id.button_motorsport);
        instagram = view.findViewById(R.id.button_instagram);
        twitter = view.findViewById(R.id.button_twitter);
        photography = view.findViewById(R.id.button_fotografia);
        painting = view.findViewById(R.id.button_pittura);
        escursioni = view.findViewById(R.id.button_escursioni);
        gardening = view.findViewById(R.id.button_giardinaggio);
        programming = view.findViewById(R.id.button_programmatore);
        writing = view.findViewById(R.id.button_scrittura);
        moda = view.findViewById(R.id.button_moda);
        vegetarian = view.findViewById(R.id.button_vegetariano);
        vegan = view.findViewById(R.id.button_vegan);
        carnivore = view.findViewById(R.id.button_carnivoro);
        single = view.findViewById(R.id.button_single);
        married = view.findViewById(R.id.button_married);
        engaged = view.findViewById(R.id.button_impegnato);
        cucinaItaliana = view.findViewById(R.id.button_cucina_italiana);
        blogger = view.findViewById(R.id.button_blogger);
        basket = view.findViewById(R.id.button_basket);
        fumatore = view.findViewById(R.id.button_fumatore);
        facebook = view.findViewById(R.id.button_facebook);
        navigate = view.findViewById(R.id.button_navigate);
        freelance = view.findViewById(R.id.button_freelance);
        imprenditoria = view.findViewById(R.id.button_imprenditoria);
        elettricista = view.findViewById(R.id.button_elettricista);
        serieTV = view.findViewById(R.id.button_serieTV);
        libri = view.findViewById(R.id.button_libri);
        tecnologia = view.findViewById(R.id.button_tecnologia);
        destEsotiche = view.findViewById(R.id.button_destinazioni_esotiche);
        rock = view.findViewById(R.id.button_rock);
        classica = view.findViewById(R.id.button_classica);
        pop = view.findViewById(R.id.button_pop);
        ricette = view.findViewById(R.id.button_ricette);
        dolce = view.findViewById(R.id.button_dolce);
        calcio = view.findViewById(R.id.button_calcio);
        tennis = view.findViewById(R.id.button_tennis);
        yoga = view.findViewById(R.id.button_yoga);
        palestra = view.findViewById(R.id.button_palestra);

        //definisco i setOnClickListener(this) in modo tale che utilizzi il metodo OnClick(v) quando vengono premuti
        cinema.setOnClickListener(this);
        party.setOnClickListener(this);
        viaggiLowCost.setOnClickListener(this);
        lgbt.setOnClickListener(this);
        karaoke.setOnClickListener(this);
        nft.setOnClickListener(this);
        boxe.setOnClickListener(this);
        festival.setOnClickListener(this);
        crossfit.setOnClickListener(this);
        nature.setOnClickListener(this);
        beach.setOnClickListener(this);
        motorsport.setOnClickListener(this);
        instagram.setOnClickListener(this);
        twitter.setOnClickListener(this);
        photography.setOnClickListener(this);
        painting.setOnClickListener(this);
        escursioni.setOnClickListener(this);
        gardening.setOnClickListener(this);
        programming.setOnClickListener(this);
        writing.setOnClickListener(this);
        moda.setOnClickListener(this);
        vegetarian.setOnClickListener(this);
        vegan.setOnClickListener(this);
        carnivore.setOnClickListener(this);
        single.setOnClickListener(this);
        married.setOnClickListener(this);
        engaged.setOnClickListener(this);
        cucinaItaliana.setOnClickListener(this);
        blogger.setOnClickListener(this);
        basket.setOnClickListener(this);
        fumatore.setOnClickListener(this);
        facebook.setOnClickListener(this);
        navigate.setOnClickListener(this);
        freelance.setOnClickListener(this);
        imprenditoria.setOnClickListener(this);
        elettricista.setOnClickListener(this);
        serieTV.setOnClickListener(this);
        libri.setOnClickListener(this);
        tecnologia.setOnClickListener(this);
        destEsotiche.setOnClickListener(this);
        rock.setOnClickListener(this);
        classica.setOnClickListener(this);
        pop.setOnClickListener(this);
        ricette.setOnClickListener(this);
        dolce.setOnClickListener(this);
        calcio.setOnClickListener(this);
        tennis.setOnClickListener(this);
        yoga.setOnClickListener(this);
        palestra.setOnClickListener(this);



        //prendo l'id dell'utente
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //definisco il campo "preferences" in firebase
        userPreferencesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("preferences");

        //change schermata
        navigate.setOnClickListener(v ->{
            Navigation.findNavController(requireView()).navigate(R.id.navigate_to_homeActivity);
        });


        return view;
    }
    @Override
    public void onClick(View v) {
        //salvo il nome del button cliccato in una lista
        if (v == cinema) {
            savePreference("Cinema");
        } else if (v == party) {
            savePreference("Party");
        } else if (v == viaggiLowCost) {
            savePreference("ViaggiLowCost");
        } else if (v == lgbt) {
            savePreference("LGBT");
        } else if (v == karaoke) {
            savePreference("Karaoke");
        } else if (v == nft) {
            savePreference("NFT");
        } else if (v == boxe) {
            savePreference("Boxe");
        } else if (v == festival) {
            savePreference("Festival");
        } else if (v == crossfit) {
            savePreference("Crossfit");
        } else if (v == nature) {
            savePreference("Nature");
        } else if (v == beach) {
            savePreference("Spiaggia");
        } else if (v == motorsport) {
            savePreference("Motorsport");
        } else if (v == instagram) {
            savePreference("Instagram");
        } else if (v == twitter) {
            savePreference("Twitter");
        } else if (v == photography) {
            savePreference("Fotografia");
        } else if (v == painting) {
            savePreference("Pittura");
        } else if (v == escursioni) {
            savePreference("Escursioni");
        } else if (v == gardening) {
            savePreference("Giardinaggio");
        } else if (v == programming) {
            savePreference("Programmatore");
        } else if (v == writing) {
            savePreference("Scrittura");
        } else if (v == moda) {
            savePreference("Moda");
        } else if (v == vegetarian) {
            savePreference("Vegetariano");
        } else if (v == vegan) {
            savePreference("Vegan");
        } else if (v == carnivore) {
            savePreference("Carnivoro");
        } else if (v == single) {
            savePreference("Single");
        } else if (v == married) {
            savePreference("Sposato");
        } else if (v == engaged) {
            savePreference("Impegnato");
        } else if (v == cucinaItaliana) {
            savePreference("Cucina Italiana");
        } else if (v == blogger) {
            savePreference("Blogger");
        } else if (v == basket) {
            savePreference("Basket");
        } else if (v == fumatore) {
            savePreference("Fumatore");
        } else if (v == facebook) {
            savePreference("Facebook");
        } else if (v == freelance) {
            savePreference("Freelance");
        } else if (v == imprenditoria) {
            savePreference("Imprenditoria");
        } else if (v == elettricista) {
            savePreference("Elettricista");
        } else if (v == serieTV) {
            savePreference("Serie TV");
        } else if (v == libri) {
            savePreference("Libri");
        } else if (v == tecnologia) {
            savePreference("Tecnologia");
        } else if (v == destEsotiche) {
            savePreference("Destinazioni Esotiche");
        } else if (v == rock) {
            savePreference("Rock");
        } else if (v == classica) {
            savePreference("Classica");
        } else if (v == pop) {
            savePreference("Pop");
        } else if (v == ricette) {
            savePreference("Ricette");
        } else if (v == dolce) {
            savePreference("Dolce");
        } else if (v == calcio) {
            savePreference("Calcio");
        } else if (v == tennis) {
            savePreference("Tennis");
        } else if (v == yoga) {
            savePreference("Yoga");
        } else if (v == palestra) {
            savePreference("Palestra");
        }
    }


    private void savePreference(String preference) {
        preferences.add(preference);

        //salvo tutto su firebase
        userPreferencesRef.setValue(preferences)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Preferenza salvata con successo!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Errore nel salvataggio della preferenza", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}