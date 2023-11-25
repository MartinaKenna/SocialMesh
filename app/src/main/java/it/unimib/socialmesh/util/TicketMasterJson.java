package it.unimib.socialmesh.util;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TicketMasterJson {
    private final String API_KEY = "ymPPalpoNoG8lG5xyca0AQ6uhACG4y3j";
    private final String BASE_URL = "https://app.ticketmaster.com/discovery/v2/events?";

    public TicketMasterJson() {}

    public String getTicketmasterEvents(String country, String startDateTime, String endDateTime) {
        try {
            //Crea l'url con i parametri richiesti
            String api_call_url = BASE_URL
                                + "apikey=" + API_KEY
                                + "&country=" + country
                                + "&startDateTime=" + startDateTime
                                + "&endDateTime=" + endDateTime;

            URL url = new URL(api_call_url);

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
                connection.disconnect();

                return response.toString();
            } else {
                // Stampare un messaggio di errore in caso di risposta non 200 OK
                Log.d("TEST JSON PARSER", "Errore nella chiamata alle API di Ticketmaster. Codice errore: " + responseCode);
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
