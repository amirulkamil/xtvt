package co.xtvt.xtvt;

/**
 * Created by User on 28/11/2017.
 */

public class User {
    String userId;
    String userName;
    String userEmail;

    public User(){

    }

    public User(String userId, String userName, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
