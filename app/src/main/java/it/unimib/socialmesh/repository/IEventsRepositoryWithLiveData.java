package it.unimib.socialmesh.repository;
import androidx.lifecycle.MutableLiveData;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.Result;
import java.util.List;

public interface IEventsRepositoryWithLiveData {
    MutableLiveData<Result>  fetchEvents(String type, String city, String startDateTime, String time, long lastUpdate);

}
