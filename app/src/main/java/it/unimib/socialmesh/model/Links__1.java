package it.unimib.socialmesh.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Links__1 {

    @SerializedName("self")
    @Expose
    private Self__1 self;

    public Self__1 getSelf() {
        return self;
    }

    public void setSelf(Self__1 self) {
        this.self = self;
    }
}
