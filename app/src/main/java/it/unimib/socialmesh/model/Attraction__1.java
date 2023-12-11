package it.unimib.socialmesh.model;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Attraction__1 {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("aliases")
    @Expose
    private List<String> aliases;
    @SerializedName("images")
    @Expose
    private List<Image__2> images;
    @SerializedName("classifications")
    @Expose
    private List<Classification__1> classifications;
    @SerializedName("_links")
    @Expose
    private Links__2 links;

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


    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public List<Image__2> getImages() {
        return images;
    }

    public void setImages(List<Image__2> images) {
        this.images = images;
    }

    public List<Classification__1> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<Classification__1> classifications) {
        this.classifications = classifications;
    }




    public Links__2 getLinks() {
        return links;
    }

    public void setLinks(Links__2 links) {
        this.links = links;
    }
}
