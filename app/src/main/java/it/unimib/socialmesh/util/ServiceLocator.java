package it.unimib.socialmesh.util;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import it.unimib.socialmesh.repository.EventsRepositoryWithLiveData;
import it.unimib.socialmesh.R;
import it.unimib.socialmesh.database.EventRoomDatabase;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.Result;
import it.unimib.socialmesh.repository.IEventsRepositoryWithLiveData;
import it.unimib.socialmesh.service.EventApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *  Registry to provide the dependencies for the classes
 *  used in the application.
 */
public class ServiceLocator {

    private static volatile ServiceLocator INSTANCE = null;
    private static final String TAG = ServiceLocator.class.getSimpleName();

    private ServiceLocator() {
    }

    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized (ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * It creates an instance of EventApiService using Retrofit.
     *
     * @return an instance of EventApiService.
     */
    public EventApiService getEventsApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.EVENTS_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(EventApiService.class);
    }


    public EventRoomDatabase getEventDao(Application application) {
        return EventRoomDatabase.getDatabase(application);
    }

    public IEventsRepositoryWithLiveData getEventRepository(Application application) {
        BaseEventsLocalDataSource eventsLocalDataSource;
        BaseEventsRemoteDataSource eventsRemoteDataSource;

        eventsRemoteDataSource = new EventsRemoteDataSource(application.getString(R.string.events_api_key));
        eventsLocalDataSource = new EventsLocalDataSource(getEventDao(application));

        return new EventsRepositoryWithLiveData(eventsRemoteDataSource,eventsLocalDataSource);

    }
}
