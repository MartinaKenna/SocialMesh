package it.unimib.socialmesh.model;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Event {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("images")
    @Expose
    private List<Image> images;

    @SerializedName("dates")
    @Expose
    private Dates dates;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("ageRestrictions")
    @Expose
    private AgeRestrictions ageRestrictions;

    @SerializedName("_embedded")
    @Expose
    private Embedded_1 embedded;

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



    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }



    public Dates getDates() {
        return dates;
    }

    public void setDates(Dates dates) {
        this.dates = dates;
    }



    public String getInfo() {
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
    }

}


