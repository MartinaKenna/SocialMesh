package it.unimib.socialmesh.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Dates {

    @SerializedName("initialStartDate")
    @Expose
    private InitialStartDate initialStartDate;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("spanMultipleDays")
    @Expose
    private Boolean spanMultipleDays;

    public InitialStartDate getInitialStartDate() {
        return initialStartDate;
    }

    public void setInitialStartDate(InitialStartDate initialStartDate) {
        this.initialStartDate = initialStartDate;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getSpanMultipleDays() {
        return spanMultipleDays;
    }

    public void setSpanMultipleDays(Boolean spanMultipleDays) {
        this.spanMultipleDays = spanMultipleDays;
    }
}
