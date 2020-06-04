/*
 * The utility class performs user functions such as add users, test users and 
 * also the loginClient method with uses PasswordManager class to obtain a boolean
 * if login is valid or not. This is then passed back to the ClientHandler class
 * to enable login.
 */
package musicplayerproject;

import java.util.ArrayList;

/**
 *
 * @author Zara
 */
public class UserUtility {
    //array list attribute of userUtility stores user objects
    private static final ArrayList<User> users = new ArrayList<>();

    //method to create the admin user required for assessment criteria
    public void addAdminUser() {
        String username = "admin";
        String password = "1234";
        String salt = SaltGenerator.generateSalt(10).get();
        String hash = PasswordManager.hashPassword(password, salt).get();
        User user = new User(username, salt, hash);
        users.add(user);
        System.out.println("Admin user created");
    }

    //method to add a user
    public String addUser(String username, String password) {
        String salt = SaltGenerator.generateSalt(8).get();
        String hash = PasswordManager.hashPassword(password, salt).get();
        User user = new User(username, salt, hash);
        users.add(user);
        System.out.println("The following user has been added >>> ");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Hash: " + user.getPasswordHash());
        System.out.println("Salt: " + user.getSalt());
        return "User added";
    }

    //method to retrive a username
    public static User getUser(String username) {
        User target = null;
        for (User u : users) {
            if (username.equals(u.getUsername())) {
                target = u;
            }
        }
        return target;
    }

    //method to verify a username and password
    public String testUsername(String username, String password) {
        //String return = "";
        User name = getUser(username);
        if (name != null) {
            boolean result = PasswordManager.verifyPassword(password, name.getPasswordHash(), name.getSalt());
            if (result) {
                return ("verified");
            } else {
                return ("passFailed");
            }
        } else {
            return ("userFail");
            //testUsername();
        }
    }

    //method to print users to check if added correctly
//    public void printAllUsers() {
//        for (User u : users) {
//            System.out.println("Username: " + u.getUsername()
//                    + "\nSalt: " + u.getSalt()
//                    + "\nHash: " + u.getPasswordHash());
//        }
//    }
}
