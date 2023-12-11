package it.unimib.socialmesh.model;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Embedded {
    @SerializedName("events")
    @Expose
    private List<Event__1> events;

    public List<Event__1> getEvents() {
        return events;
    }

    public void setEvents(List<Event__1> events) {
        this.events = events;
    }
}
