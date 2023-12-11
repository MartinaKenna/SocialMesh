package it.unimib.socialmesh.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Embedded__1 {

    @SerializedName("venues")
    @Expose
    private List<Venue__1> venues;
    @SerializedName("attractions")
    @Expose
    private List<Attraction__1> attractions;

    public List<Venue__1> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue__1> venues) {
        this.venues = venues;
    }

    public List<Attraction__1> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<Attraction__1> attractions) {
        this.attractions = attractions;
    }
}
