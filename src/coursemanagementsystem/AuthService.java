package coursemanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Mateus
 */
public class AuthService {

    private DatabaseIO databaseIO;

    public AuthService(DatabaseIO databaseIO) {
        this.databaseIO = databaseIO;
    }

    public User authenticateUser(String username, String enteredPassword) throws SQLException {
        String query = "SELECT user_id, username, role, lecturer_id, password, salt FROM users WHERE username = ?";

        try ( Connection conn = databaseIO.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String userId = rs.getString("user_id");
                    String roleStr = rs.getString("role");
                    Role role = Role.valueOf(roleStr.toUpperCase()); // Ensure your Role enum matches the database
                    String lecturerId = rs.getString("lecturer_id"); // Corrected the field name
                    String storedHash = rs.getString("password");
                    String salt = rs.getString("salt");

                    // hash the entered password with the stored salt using the same iterations used for hashing stpred passwords
                    String hashedEnteredPassword = Hasher.hashPassword(enteredPassword, salt, 1000);

                    // compare the hashed entered password with the stored hash
                    if (hashedEnteredPassword.equals(storedHash)) {
                        //Authentication Successfull
                        return new User(userId, username, role, lecturerId);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null; // Authentication failed
        }
    }
}
