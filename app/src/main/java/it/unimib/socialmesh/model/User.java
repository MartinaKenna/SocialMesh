package it.unimib.socialmesh.model;

public class User {
    public String fullName;
    public String email;
    public String profilePicUrl;

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public User() {
    }

    public User (String fullname,String email) {
        this.fullName = fullname;
        this.email = email;
        this.profilePicUrl = null;
    }
}

