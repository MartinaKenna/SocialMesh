package it.unimib.socialmesh.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class EventPlace implements Parcelable {
    private String name;
    private String id;
    private String city;
    private String state;
    private String country;
    private String address;
    private double longitude;
    private double latitude;

    public EventPlace(String name, String id, String city, String state, String country,
                      String address, double longitude, double latitude) {
        this.name = name;
        this.id = id;
        this.city = city;
        this.state = state;
        this.country = country;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    protected EventPlace(Parcel in) {
        name = in.readString();
        id = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        address = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Creator<EventPlace> CREATOR = new Creator<EventPlace>() {
        @Override
        public EventPlace createFromParcel(Parcel in) { return new EventPlace(in); }

        @Override
        public EventPlace[] newArray(int size) { return new EventPlace[size]; }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.country);
        dest.writeString(this.address);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
    }
}
