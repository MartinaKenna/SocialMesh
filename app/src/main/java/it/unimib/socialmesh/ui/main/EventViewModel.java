package it.unimib.socialmesh.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.socialmesh.model.Result;
import it.unimib.socialmesh.repository.IEventsRepositoryWithLiveData;

public class EventViewModel extends ViewModel {
    private static final String TAG = EventViewModel.class.getSimpleName();
    private final IEventsRepositoryWithLiveData eventsRepositoryWithLiveData;
    private MutableLiveData<Result> eventsListLiveData;

    public EventViewModel(IEventsRepositoryWithLiveData iEventsRepositoryWithLiveData) {
        this.eventsRepositoryWithLiveData = iEventsRepositoryWithLiveData;
    }

    public MutableLiveData<Result> getEvents(String type, String city, String startDateTime, String time, long lastUpdate){
        if( eventsListLiveData == null){
            fetchEvents(type,city,startDateTime,time, lastUpdate);
        }
        return eventsListLiveData;
    }

    private void fetchEvents(String type, String city, String startDateTime, String time, long lastUpdate){
        eventsListLiveData = eventsRepositoryWithLiveData.fetchEvents(type,city,startDateTime,time,lastUpdate);
    }
}
