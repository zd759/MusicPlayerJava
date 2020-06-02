/*
 * Password manager class uses an algorithm to hash plain text passwords. It can
 * also check if a specfic password is or isn't a match to an existing one.
 */
package musicplayerproject;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Zara
 */
public class PasswordManager {

    //attributes
    //iterations is # times to perform hashing algorithm
    private static final int ITERATIONS = 49152;
    //key length is desired length of the resulting cryptographic key in bits
    private static final int KEY_LENGTH = 384;
    //algorithm type SHA (Secure Hash Algorithm) 384 bits hash 'key stretching'
    private static final String ALGORITHM = "PBKDF2WithHmacSHA384";

    //method to hash the password. changes string to byte[], garbage collector gets
    //rid of string. String are not secure way to store passwords as they are immutable
    //and cannot change; salt cannot be added so must be stored in different way.
    public static Optional<String> hashPassword(String plainTextPassword, String salt) {
        //set up arrays
        char[] chars = plainTextPassword.toCharArray();
        byte[] bytes = salt.getBytes();
        //specify how to generate hashed password char=password, bytes=salt string, spec= all info (needs deleting)
        PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);
        //clear char array when done
        Arrays.fill(chars, Character.MIN_VALUE);
        //try to use algorithm specified to hash the password
        try {
            SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
            //get encocded- gets hashed password as a byte and save it as securePassword
            byte[] securePassword = fac.generateSecret(spec).getEncoded();
            //encode byte[] in base-64 (ASCII chars) and return as string so can save in database
            return Optional.of(Base64.getEncoder().encodeToString(securePassword));
        //if any problems return empty optional
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.err.println("Exception encountered in hashing the password");
            return Optional.empty();

        } finally {
            //clear password from spec variable to maintain security. exucuted after try OR catch
            spec.clearPassword();
        }
    }
    //method to determine a given password matches or not - used by loginClient method in UserUtility
    public static boolean verifyPassword(String password, String key, String salt) {
        Optional<String> optEncrypted = hashPassword(password, salt);
        if (!optEncrypted.isPresent()) {
            return false;
        }
        return optEncrypted.get().equals(key);
    }
}
