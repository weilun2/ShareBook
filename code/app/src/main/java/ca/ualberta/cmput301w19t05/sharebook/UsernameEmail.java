package ca.ualberta.cmput301w19t05.sharebook;

public class UsernameEmail {
    private String userID;
    private String username;
    private String email;

    public UsernameEmail(String userID, String username, String email) {
        this.userID = userID;
        this.username = username;
        this.email = email;
    }

    public UsernameEmail() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
