package it.unimib.socialmesh.model;

public abstract class Result {
    private Result() {}

    public boolean isSuccess() {
        if (this instanceof EventResponseSuccess || this instanceof UserResponseSuccess) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Class that represents a successful action during the interaction
     * with a Web Service or a local database.
     */
    public static final class EventResponseSuccess extends Result {
        private final EventApiResponse eventApiResponse;
        public EventResponseSuccess(EventApiResponse eventApiResponseResponse) { this.eventApiResponse = eventApiResponseResponse; }
        public EventApiResponse getData() {
            return eventApiResponse;
        }
    }

    public static final class UserResponseSuccess extends Result {
        private final User user;
        public UserResponseSuccess(User user) {
            this.user = user;
        }
        public User getData() {
            return user;
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


