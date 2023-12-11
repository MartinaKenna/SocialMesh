package it.unimib.socialmesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventApiResponse {
    @SerializedName("_embedded")

    @Expose
    private Embedded embedded;


    public List<Event> getEvents() {
        return embedded.getEvents();
    }

    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }


    public EventApiResponse() {
    }



   /* protected EventApiResponse(Parcel in) {
        this.eventList = in.createTypedArrayList(Event.CREATOR);
    }*/

  /*  public static final Parcelable.Creator<EventApiResponse> CREATOR = new Parcelable.Creator<EventApiResponse>() {
        @Override
        public EventApiResponse createFromParcel(Parcel in) {
            return new EventApiResponse(in);
        }

        @Override
        public EventApiResponse[] newArray(int size) {
            return new EventApiResponse[size];
        }
    };*/


    public int describeContents() {
        return 0;
    }
/*
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(this.eventList);
    }
}*/
}