package it.unimib.socialmesh.ui.main;

import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.data.repository.user.IUserRepository;
import it.unimib.socialmesh.ui.welcome.UserViewModel;
import it.unimib.socialmesh.ui.welcome.UserViewModelFactory;
import it.unimib.socialmesh.util.ServiceLocator;

public class EditPreferencesActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference userPreferencesRef;
    private UserViewModel userViewModel;

    private Button cinema, party, fumatore, viaggiLowCost, lgbt,
            karaoke, nft, boxe, festival, crossfit, nature, beach, motorsport, instagram,
            twitter, photography, painting, escursioni, gardening, writing, moda, programming,
            vegetarian, vegan, carnivore, single, engaged, married, blogger, cucinaItaliana,
            basket, facebook, navigate, freelance, imprenditoria, tecnologia, tennis, calcio,
            destEsotiche, serieTV, libri, rock, classica, pop, ricette, dolce, yoga, palestra,
            elettricista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_preferences);
        cinema = findViewById(R.id.button_cinema);
        party = findViewById(R.id.button_party);
        viaggiLowCost = findViewById(R.id.button_viaggi_lowCost);
        lgbt = findViewById(R.id.button_lgbt);
        karaoke = findViewById(R.id.button_karaoke);
        nft = findViewById(R.id.button_nft);
        boxe =findViewById(R.id.button_boxe);
        festival = findViewById(R.id.button_festival);
        crossfit = findViewById(R.id.button_crossfit);
        nature = findViewById(R.id.button_nature);
        beach = findViewById(R.id.button_beach);
        motorsport = findViewById(R.id.button_motorsport);
        instagram =  findViewById(R.id.button_instagram);
        twitter =  findViewById(R.id.button_twitter);
        photography = findViewById(R.id.button_fotografia);
        painting =  findViewById(R.id.button_pittura);
        escursioni =  findViewById(R.id.button_escursioni);
        gardening = findViewById(R.id.button_giardinaggio);
        programming = findViewById(R.id.button_programmatore);
        writing = findViewById(R.id.button_scrittura);
        moda = findViewById(R.id.button_moda);
        vegetarian = findViewById(R.id.button_vegetariano);
        vegan = findViewById(R.id.button_vegan);
        carnivore = findViewById(R.id.button_carnivoro);
        single = findViewById(R.id.button_single);
        married = findViewById(R.id.button_married);
        engaged = findViewById(R.id.button_impegnato);
        cucinaItaliana = findViewById(R.id.button_cucina_italiana);
        blogger = findViewById(R.id.button_blogger);
        basket = findViewById(R.id.button_basket);
        fumatore = findViewById(R.id.button_fumatore);
        facebook = findViewById(R.id.button_facebook);
        navigate = findViewById(R.id.button_navigate);
        freelance = findViewById(R.id.button_freelance);
        imprenditoria =findViewById(R.id.button_imprenditoria);
        elettricista = findViewById(R.id.button_elettricista);
        serieTV = findViewById(R.id.button_serieTV);
        libri = findViewById(R.id.button_libri);
        tecnologia = findViewById(R.id.button_tecnologia);
        destEsotiche = findViewById(R.id.button_destinazioni_esotiche);
        rock = findViewById(R.id.button_rock);
        classica = findViewById(R.id.button_classica);
        pop = findViewById(R.id.button_pop);
        ricette = findViewById(R.id.button_ricette);
        dolce = findViewById(R.id.button_dolce);
        calcio = findViewById(R.id.button_calcio);
        tennis =  findViewById(R.id.button_tennis);
        yoga =  findViewById(R.id.button_yoga);
        palestra =  findViewById(R.id.button_palestra);

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

        ImageButton closeSettingsButton = findViewById(R.id.back_btn);
        closeSettingsButton.setOnClickListener(view -> {
            setResult(Activity.RESULT_OK);
            finish();
        });

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userPreferencesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("preferences");

        //change schermata
        navigate.setOnClickListener(v ->{
            setResult(Activity.RESULT_OK);
            finish();
        });


    }
    @Override
    public void onClick(View v) {
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(this.getApplication());
        userViewModel = new ViewModelProvider(
                this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);
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





