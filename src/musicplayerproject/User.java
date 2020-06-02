/*
 * User class contains the attributes of a Client. It enables in instance of a 
 * client/user to be created with username, salt and hash, and easily able to add
 * them to an ArrayList of users. Also contains getters and setters.
 */
package musicplayerproject;

import java.util.ArrayList;

/**
 *
 * @author Zara
 */
public class User {

    //attributes
    private String username;
    private String salt;
    private String passwordHash;

    ArrayList<User> users = new ArrayList<>();

    //default constructor
    public User() {
    }

    //overload constructor for creating a new user
    public User(String userName, String salt, String passwordHash) {
        this.username = userName;
        this.salt = salt;
        this.passwordHash = passwordHash;
    }

    //getters and setters//
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @param salt the salt to set
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * @return the passwordHash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * @param passwordHash the passwordHash to set
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
