package it.unimib.socialmesh.util;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.EventApiResponse;
import it.unimib.socialmesh.model.EventPlace;

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
    private final String startDateParameter = "start";
    private final String localDateParameter = "localDate";
    private final String localTimeParameter = "localTime";
    private final String legalAgeEnforcedParameter = "legalAgeEnforced";

    public JSONParserUtil(Application application) {
        this.context = application.getApplicationContext();
    }

    /*
     * TODO da sistemare per quando sistemiamo le api
     */
    /*
    public EventApiResponse getTicketmasterEvents(String to_parse) throws JSONException {

        JSONObject rootJson;

        if(to_parse != null) {

            // Parsa l'oggetto principale
            rootJson = new JSONObject(to_parse);

            // Parsa _embedded
            JSONObject embedded = rootJson.getJSONObject(embeddedParameter);

            // Parsa l'array di events
            JSONArray events = embedded.getJSONArray(eventsParameter);

            List<Event> eventList = new ArrayList<>();

            for (int i = 0; i < events.length(); i++) {

                JSONObject event = events.getJSONObject(i);

                String eventName = event.getString(nameParameter);

                String eventId = event.getString(idParameter);

                String eventUrl = event.getString(urlParameter);

                String eventImageUrl = event.getJSONArray(imagesParameter).getString(0);

                String eventDate = event.getJSONObject(dateParameter).getJSONObject(startDateParameter).getString(localDateParameter);

                String eventTime = event.getJSONObject(dateParameter).getJSONObject(startDateParameter).getString(localTimeParameter);

                String eventNotes = event.getString(infoParameter);

                boolean eventAgeRestrictions = event.getJSONObject(ageRestrictionsParameter).getBoolean(legalAgeEnforcedParameter);

                String place = event.getJSONObject(embeddedParameter).getJSONArray(venuesParameter).getString(0);


                eventList.add(new Event(eventName, eventId, eventUrl,
                        eventImageUrl, eventDate, eventTime,
                        eventNotes, eventAgeRestrictions, place));

            }
            return new EventApiResponse(eventList);
        }
        else {
            Log.d(TAG, "LA STRINGA JSON PASSATA E' NULLA");
            return null;
        }
    }
    */

    public EventApiResponse getTicketmasterEventsFromFile(String fileName) throws IOException, JSONException {

        InputStream inputStream = context.getAssets().open(fileName);
        String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        JSONObject rootJson = new JSONObject(content);

        // Parsa _embedded
        JSONObject embedded = rootJson.getJSONObject(embeddedParameter);

        // Parsa l'array di events
        JSONArray events = embedded.getJSONArray(eventsParameter);

        List<Event> eventList = new ArrayList<>();

        for (int i = 0; i < events.length(); i++) {

            JSONObject event = events.getJSONObject(i);

            String eventName = event.getString(nameParameter);

            String eventId = event.getString(idParameter);

            String eventUrl = event.getString(urlParameter);

            //TODO scegliere bene quale immagine prendere
            String eventImageUrl = event.getJSONArray(imagesParameter).getString(0);

            String eventDate = null;
            try {
                eventDate = event.getJSONObject(dateParameter).getJSONObject(startDateParameter).getString(localDateParameter);
            } catch (JSONException je) {
                Log.d(TAG, "valore JSON data evento non trovato");
            }

            String eventTime = null;
            try {
                eventTime = event.getJSONObject(dateParameter).getJSONObject(startDateParameter).getString(localTimeParameter);
            } catch (JSONException je) {
                Log.d(TAG, "valore JSON ora evento non trovato");
            }

            String eventNotes = null;
            try {
                eventNotes = event.getString(infoParameter);
            } catch (JSONException je) {
                Log.d(TAG, "valore JSON note evento non trovato");
            }

            boolean eventAgeRestrictions = false;
            try {
                eventAgeRestrictions = event.getJSONObject(ageRestrictionsParameter).getBoolean(legalAgeEnforcedParameter);
            } catch (JSONException je) {
                Log.d(TAG, "valore JSON restrizione eta' non trovato");
            }

            EventPlace place = null;
            try {
                String place_tmp = event.getJSONObject(embeddedParameter).getJSONArray(venuesParameter).getString(0);
                place = parseEventPlace(place_tmp);

            } catch (JSONException je) {
                Log.d(TAG, "valore JSON posto non trovato");
            }

            //Assemblo l'oggetto evento risultante
            eventList.add(new Event(eventName, eventId, eventUrl,
                    eventImageUrl, eventDate, eventTime,
                    eventNotes, eventAgeRestrictions, place));
        }

        return new EventApiResponse(eventList);
    }

    private EventPlace parseEventPlace(String to_parse) {
        try {
            JSONObject rootObj = new JSONObject(to_parse);
            String name = rootObj.getString("name");
            String id = rootObj.getString("id");
            String city = rootObj.getJSONObject("city").getString("name");
            String state = rootObj.getJSONObject("state").getString("name");
            String country = rootObj.getJSONObject("country").getString("name");
            String address = rootObj.getJSONObject("address").getString("line1");
            double longitude = rootObj.getJSONObject("location").getDouble("longitude");
            double latitude = rootObj.getJSONObject("location").getDouble("latitude");

            return new EventPlace(name, id, city, state, country, address, longitude, latitude);

        } catch (JSONException e) {
            Log.d(TAG, "ERRORE NEL PARSING DI UN PLACE");
            return null;
        }
    }
}
