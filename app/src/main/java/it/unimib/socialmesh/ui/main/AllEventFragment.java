package it.unimib.socialmesh.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.adapter.RecyclerViewAllEvents;
import it.unimib.socialmesh.adapter.RecyclerViewEventsAdapter;
import it.unimib.socialmesh.model.Event;

public class AllEventFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAllEvents adapter;
    private EventFragment eventFragment;
    private List<Event> eventsList;
    private Integer viewType;
    private Button buttonKM, buttonFilter, button1, button2, button3, button4, button5, button6;
    private PopupWindow popupWindow, popupWindow2;

    public AllEventFragment(){
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_all_event, container, false);
        recyclerView = view.findViewById(R.id.recyclerviewAll);
        buttonFilter = view.findViewById(R.id.button);
        buttonKM = view.findViewById(R.id.buttonKM);

        if (getArguments() != null) {
            viewType = AllEventFragmentArgs.fromBundle(getArguments()).getViewType();
            eventsList = AllEventFragmentArgs.fromBundle(getArguments()).getEventList();
        }

        if(viewType==0){
            buttonKM.setVisibility(View.VISIBLE);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            adapter = new RecyclerViewAllEvents(requireContext(), eventFragment.getLatitude(), eventFragment.getLongitude(), eventsList, viewType,
                    new RecyclerViewAllEvents.OnItemClickListener() {
                        @Override
                        public void onEventItemClick(Event event) {
                            EventFragmentDirections.ActionEventFragmentToEventDetailsFragment action =
                                    EventFragmentDirections.actionEventFragmentToEventDetailsFragment(event);
                            Navigation.findNavController(requireView()).navigate(action);
                        }
                    });

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
        }
        else{
            buttonKM.setVisibility(View.GONE);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            adapter = new RecyclerViewAllEvents(requireContext(), eventFragment.getLatitude(), eventFragment.getLongitude(), eventsList, viewType,
                    new RecyclerViewAllEvents.OnItemClickListener() {
                        @Override
                        public void onEventItemClick(Event event) {
                            EventFragmentDirections.ActionEventFragmentToEventDetailsFragment action =
                                    EventFragmentDirections.actionEventFragmentToEventDetailsFragment(event);
                            Navigation.findNavController(requireView()).navigate(action);
                        }
                    });

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
        }

        buttonFilter.setOnClickListener(v -> {
            View popupView = inflater.inflate(R.layout.popupview, container, false);

            button1 = popupView.findViewById(R.id.button1);
            button2 = popupView.findViewById(R.id.button2);
            button3 = popupView.findViewById(R.id.button3);
            //dichiaro la tendina
            popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

            // Imposto il testo per i bottoni
            switch(getMostFrequentGenres().size()){
                case 1: button1.setText(getMostFrequentGenres().get(0));
                    button2.setVisibility(View.GONE);
                    button3.setVisibility(View.GONE);
                    break;
                case 2: button1.setText(getMostFrequentGenres().get(0));
                    button2.setText(getMostFrequentGenres().get(1));
                    button3.setVisibility(View.GONE);
                    break;
                case 3: button1.setText(getMostFrequentGenres().get(0));
                    button2.setText(getMostFrequentGenres().get(1));
                    button3.setText(getMostFrequentGenres().get(2));
                    break;
                default:button1.setVisibility(View.GONE);
                    button2.setVisibility(View.GONE);
                    button3.setVisibility(View.GONE);
                    break;
            }

            button1.setOnClickListener(t -> {
                String buttonText = ((Button) t).getText().toString();
                adapter.filterByGenre(buttonText);
                popupWindow.dismiss();
            });
            button2.setOnClickListener(t -> {
                String buttonText = ((Button) t).getText().toString();
                adapter.filterByGenre(buttonText);
                popupWindow.dismiss();
            });
            button3.setOnClickListener(t -> {
                String buttonText = ((Button) t).getText().toString();
                adapter.filterByGenre(buttonText);
                popupWindow.dismiss();
            });

            popupWindow.showAsDropDown(buttonFilter, 0, -popupWindow.getHeight());

        });


        //tendina per i km
        buttonKM.setOnClickListener(v ->{
            View popupView2 = inflater.inflate(R.layout.popupview2, container, false);

            popupWindow2 = new PopupWindow(popupView2, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

            button4 = popupView2.findViewById(R.id.button4);
            button5 = popupView2.findViewById(R.id.button5);
            button6 = popupView2.findViewById(R.id.button6);

            button4.setText("10km");
            button5.setText("50km");
            button6.setText("100km");

            button4.setOnClickListener(t ->{
                adapter.setKM(10);
                popupWindow2.dismiss();
            });
            button5.setOnClickListener(t ->{
                adapter.setKM(50);
                popupWindow2.dismiss();
            });
            button6.setOnClickListener(t ->{
                adapter.setKM(100);
                popupWindow2.dismiss();
            });
            popupWindow2.showAsDropDown(buttonKM, 0, -popupWindow2.getHeight());
        });
        return super.onCreateView(inflater, container, savedInstanceState);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    public List<String> getMostFrequentGenres() {
        Map<String, Integer> genreOccurrences = new HashMap<>();

        // Conto le occorrenze di ciascun genere
        for (Event event : eventsList) {
            String genreName = event.getGenreName();
            genreOccurrences.put(genreName, genreOccurrences.getOrDefault(genreName, 0) + 1);
        }

        // Trovo i tre generi piu frequenti diversi tra loro
        List<String> mostFrequentGenres = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int maxOccurrences = 0;
            String mostFrequentGenre = "";

            for (Map.Entry<String, Integer> entry : genreOccurrences.entrySet()) {
                if (!mostFrequentGenres.contains(entry.getKey()) && entry.getValue() > maxOccurrences) {
                    maxOccurrences = entry.getValue();
                    mostFrequentGenre = entry.getKey();
                }
            }

            if (!mostFrequentGenre.isEmpty()) {
                mostFrequentGenres.add(mostFrequentGenre);
            }
        }

        return mostFrequentGenres;
    }
}
