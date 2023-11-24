package it.unimib.socialmesh.model;

public class Event {
    private String name;
    private String id;
    private String url;
    private String startDate;
    private String endDate;
    private String notes;
    private String image;
    private Boolean ageRestrictions;

    public Event(String name, String id, String url,
                 String image,
                 String startDate, String endDate,
                 String notes,
                 Boolean ageRestrictions) {
        this.name = name;
        this.id = id;
        this.url = url;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.image = image;
        this.ageRestrictions = ageRestrictions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getAgeRestrictions() {
        return ageRestrictions;
    }

    public void setAgeRestrictions(Boolean ageRestrictions) {
        this.ageRestrictions = ageRestrictions;
    }
}
