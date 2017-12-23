package infosys_group2_4.variables;

/**
 * Created by Oon Tong on 11/20/2017.
 */
public class User {

    String username;
    String passHash;
    String Token;
    String displayName;
    int userID;

    public int getUserID() {
        return userID;
    }

    public User setUserID(int userID) {
        this.userID = userID;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public User setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getToken() {
        return Token;
    }

    public User setToken(String token) {
        Token = token;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassHash() {
        return passHash;
    }

    public User setPassHash(String passHash) {
        this.passHash = passHash;
        return this;
    }
}
