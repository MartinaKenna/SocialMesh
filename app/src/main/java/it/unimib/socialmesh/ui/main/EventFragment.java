package it.unimib.socialmesh.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.EventApiResponse;
import it.unimib.socialmesh.repository.EventsRepository;
import it.unimib.socialmesh.util.ResponseCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment implements ResponseCallback{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private List<Event> eventsList;
    private EventsRepository eventsRepository;

    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance() {return new EventFragment();}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventsRepository = new EventsRepository(requireActivity().getApplication(), this);
        eventsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventsRepository.fetchEvents("music", "324", "2024-06-01T08:00:00Z", "2024-06-30T08:00:00Z", 10);
    }



    @Override
    public void onSuccess(List<Event> eventsList, long lastUpdate) {
        Log.d("ON SUCCESS CHIAMATO", eventsList.toString());
    }

    @Override
    public void onFailure(String errorMessage) {

    }
}