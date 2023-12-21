package it.unimib.socialmesh.model;
import android.util.Log;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import it.unimib.socialmesh.model.jsonFields.AgeRestrictions;
import it.unimib.socialmesh.model.jsonFields.Dates;
import it.unimib.socialmesh.model.jsonFields.Embedded_1;
import it.unimib.socialmesh.model.jsonFields.Image;
import it.unimib.socialmesh.repository.EventsRepository;
import it.unimib.socialmesh.util.Converters;


@Entity
public class Event {
    @PrimaryKey(autoGenerate = true)
    private long localId;

    private static final String TAG = EventsRepository.class.getSimpleName();
    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String remoteId;

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
    @Embedded(prefix = "dates_")
    @SerializedName("dates")
    @Expose
    private Dates dates;

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
    public String getName() {
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
        return dates.getStart().getDateTime();
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

    public String getName(String filter) {
        if(filter.equalsIgnoreCase("rock") && getType().equalsIgnoreCase("rock")){
            return name;
        }
        else if(filter.equalsIgnoreCase("latin") && getType().equalsIgnoreCase("latin")){
            return name;
        }
        else if(filter.equalsIgnoreCase("hiphoprap") && getType().equalsIgnoreCase("hip-hop/rap")){
            Log.d(TAG, "dentro if" + name);
            return name;
        }
        else if(filter.equals("")){
            return name;
        }
        return null;
    }
    public String getUrlImages() {
        return images.get(0).getUrlImages();
    }

}