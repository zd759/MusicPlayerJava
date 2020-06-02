/*
 * The utility class performs user functions such as add users, test users and 
 * also the loginClient method with uses PasswordManager class to obtain a boolean
 * if login is valid or not. This is then passed back to the ClientHandler class
 * to enable login.
 */
package musicplayerproject;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Zara
 */
public class UserUtility {

    //private static User users = new User();
    private static ArrayList<User> users = new ArrayList<>();

    //method to create the admin user required for assessment criteria
    public void addAdminUser() {
        String username = "admin";
        String password = "1234";
        String salt = SaltGenerator.generateSalt(10).get();
        String hash = PasswordManager.hashPassword(password, salt).get();
        User user = new User(username, salt, hash);
        users.add(user);
        System.out.println("Admin user has been added");
    }

    //method to add a user
    public void addUser() {
        System.out.println("Please enter a username to add: ");
        Scanner sc = new Scanner(System.in);
        String username = sc.next();
        System.out.println("And password: ");
        String password = sc.next();
        String salt = SaltGenerator.generateSalt(8).get();
        String hash = PasswordManager.hashPassword(password, salt).get();
        User user = new User(username, salt, hash);
        users.add(user);
        System.out.println("The following user has been added >>> ");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Hash: " + user.getPasswordHash());
        System.out.println("Salt: " + user.getSalt());
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
    public void testUsername() {
        System.out.println("Please enter a username to test: ");
        Scanner sc = new Scanner(System.in);
        String username = sc.next();
        System.out.println("Enter password: ");
        String password = sc.next();
        User name = getUser(username);
        if (name != null) {
            boolean result = PasswordManager.verifyPassword(password, name.getPasswordHash(), name.getSalt());
            if (result) {
                System.out.println("Test login verified!");
            } else {
                System.out.println("Error: Test login failed. Please try again");
            }
        } else {
            System.out.println("Test user not found in database. Please try again.");
            //testUsername();
        }
    }

    //method to print users to check if added correctly
    public void printAllUsers() {
        for (User u : users) {
            System.out.println("Username: " + u.getUsername()
                    + "\nSalt: " + u.getSalt()
                    + "\nHash: " + u.getPasswordHash());
        }
    }

    //method for client login
    public static String loginClient(String username, String password) {
        String loginResult;
        User name = getUser(username);
        if (name != null) {
            boolean result = PasswordManager.verifyPassword(password, name.getPasswordHash(), name.getSalt());
            if (result) {
                System.out.println("Client login verified!");
                loginResult = "verified";
                return loginResult;
            } else {
                System.out.println("Error: Password incorrect. Client login failed");
                loginResult = "passwordError";
                return loginResult;
            }
        } else {
            System.out.println("Client user not found in database.");
            loginResult = "usernameError";
            return loginResult;
        }
    }
}
