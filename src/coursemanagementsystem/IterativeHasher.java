
package coursemanagementsystem;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 *
 * @author Mateus Manhani
 */
public class IterativeHasher {
    
    public static String hashPassword(String password, String salt, int iterations) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = (password + salt).getBytes();
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
}
