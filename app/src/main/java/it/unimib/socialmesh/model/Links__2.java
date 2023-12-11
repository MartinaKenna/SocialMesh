package it.unimib.socialmesh.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Links__2 {

    @SerializedName("self")
    @Expose
    private Self__2 self;

    public Self__2 getSelf() {
        return self;
    }

    public void setSelf(Self__2 self) {
        this.self = self;
    }
}
