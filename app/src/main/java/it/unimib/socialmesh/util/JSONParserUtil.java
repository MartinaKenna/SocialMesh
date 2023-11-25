package it.unimib.socialmesh.util;

import android.app.Application;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private final String startDateParameter = "start";
    private final String localDateParameter = "localDate";
    private final String localTimeParameter = "localTime";
    private final String legalAgeEnforcedParameter = "legalAgeEnforced";

    public JSONParserUtil(Application application) {
        this.context = application.getApplicationContext();
    }

    public EventApiResponse getTicketmasterEvents(String country, String startDateTime, String endDateTime) throws JSONException {

        TicketMasterJson apiCall = new TicketMasterJson();
        String to_parse = apiCall.getTicketmasterEvents(country, startDateTime, endDateTime);

        // Parsa il JSON restituito
        JSONObject json = new JSONObject(to_parse.toString());
        JSONObject embedded = json.getJSONObject(embeddedParameter);
        JSONArray events = embedded.getJSONArray(eventsParameter);

        //Variabili e classi di ritorno
        EventApiResponse ear = new EventApiResponse();
        List<Event> event_list = new ArrayList<>();

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

            Boolean eventAgeRestrictions = event.getJSONObject(ageRestrictionsParameter).getBoolean(legalAgeEnforcedParameter);

            String place = event.getJSONObject(embeddedParameter).getJSONArray(venuesParameter).getString(0);

            Event e = new Event(eventName, eventId, eventUrl,
                                eventImageUrl, eventDate, eventTime,
                                eventNotes, eventAgeRestrictions, place);
            event_list.add(e);
        }
        return new EventApiResponse(event_list);
    }
}
