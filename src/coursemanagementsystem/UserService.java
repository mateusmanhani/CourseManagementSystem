package coursemanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author Mateus Manhani
 */
public class UserService {

    private DatabaseIO databaseIO;

    public UserService(DatabaseIO databaseIO) {
        this.databaseIO = databaseIO;
    }

    public boolean addUSer(User user, String salt) {
        // Hash the password before storing it in the database
        String hashedPassword = Hasher.hashPassword(user.getPassword(), salt, 1000);
        
        String query = "INSERT INTO users (user_id, username, password, role, lecturer_id, salt) VALUES (?, ?, ?, ?, ?, ?)";
        try ( Connection conn = databaseIO.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUserID());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, hashedPassword);
            stmt.setString(4, user.getRole().name());
            stmt.setString(5, user.getRole() == Role.LECTURER ? user.getLecturerId() : null); // if user role is LECTURER also insert lecturerId else insert null
            stmt.setString(6, salt);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // If any rows were actually updated it ill return true;

        } catch (SQLException e) {
            System.out.println("Error adding user.");
            e.getSQLState();
            return false;
        }
    }

    public boolean deleteUser(String userId) {
        String query = "DELETE FROM users WHERE user_id = ?";
        try ( Connection conn = databaseIO.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; //return true if any rows are affected.

        } catch (SQLException e) {
            System.out.println("Error deleting user.");
            e.getSQLState();
            return false;
        }
    }

    public boolean updateUser(User user, Optional<String> newPassword) {
        // Start building the SQL query
        String baseQuery = "UPDATE users SET username = ?, role = ?, lecturer_id = ?";
        String passwordQueryPart = ", password = ?, salt = ?";
        String whereClause = " WHERE user_id = ?";

        // Decide if password needs to be updated
        boolean updatePassword = newPassword.isPresent();
        String query = baseQuery + (updatePassword ? passwordQueryPart : "") + whereClause;

        try (Connection conn = databaseIO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set common fields: username, role, lecturer_id
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getRole().name());
            stmt.setString(3, user.getLecturerId()); // This can be null

            int paramIndex = 4; // Next parameter index

            if (updatePassword) {
                // Extract the password value safely
                String actualNewPassword = newPassword.get();
                // Hash the password
                String newSalt = Hasher.generateSalt();
                String hashedPassword = Hasher.hashPassword(actualNewPassword, newSalt, 1000);
                stmt.setString(paramIndex++, hashedPassword);
                stmt.setString(paramIndex++, newSalt);
            }

            stmt.setString(paramIndex, user.getUserID()); // Finally, set the user_id for the WHERE clause

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            return false;
        }
    }
    
    public void updateAllUserPasswords() {
    String selectQuery = "SELECT user_id FROM users";
    String updateQuery = "UPDATE users SET password = ?, salt = ? WHERE user_id = ?";

    try (Connection conn = databaseIO.getConnection();
         PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
         ResultSet rs = selectStmt.executeQuery()) {

        if (!rs.isBeforeFirst() ) {    
            System.out.println("No users found to update."); 
            return;
        }

        while (rs.next()) {
            String userId = rs.getString("user_id");

            String salt = Hasher.generateSalt();
            String hashedPassword = Hasher.hashPassword("defaultPassword", salt, 1000); // Example use

            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, hashedPassword);
                updateStmt.setString(2, salt);
                updateStmt.setString(3, userId);

                int updatedRows = updateStmt.executeUpdate();
                System.out.println("Updated " + updatedRows + " rows for user ID: " + userId);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("SQL Exception: " + e.getMessage());
    }
}
    
    public boolean changeMyUsername(String userId, String newUsername) {
        String query = "UPDATE users SET username = ? WHERE user_id = ?";
        try (Connection conn = databaseIO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newUsername);
            stmt.setString(2, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; //If any rows are affected return true
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean changeMyPassword(String userId, String newPassword, String salt) {
    // Hash the new password before updating the database
    String hashedPassword = Hasher.hashPassword(newPassword, salt, 1000); // Example: Using IterativeHasher to hash the password
    
    String query = "UPDATE users SET password = ?, salt = ? WHERE user_id = ?";
    try (Connection conn = databaseIO.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, hashedPassword);
        stmt.setString(2, salt);
        stmt.setString(3, userId);
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0; //If any rows are affected return true
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    
    public boolean changeMyRole(String userId, Role newRole) {
        String query = "UPDATE users SET role = ? WHERE user_id = ?";
        try (Connection conn = databaseIO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newRole.name());
            stmt.setString(2, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; //If any rows are affected return true
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // method to generate unique userIds
    public String generateUniqueUserID(){
        return UUID.randomUUID().toString();
    }
    
    // Method to fetch an user from the database by Id
    public User fetchUserById(String userID){
        String query = "SELECT username, password, role, lecturer_id FROM users WHERE user_id = ?";
        User user = null;
        
        try (Connection conn = databaseIO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, userID);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next()){
                String username = rs.getString("username");
                String roleStr = rs.getString("role");
                Role role = Role.valueOf(roleStr.toUpperCase()); 
                String lecturerId = rs.getString("lecturer_id"); // may be null 
                String salt = rs.getString("salt"); // Retrieve the salt
                
                user = new User(userID, username, role, lecturerId, salt);
            }
        }catch (SQLException e) {
            System.out.println("SQLException in fetchUserById: " + e.getMessage());
        }
        return user;
    }
}
