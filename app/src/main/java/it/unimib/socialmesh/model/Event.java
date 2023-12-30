package it.unimib.socialmesh.model;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import it.unimib.socialmesh.model.jsonFields.AgeRestrictions;
import it.unimib.socialmesh.model.jsonFields.Classification;
import it.unimib.socialmesh.model.jsonFields.Dates;
import it.unimib.socialmesh.model.jsonFields.Embedded_1;
import it.unimib.socialmesh.model.jsonFields.Genre;
import it.unimib.socialmesh.model.jsonFields.Image;
import it.unimib.socialmesh.repository.EventsRepository;
import it.unimib.socialmesh.util.Converters;


@Entity
public class Event implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    private long localId;

    private static final String TAG = Event.class.getSimpleName();
    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("id")
    @Expose
    private String remoteId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected Event(Parcel in) {
        localId = in.readLong();
        name = in.readString();
        type = in.readString();
        remoteId = in.readString();
        description = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public long getLocalId() {
        return localId;
    }

    public void setLocalId(long localId) {
        this.localId = localId;
    }

    @TypeConverters(Converters.class)
    @SerializedName("images")
    @Expose
    private List<Image> images;

    @TypeConverters(Converters.class)
    @SerializedName("classifications")
    @Expose
    private List<Classification> classifications;
    @Embedded(prefix = "dates_")
    @SerializedName("dates")
    @Expose
    private Dates dates;

    @Embedded(prefix = "genre_")
    @SerializedName("genre")
    @Expose
    private Genre genre;

    /* @SerializedName("info")
     @Expose
     private String info;
     @Embedded(prefix = "ageRestrictions_")
   @SerializedName("ageRestrictions")
     @Expose
     private AgeRestrictions ageRestrictions;
     @Embedded(prefix = "_embedded_")
     @SerializedName("_embedded")
     @Expose
     private Embedded_1 embedded;
 */
    public Event(){}

    //getName con il filtro del max 20 parole
    public String getName1() {
        if (name.length() > 20) {
            int count = 0;
            StringBuilder word = new StringBuilder();

            while (count < name.length()) {
                char character = name.charAt(count);
                if (character == '-' || character == ':') {
                    return word.toString();
                }
                word.append(character);
                count++;
            }
        }
        return name;
    }

    public String getName(){
        return name;
    }




    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getRemoteId() {
        return remoteId;
    }
    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public Dates getDates() {
        return dates;
    }

    public String getDates1() {

        int count = 0;
        StringBuilder date = new StringBuilder();
        if (dates != null && dates.getStart() != null && dates.getStart().getDateTime() != null){
            while (count < dates.getStart().getDateTime().length()) {
                char character = dates.getStart().getDateTime().charAt(count);
                if (character == 'T') {
                    return date.toString();
                }
                date.append(character);
                count++;
            }
        }


        return date.toString();
    }


   public List<Image> getImages() {
        return images;
    }
    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void setDates(Dates dates) {
        this.dates = dates;
    }

    public Genre getGenre(){
        return genre;
    }

    public void setGenre(Genre genre){
        this.genre=genre;
    }

    public List<Classification> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<Classification> classifications){
        this.classifications=classifications;
    }

    public String getGenreName(){
        Log.d(TAG, classifications.get(0).getGenre().getName());
        return classifications.get(0).getGenre().getName();
    }
   /* public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
   public AgeRestrictions getAgeRestrictions() {
        return ageRestrictions;
    }
    public void setAgeRestrictions(AgeRestrictions ageRestrictions) {
        this.ageRestrictions = ageRestrictions;
    }


    public Embedded_1 getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded_1 embedded) {
        this.embedded = embedded;
    }*/

    public String getUrlImages() {
        return images.get(0).getUrlImages();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(localId);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(remoteId);
    }
}


