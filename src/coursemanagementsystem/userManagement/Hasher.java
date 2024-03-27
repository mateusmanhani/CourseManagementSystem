
package coursemanagementsystem.userManagement;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 *
 * @author Mateus Manhani
 */
public class Hasher {
    
        /**
     * This method takes in a plain password, a salt string, and the number of iterations
     * and returns the hashed and salted password.
     *
     * @param plainPassword The plain password to be hashed and salted.
     * @param salt A string used as salt for hashing.
     * @param iterations The number of iterations for hashing.
     * @return The hashed and salted password as a string.
     */
    public static String hashPassword(String plainPassword, String salt, int iterations) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = (plainPassword + salt).getBytes();
            for (int i = 0; i < iterations; i++) {
                md.update(hash);
                hash = md.digest();
            }
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * This method generates a salt string
     * @return String salt
     *
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }
}
