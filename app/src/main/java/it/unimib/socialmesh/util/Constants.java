package it.unimib.socialmesh.util;

public class Constants {
    public static final String USA = "US";

    public static final String FILE_JSON_TEST_API = "EventsApiTest.json";

    public static final String EVENTS_API_BASE_URL = "https://app.ticketmaster.com/discovery/v2/";

    public static final String EVENTS_ENDPOINT = "events.json";

    public static final String EVENTS_TYPE_PARAMETER = "classificationName";

    public static final String EVENTS_CITY_PARAMETER = "dmaId";
    public static final String EVENTS_START_DATE_PARAMETER = "startDateTime";
    public static final String EVENTS_END_DATE_PARAMETER = "endDateTime";
    public static final String API_KEY_PARAMETER = "apikey";
    public static final int FRESH_TIMEOUT = 1000 * 60 * 60; // 1 hour in milliseconds
    // Constants for Room database
    public static final String EVENTS_DATABASE_NAME = "events_db";
    public static final int DATABASE_VERSION = 3;

}