package it.unimib.socialmesh.repository;

import android.app.Application;
import android.util.Log;

import it.unimib.socialmesh.database.EventDao;
import it.unimib.socialmesh.database.EventRoomDatabase;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.service.EventApiService;

import androidx.annotation.NonNull;

import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.EventApiResponse;
import it.unimib.socialmesh.util.ResponseCallback;
import it.unimib.socialmesh.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsRepository {
    private static final String TAG = EventsRepository.class.getSimpleName();
    private final Application application;
    private final EventDao eventDao;

    private final EventApiService eventsApiService;
    private final ResponseCallback responseCallback;

    //TODO aggiungere database

    public EventsRepository(Application application, ResponseCallback responseCallback) {
        this.application = application;
        this.eventsApiService = ServiceLocator.getInstance().getEventsApiService();
        this.responseCallback = responseCallback;

        //TODO da sistemare sta roba
        EventRoomDatabase eventRoomDatabase = ServiceLocator.getInstance().getEventDao(application);
        this.eventDao = eventRoomDatabase.eventDao();
    }


    /*
     * Metodo scritto secondo le indicazioni del prof, al momento non funziona
     *
     * Dovrebbe recuperare i dati da TicketMaster e parsarli con una classe
     * GSON gestita da Retrofit
     * */
    public void fetchEvents(String type, String city, int size, String startDateTime, String time, long lastUpdate) {

        long currentTime = System.currentTimeMillis();

        // It gets the news from the Web Service if the last download
        // of the news has been performed more than FRESH_TIMEOUT value ago
        //TODO sistemare la condizione per l'aggiornamento
        //if (currentTime - lastUpdate > FRESH_TIMEOUT) {
        if (true) {
            Call<EventApiResponse> eventsResponseCall = eventsApiService.getEvents(type, city,size, startDateTime, time,
                    "ymPPalpoNoG8lG5xyca0AQ6uhACG4y3j");

            eventsResponseCall.enqueue(new Callback<EventApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<EventApiResponse> call,
                                       @NonNull Response<EventApiResponse> response) {

                    if(response.body() != null && response.isSuccessful()) {
                        List<Event> eventsList = response.body().getEvents();
                        responseCallback.onSuccess(eventsList, System.currentTimeMillis());
                        saveDataInDatabase(eventsList);
                    } else {
                        responseCallback.onFailure(application.getString(R.string.error_retrieving_events));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<EventApiResponse> call, @NonNull Throwable t) {

                    if(t.getMessage() != null)
                        Log.d(TAG, t.getMessage());
                    else
                        Log.d(TAG, "messaggio di errore nullo");

                    responseCallback.onFailure(t.getMessage());
                }
            });
        } else {
            //TODO
            //  Log.d(TAG, application.getString(R.string.data_read_from_local_database));
            readDataFromDatabase(lastUpdate);
        }
    }


    /*
     * Metodo che recupera i dati dal file JSON nella cartella "assets", questo funziona
     * ma ovviamente non si aggiorna con i dati di Ticketmaster in automatico
     */

/*
    public void fetchEventsFromJsonFile(String type, String city, String startDateTime, String time, long lastUpdate) {

        JSONParserUtil jsonParserUtil = new JSONParserUtil(application);
        EventApiResponse eventsApiResponse = null;

        try {
            eventsApiResponse = jsonParserUtil.getTicketmasterEventsFromFile(FILE_JSON_TEST_API);
        } catch (JSONException e) {
            Log.d(TAG, "ERRORE PARSING" + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(eventsApiResponse != null) {
            List<Event> eventsList = eventsApiResponse.getEvents();
        }
    }

*/


    /**
     * Saves the news in the local database.
     * The method is executed in a Runnable because the database access
     * cannot been executed in the main thread.
     * @param eventsList the list of news to be written in the local database.
     */

    private void saveDataInDatabase(List<Event> eventsList) {
        EventRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<Event> allEvents = eventDao.getAll();

            // Checks if the news just downloaded has already been downloaded earlier
            // in order to preserve the news status (marked as favorite or not)
            for (Event event : allEvents) {
                // This check works because News and NewsSource classes have their own
                // implementation of equals(Object) and hashCode() methods
                if (eventsList.contains(event)) {
                    // The primary key and the favorite status is contained only in the News objects
                    // retrieved from the database, and not in the News objects downloaded from the
                    // Web Service. If the same news was already downloaded earlier, the following
                    // line of code replaces the the News object in newsList with the corresponding
                    // News object saved in the database, so that it has the primary key and the
                    // favorite status.
                    eventsList.set(eventsList.indexOf(event), event);
                }
            }

            // Writes the news in the database and gets the associated primary keys
            List<Long> insertedEventsIds = eventDao.insertEventsList(eventsList);
            for (int i = 0; i < eventsList.size(); i++) {
                // Adds the primary key to the corresponding object News just downloaded so that
                // if the user marks the news as favorite (and vice-versa), we can use its id
                // to know which news in the database must be marked as favorite/not favorite
                eventsList.get(i).setLocalId(insertedEventsIds.get(i));
            }

            responseCallback.onSuccess(eventsList, System.currentTimeMillis());
        });
    }



    /**
     * Gets the news from the local database.
     * The method is executed in a Runnable because the database access
     * cannot been executed in the main thread.
     */

    private void readDataFromDatabase(long lastUpdate) {
        EventRoomDatabase.databaseWriteExecutor.execute(() -> {
            responseCallback.onSuccess(eventDao.getAll(), lastUpdate);
        });
    }

}
