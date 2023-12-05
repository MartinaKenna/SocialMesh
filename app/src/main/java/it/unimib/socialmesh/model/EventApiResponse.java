package it.unimib.socialmesh.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.navigation.NavType;

import java.util.List;

public class EventApiResponse implements Parcelable {
    private List<Event> eventList;

    public EventApiResponse() {}

    public EventApiResponse(List<Event> eventsList) {
        this.eventList = eventsList;
    }

    public List<Event> getEvents() {
        return eventList;
    }

    protected EventApiResponse(Parcel in) {
        this.eventList = in.createTypedArrayList(Event.CREATOR);
    }

    public static final Parcelable.Creator<EventApiResponse> CREATOR = new Parcelable.Creator<EventApiResponse>() {
        @Override
        public EventApiResponse createFromParcel(Parcel in) {
            return new EventApiResponse(in);
        }

        @Override
        public EventApiResponse[] newArray(int size) {
            return new EventApiResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(this.eventList);
    }
}
