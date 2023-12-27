package it.unimib.socialmesh.model;
import it.unimib.socialmesh.model.EventApiResponse;
public abstract class Result {
    private Result() {}

    public boolean isSuccess() {
        return this instanceof Success;
    }

    /**
     * Class that represents a successful action during the interaction
     * with a Web Service or a local database.
     */
    public static final class Success extends Result {
        private final EventApiResponse eventApiResponse;
        public Success(EventApiResponse eventApiResponseResponse) {
            this.eventApiResponse = eventApiResponseResponse;
        }
        public EventApiResponse getData() {
            return eventApiResponse;
        }
    }

    /**
     * Class that represents an error occurred during the interaction
     * with a Web Service or a local database.
     */
    public static final class Error extends Result {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}


