package it.unimib.socialmesh.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Event implements Parcelable {
    private String name;
    private String id;
    private String url;
    private String startDate;
    private String time;
    private String notes;
    private String image;
    private Boolean ageRestrictions;
    private EventPlace place;

    public Event() {}

    public Event(String name, String id, String url, String image, String startDate,
                 String time, String notes, boolean ageRestrictions, EventPlace place) {
        this.name = name;
        this.id = id;
        this.url = url;
        this.startDate = startDate;
        this.time = time;
        this.notes = notes;
        this.image = image;
        this.ageRestrictions = ageRestrictions;
        this.place = place;
    }

    protected Event(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
        this.url = in.readString();
        this.startDate = in.readString();
        this.time = in.readString();
        this.notes = in.readString();
        this.image = in.readString();
        this.ageRestrictions = in.readByte() != 0;
        this.place = in.readParcelable(EventPlace.class.getClassLoader());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getAgeRestrictions() {
        return ageRestrictions;
    }

    public void setAgeRestrictions(Boolean ageRestrictions) {
        this.ageRestrictions = ageRestrictions;
    }


    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.url);
        dest.writeString(this.startDate);
        dest.writeString(this.time);
        dest.writeString(this.notes);
        dest.writeString(this.image);
        dest.writeByte(this.ageRestrictions ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.place, flags);
    }
}
