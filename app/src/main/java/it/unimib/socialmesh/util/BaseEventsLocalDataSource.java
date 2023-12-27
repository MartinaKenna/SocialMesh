package it.unimib.socialmesh.util;

import java.util.List;

import it.unimib.socialmesh.model.Event;

public abstract class BaseEventsLocalDataSource {

    protected EventCallback eventCallback;

    public void setEventCallback(EventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }

    public abstract void getEvents();
    public abstract void insertEvents(List<Event> eventsList);
}