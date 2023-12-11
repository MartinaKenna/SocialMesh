package it.unimib.socialmesh.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Classification {


    @SerializedName("genre")
    @Expose
    private Genre genre;
    @SerializedName("subGenre")
    @Expose
    private SubGenre subGenre;
    @SerializedName("type")
    @Expose
    private Type type;
    @SerializedName("subType")
    @Expose
    private SubType subType;
    @SerializedName("family")
    @Expose
    private Boolean family;

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public SubGenre getSubGenre() {
        return subGenre;
    }

    public void setSubGenre(SubGenre subGenre) {
        this.subGenre = subGenre;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public SubType getSubType() {
        return subType;
    }

    public void setSubType(SubType subType) {
        this.subType = subType;
    }

    public Boolean getFamily() {
        return family;
    }

    public void setFamily(Boolean family) {
        this.family = family;
    }
}
