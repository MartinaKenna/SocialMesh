package it.unimib.socialmesh.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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

public class EditPreferencesActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference userPreferencesRef;
    private Button cinema, party, fumatore, viaggiLowCost, lgbt,
            karaoke, nft, boxe, festival, crossfit, nature, beach, motorsport, instagram,
            twitter, photography, painting, escursioni, gardening, writing, moda, programming,
            vegetarian, vegan, carnivore, single, engaged, married, blogger, cucinaItaliana,
            basket, facebook, navigate, freelance, imprenditoria, tecnologia, tennis, calcio,
            destEsotiche, serieTV, libri, rock, classica, pop, ricette, dolce, yoga, palestra,
            elettricista;
    List<String> preferences = new ArrayList<>();
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

        //prendo l'id dell'utente
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //definisco il campo "preferences" in firebase
        userPreferencesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("preferences");

        //change schermata
        navigate.setOnClickListener(v ->{
            setResult(Activity.RESULT_OK);
            finish();
        });


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
                        Toast.makeText(this, "Preferenza salvata con successo!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Errore nel salvataggio della preferenza", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}





