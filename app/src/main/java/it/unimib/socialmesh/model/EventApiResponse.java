package it.unimib.socialmesh.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class EventApiResponse implements Parcelable {
    private List<Event> embedded;

    public EventApiResponse() {}

    public EventApiResponse(List<Event> embedded) {
        this.embedded = embedded;
    }

    protected EventApiResponse(Parcel in) {
    }

    public static final Creator<EventApiResponse> CREATOR = new Creator<EventApiResponse>() {
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
    }
}
