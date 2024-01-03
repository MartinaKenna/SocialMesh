package it.unimib.socialmesh.util;



public abstract class BaseEventsRemoteDataSource{
    protected EventCallback eventCallback;
    public void setEventCallback(EventCallback eventCallback){
        this.eventCallback = eventCallback;
    }
    public abstract void getEvents(String type, String city, int size,String startDateTime, String time);
}