package it.unimib.socialmesh.util;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.EventApiResponse;

public class JSONParserUtil {

    private final Context context;
    private final String embeddedParameter = "_embedded";
    private final String nameParameter = "name";
    private final String idParameter = "id";
    private final String urlParameter = "url";
    private final String infoParameter = "pleaseNote";
    private final String imagesParameter = "images";
    private final String ageRestrictionsParameter = "ageRestrictions";
    private final String venuesParameter = "venues";
    private final String eventsParameter = "events";
    private final String dateParameter = "dates";
    private final String TAG = "PARSER JSON";

    public JSONParserUtil(Application application) {
        this.context = application.getApplicationContext();
    }

    public EventApiResponse getTicketmasterEvents(String apiKey, String country, String startDateParameter, String localDateParameter, String localTimeParameter) {

        try {
            // Costruisci l'URL per la richiesta degli eventi da Ticketmaster
            String urlString = "https://app.ticketmaster.com/discovery/v2/events?"
                    + "apikey=" + apiKey
                    //+ "&city=" + city
                    + "&country=" + country;
            //+ "&startDateTime=" + startDateTime
            //+ "&endDateTime=" + endDateTime;
            //System.out.println(urlString);

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Verifica lo status code della risposta
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Leggi la risposta
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parsa il JSON restituito
                JSONObject json = new JSONObject(response.toString());
                JSONObject embedded = json.getJSONObject(embeddedParameter);
                JSONArray events = embedded.getJSONArray(eventsParameter);

                // Estrai informazioni rilevanti
                for (int i = 0; i < events.length(); i++) {

                    JSONObject event = events.getJSONObject(i);

                    String eventName = event.getString(nameParameter);

                    String eventId = event.getString(idParameter);

                    String eventUrl = event.getString(urlParameter);

                    String eventImageUrl = event.getJSONArray(imagesParameter).getString(0);

                    String eventDate = event.getJSONObject(dateParameter).getJSONObject(startDateParameter).getString(localDateParameter);

                    String eventTime = event.getJSONObject(dateParameter).getJSONObject(startDateParameter).getString(localTimeParameter);

                    String eventNotes = event.getString(infoParameter);

                    Boolean eventAgeRestrictions = event.getJSONObject(ageRestrictionsParameter).getBoolean("legalAgeEnforced");

                    //TODO values._embedded.venues.addres.line1

                    Event e = new Event(eventName, eventId,
                                        eventUrl, eventImageUrl,
                                        eventDate, eventTime,
                                        eventNotes, eventAgeRestrictions);

                }
            } else {
                // Stampare un messaggio di errore in caso di risposta non 200 OK
                Log.d(TAG,"Errore nella chiamata alle API di Ticketmaster. Codice errore: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
