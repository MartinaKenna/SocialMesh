package it.unimib.socialmesh.model.jsonFields;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Embedded_1 {

    @SerializedName("venues")
    @Expose
    private List<Venue> venues;
    @SerializedName("attractions")
    @Expose
    private List<Attraction> attractions;

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

    public List<Attraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<Attraction> attractions) {
        this.attractions = attractions;
    }
}
