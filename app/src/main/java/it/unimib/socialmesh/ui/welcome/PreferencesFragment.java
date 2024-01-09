package it.unimib.socialmesh.ui.welcome;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.data.repository.user.IUserRepository;
import it.unimib.socialmesh.util.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreferencesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreferencesFragment extends Fragment implements View.OnClickListener{

    DatabaseReference userPreferencesRef;
    private UserViewModel userViewModel;

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
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(
                this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        //salvo il nome del button cliccato in una lista
        if (v == cinema) {
            userViewModel.addPreference("Cinema");
        } else if (v == party) {
            userViewModel.addPreference("Party");
        } else if (v == viaggiLowCost) {
            userViewModel.addPreference("ViaggiLowCost");
        } else if (v == lgbt) {
            userViewModel.addPreference("LGBT");
        } else if (v == karaoke) {
            userViewModel.addPreference("Karaoke");
        } else if (v == nft) {
            userViewModel.addPreference("NFT");
        } else if (v == boxe) {
            userViewModel.addPreference("Boxe");
        } else if (v == festival) {
            userViewModel.addPreference("Festival");
        } else if (v == crossfit) {
            userViewModel.addPreference("Crossfit");
        } else if (v == nature) {
            userViewModel.addPreference("Nature");
        } else if (v == beach) {
            userViewModel.addPreference("Spiaggia");
        } else if (v == motorsport) {
            userViewModel.addPreference("Motorsport");
        } else if (v == instagram) {
            userViewModel.addPreference("Instagram");
        } else if (v == twitter) {
            userViewModel.addPreference("Twitter");
        } else if (v == photography) {
            userViewModel.addPreference("Fotografia");
        } else if (v == painting) {
            userViewModel.addPreference("Pittura");
        } else if (v == escursioni) {
            userViewModel.addPreference("Escursioni");
        } else if (v == gardening) {
            userViewModel.addPreference("Giardinaggio");
        } else if (v == programming) {
            userViewModel.addPreference("Programmatore");
        } else if (v == writing) {
            userViewModel.addPreference("Scrittura");
        } else if (v == moda) {
            userViewModel.addPreference("Moda");
        } else if (v == vegetarian) {
            userViewModel.addPreference("Vegetariano");
        } else if (v == vegan) {
            userViewModel.addPreference("Vegan");
        } else if (v == carnivore) {
            userViewModel.addPreference("Carnivoro");
        } else if (v == single) {
            userViewModel.addPreference("Single");
        } else if (v == married) {
            userViewModel.addPreference("Sposato");
        } else if (v == engaged) {
            userViewModel.addPreference("Impegnato");
        } else if (v == cucinaItaliana) {
            userViewModel.addPreference("Cucina Italiana");
        } else if (v == blogger) {
            userViewModel.addPreference("Blogger");
        } else if (v == basket) {
            userViewModel.addPreference("Basket");
        } else if (v == fumatore) {
            userViewModel.addPreference("Fumatore");
        } else if (v == facebook) {
            userViewModel.addPreference("Facebook");
        } else if (v == freelance) {
            userViewModel.addPreference("Freelance");
        } else if (v == imprenditoria) {
            userViewModel.addPreference("Imprenditoria");
        } else if (v == elettricista) {
            userViewModel.addPreference("Elettricista");
        } else if (v == serieTV) {
            userViewModel.addPreference("Serie TV");
        } else if (v == libri) {
            userViewModel.addPreference("Libri");
        } else if (v == tecnologia) {
            userViewModel.addPreference("Tecnologia");
        } else if (v == destEsotiche) {
            userViewModel.addPreference("Destinazioni Esotiche");
        } else if (v == rock) {
            userViewModel.addPreference("Rock");
        } else if (v == classica) {
            userViewModel.addPreference("Classica");
        } else if (v == pop) {
            userViewModel.addPreference("Pop");
        } else if (v == ricette) {
            userViewModel.addPreference("Ricette");
        } else if (v == dolce) {
            userViewModel.addPreference("Dolce");
        } else if (v == calcio) {
            userViewModel.addPreference("Calcio");
        } else if (v == tennis) {
            userViewModel.addPreference("Tennis");
        } else if (v == yoga) {
            userViewModel.addPreference("Yoga");
        } else if (v == palestra) {
            userViewModel.addPreference("Palestra");
        }
    }



}