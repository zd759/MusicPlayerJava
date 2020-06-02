/*
 * The Salt Generator class generates the salt for password hashing.
 */
package musicplayerproject;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

/**
 *
 * @author Zara
 */
public class SaltGenerator {

    //random is required
    private static final SecureRandom RAND = new SecureRandom();

    public static Optional<String> generateSalt(final int length) {
        //error if salt too short
        if (length < 1) {
            System.err.println("error in generateSalt: length must be > 0");
            return Optional.empty();
        }
        //create byte array to store the salt size
        byte[] salt = new byte[length];
        //generate the specified number of random bytes
        RAND.nextBytes(salt);
        //return the salt string
        return Optional.of(Base64.getEncoder().encodeToString(salt));
    }
}
