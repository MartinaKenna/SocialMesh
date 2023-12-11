package it.unimib.socialmesh.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class AgeRestrictions {
    @SerializedName("legalAgeEnforced")
    @Expose
    private Boolean legalAgeEnforced;

    public Boolean getLegalAgeEnforced() {
        return legalAgeEnforced;
    }

    public void setLegalAgeEnforced(Boolean legalAgeEnforced) {
        this.legalAgeEnforced = legalAgeEnforced;
    }
}
