package it.unimib.socialmesh.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unimib.socialmesh.R;

import it.unimib.socialmesh.model.Event;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventPartecipantsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventPartecipantsFragment extends Fragment {

    private static final String TAG = EventPartecipantsFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public EventPartecipantsFragment() {
        // Required empty public constructor
    }

    public static EventPartecipantsFragment newInstance() {
        return new EventPartecipantsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_partecipants, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null) {

            Event commonEvent = EventPartecipantsFragmentArgs.fromBundle(getArguments()).getCommonEvent();
            Log.d(TAG, "Evento ricevuto con successo");
            //TODO aggiungere cosa fare con l'eveto ricevuto

        } else {
            Log.d(TAG, "Errore, evento passato da Match a Partecipants vuoto");
        }


    }
}