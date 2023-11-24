package it.unimib.socialmesh.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TicketMasterJson {

    public String getTicketmasterEvents(String apiKey, String country, String startDateParameter, String localDateParameter, String localTimeParameter) {
        try {
            // Costruisci l'URL per la richiesta degli eventi da Ticketmaster
            String urlString = "https://app.ticketmaster.com/discovery/v2/events?"
                    + "apikey=" + apiKey
                    //+ "&city=" + city
                    + "&country=" + country;
            //+ "&startDateTime=" + startDateTime
            //+ "&endDateTime=" + endDateTime;
            System.out.println(urlString);

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
            } else {
                // Stampare un messaggio di errore in caso di risposta non 200 OK
                Log.d(TAG, "Errore nella chiamata alle API di Ticketmaster. Codice errore: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
