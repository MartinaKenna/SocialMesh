package it.unimib.socialmesh.repository;

import static it.unimib.socialmesh.util.Constants.FRESH_TIMEOUT;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.EventApiResponse;
import it.unimib.socialmesh.service.EventApiService;
import it.unimib.socialmesh.util.ResponseCallback;
import it.unimib.socialmesh.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsRepository {
    private static final String TAG = EventsRepository.class.getSimpleName();
    private final Application application;
    private final EventApiService eventsApiService;

    //TODO aggiungere database
    private final ResponseCallback responseCallback;

    public EventsRepository(Application application, ResponseCallback responseCallback) {
        this.application = application;
        //TODO da sistemare sta roba
        this.eventsApiService = ServiceLocator.getInstance().getEventsApiService();
        //NewsRoomDatabase newsRoomDatabase = ServiceLocator.getInstance().getNewsDao(application);
        //this.newsDao = newsRoomDatabase.newsDao();
        this.responseCallback = responseCallback;
    }

    public void fetchEvents(String country, String startDateTime, String endDateTime, long lastUpdate) {

        long currentTime = System.currentTimeMillis();

        // It gets the news from the Web Service if the last download
        // of the news has been performed more than FRESH_TIMEOUT value ago
        if (currentTime - lastUpdate > FRESH_TIMEOUT) {
            Call<EventApiResponse> eventsResponseCall = eventsApiService.getEvents(country,
                    startDateTime, endDateTime, application.getString(R.string.events_api_key));

            eventsResponseCall.enqueue(new Callback<EventApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<EventApiResponse> call,
                                       @NonNull Response<EventApiResponse> response) {

                    if (response.body() != null && response.isSuccessful()) {
                        List<Event> eventsList = response.body().getEvents();
                        //TODO saveDataInDatabase(eventsList);
                    } else {
                        responseCallback.onFailure(application.getString(R.string.error_retrieving_events));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<EventApiResponse> call, @NonNull Throwable t) {
                    responseCallback.onFailure(t.getMessage());
                }
            });
        } else {
            //TODO
            //Log.d(TAG, application.getString(R.string.data_read_from_local_database));
            //readDataFromDatabase(lastUpdate);
        }
    }




    /**
     * Saves the news in the local database.
     * The method is executed in a Runnable because the database access
     * cannot been executed in the main thread.
     * @param newsList the list of news to be written in the local database.
     */
    /*
    private void saveDataInDatabase(List<News> newsList) {
        NewsRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<News> allNews = newsDao.getAll();

            // Checks if the news just downloaded has already been downloaded earlier
            // in order to preserve the news status (marked as favorite or not)
            for (News news : allNews) {
                // This check works because News and NewsSource classes have their own
                // implementation of equals(Object) and hashCode() methods
                if (newsList.contains(news)) {
                    // The primary key and the favorite status is contained only in the News objects
                    // retrieved from the database, and not in the News objects downloaded from the
                    // Web Service. If the same news was already downloaded earlier, the following
                    // line of code replaces the the News object in newsList with the corresponding
                    // News object saved in the database, so that it has the primary key and the
                    // favorite status.
                    newsList.set(newsList.indexOf(news), news);
                }
            }

            // Writes the news in the database and gets the associated primary keys
            List<Long> insertedNewsIds = newsDao.insertNewsList(newsList);
            for (int i = 0; i < newsList.size(); i++) {
                // Adds the primary key to the corresponding object News just downloaded so that
                // if the user marks the news as favorite (and vice-versa), we can use its id
                // to know which news in the database must be marked as favorite/not favorite
                newsList.get(i).setId(insertedNewsIds.get(i));
            }

            responseCallback.onSuccess(newsList, System.currentTimeMillis());
        });
    }
    */


    /**
     * Gets the news from the local database.
     * The method is executed in a Runnable because the database access
     * cannot been executed in the main thread.
     */
    /*
    private void readDataFromDatabase(long lastUpdate) {
        NewsRoomDatabase.databaseWriteExecutor.execute(() -> {
            responseCallback.onSuccess(newsDao.getAll(), lastUpdate);
        });
    }
    */
}
