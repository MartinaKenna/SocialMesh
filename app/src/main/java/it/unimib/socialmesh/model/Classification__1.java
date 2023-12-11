package it.unimib.socialmesh.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Classification__1 {


    @SerializedName("segment")
    @Expose
    private Segment__1 segment;
    @SerializedName("genre")
    @Expose
    private Genre__1 genre;
    @SerializedName("subGenre")
    @Expose
    private SubGenre__1 subGenre;




    public Segment__1 getSegment() {
        return segment;
    }

    public void setSegment(Segment__1 segment) {
        this.segment = segment;
    }

    public Genre__1 getGenre() {
        return genre;
    }

    public void setGenre(Genre__1 genre) {
        this.genre = genre;
    }

    public SubGenre__1 getSubGenre() {
        return subGenre;
    }

    public void setSubGenre(SubGenre__1 subGenre) {
        this.subGenre = subGenre;
    }

}
