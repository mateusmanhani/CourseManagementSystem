package coursemanagementsystem.userManagement;

import coursemanagementsystem.databaseImplementation.DatabaseIO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    
    /**
     * This method allows changing updating a user in the database
     * @param user the user object containing the information to be updated
     * @param plainPassword optional parameter if password is to be changed
     * @param salt salt string for the password
     * 
     * @return true if user was successfully updated and false if not
     */
    public boolean addUSer(User user,String plainPassword, String salt) {
        // Hash the password before storing it in the database
        String hashedPassword = Hasher.hashPassword(plainPassword, salt, 1000);
         String lockQuery = "LOCK TABLES users WRITE";
        String unlockQuery = "UNLOCK TABLES";
        String addUserQuery = "INSERT INTO users (user_id, username, password, role, lecturer_id, salt) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = databaseIO.getConnection(); 
         Statement lockStmt = conn.createStatement()) {
        
            // Lock the table before the insert operation
            lockStmt.execute(lockQuery);

            try (PreparedStatement stmt = conn.prepareStatement(addUserQuery)) {

                stmt.setString(1, user.getUserID());
                stmt.setString(2, user.getUsername());
                stmt.setString(3, hashedPassword);
                stmt.setString(4, user.getRole().name());
                stmt.setString(5, user.getRole() == Role.LECTURER ? user.getLecturerId() : null); // if user role is LECTURER also insert lecturerId else insert null
                stmt.setString(6, salt);

                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0; // If any rows were actually updated return true;

            } finally {
            // Ensure the table is always unlocked, regardless of whether an exception was thrown
            try {
                lockStmt.execute(unlockQuery);
            } catch (SQLException e) {
                System.out.println("Error unlocking tables: " + e.getMessage());
                // Handle or log the unlock exception if needed
               }
            }
        } catch (SQLException e) {
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Message: " + e.getMessage());
            return false;
        }
    }
/**
     * This method allows deleting a user in the database
     * @param userId the userId of the user to be deleted
     *      
     * @return true if user was successfully deleted and false if not
     */
    public boolean deleteUser(String userId) {
        String lockQuery = "LOCK TABLES users WRITE";
        String unlockQuery = "UNLOCK TABLES";
        String deleteUserQuery = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = databaseIO.getConnection();
         Statement lockStmt = conn.createStatement()) {

            // Lock the table
            lockStmt.execute(lockQuery);
            try (PreparedStatement stmt = conn.prepareStatement(deleteUserQuery)) {

                stmt.setString(1, userId);
                int rowsAffected = stmt.executeUpdate();
                
                // Unlock the table after the operation
                lockStmt.execute(unlockQuery);
                
                return rowsAffected > 0; //return true if any rows are affected.
            } finally { // finally block to execute unlock wether an exception occurs within the try block or not
            lockStmt.execute(unlockQuery);
        }
        }catch (SQLException e) {
            System.out.println("Error deleting user.");
            e.getSQLState();
            return false;
        }
    }
    /**
     * This method allows changing updating a user in the database
     * @param user the user object containing the information to be updated
     * @param newPassword optional parameter if password is to be changed
     * 
     * @return true if user was successfully updated and false if not
     */
    public boolean updateUser(User user, Optional<String> newPassword) {
        String lockQuery = "LOCK TABLES users WRITE";
        String unlockQuery = "UNLOCK TABLES";

        // Start building the SQL query
        String baseQuery = "UPDATE users SET username = ?, role = ?, lecturer_id = ?";
        String passwordQueryPart = ", password = ?, salt = ?";
        String whereClause = " WHERE user_id = ?";

        Connection conn = null;
        Statement lockStmt = null;
        try {
            conn = databaseIO.getConnection();
            lockStmt = conn.createStatement();

            // Lock the table before any operations
            lockStmt.execute(lockQuery);

            boolean updatePassword = newPassword.isPresent();
            String query = baseQuery + (updatePassword ? passwordQueryPart : "") + whereClause;

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
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
        } catch (SQLException e) {
            System.out.println("Error managing database resources: " + e.getMessage());
            return false;
        } finally {
            if (lockStmt != null) {
                try {
                    // Unlock the table in the finally block to ensure it executes
                    lockStmt.execute(unlockQuery);
                } catch (SQLException e) {
                    System.out.println("Error unlocking tables: " + e.getMessage());
                }
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    /**
     * This method goes through the users table and hashes all passwords
     * 
     */
    public void updateAllUserPasswords() {
        String lockQuery = "LOCK TABLES users WRITE";
        String unlockQuery = "UNLOCK TABLES";
        
        String selectQuery = "SELECT user_id, password FROM users";
        String updateQuery = "UPDATE users SET password = ?, salt = ? WHERE user_id = ?";
        
        try (Connection conn = databaseIO.getConnection();
         Statement lockStmt = conn.createStatement()) {

            // Lock the table before any operations
            lockStmt.execute(lockQuery);
            
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                 ResultSet rs = selectStmt.executeQuery()) {

                if (!rs.isBeforeFirst()) {
                    System.out.println("No users found to update.");
                    return;
                }

                while (rs.next()) {
                    String userId = rs.getString("user_id");
                    String rawPassword = rs.getString("password");

                    // Generate a new salt
                    String salt = Hasher.generateSalt();

                    // Hash the raw password with the generated salt
                    String hashedPassword = Hasher.hashPassword(rawPassword, salt, 1000);

                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setString(1, hashedPassword);
                        updateStmt.setString(2, salt);
                        updateStmt.setString(3, userId);

                        int updatedRows = updateStmt.executeUpdate();
                        System.out.println("Updated " + updatedRows + " rows for user ID: " + userId);
                    }
                }

            } catch (SQLException e) {
                System.out.println("Error updating all user passwords: " + e.getMessage());
                System.out.println("SQL Exception: " + e.getMessage());
            }finally{
                // Ensure the table is unlocked even if there's an exception
                try{
                    lockStmt.execute(unlockQuery);
                } catch(SQLException e){
                    System.out.println("Error unlocking table: " + e.getMessage());
                }
            }
        }catch (SQLException e) {
        System.out.println("Error : " + e.getMessage());
    }
}
    /**
     * This method allows changing a user's username in the database
     * @param userId the user id of the user to be updated
     * @param newUsername the new password to be set
     * 
     * @return true if username was changed and false if not
     */
    public boolean changeMyUsername(String userId, String newUsername) {
        String lockQuery = "LOCK TABLES users WRITE";
        String unlockQuery = "UNLOCK TABLES";
        String updateUsernameQuery = "UPDATE users SET username = ? WHERE user_id = ?";
        
        try (Connection conn = databaseIO.getConnection();
         Statement lockStmt = conn.createStatement()) {

            // Lock the table before any operations
            lockStmt.execute(lockQuery);
            
            try (PreparedStatement stmt = conn.prepareStatement(updateUsernameQuery)) {
                stmt.setString(1, newUsername);
                stmt.setString(2, userId);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0; //If any rows are affected return true
            }catch (SQLException e) {
                System.out.println("Error changing the username: " + e.getMessage());
                return false;
            } finally{
                // Ensure the table is unlocked even if there's an exception
                try{
                    lockStmt.execute(unlockQuery);
                } catch(SQLException e){
                    System.out.println("Error unlocking table: " + e.getMessage());
                }
            }
        }catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        return false;
    }
    }
    /**
     * This method allows changing a user's password in the database
     * @param userId the user id of the user to be updated
     * @param newPassword the new password to be set
     * @param salt the salt string for the new password
     * @return true if password was changed and false if not
     */
    public boolean changeMyPassword(String userId, String newPassword, String salt) {
        // Hash the new password before updating the database
        String hashedPassword = Hasher.hashPassword(newPassword, salt, 1000); // Example: Using IterativeHasher to hash the password
    
        String lockQuery = "LOCK TABLES users WRITE";
        String unlockQuery = "UNLOCK TABLES";
    
        String updatePasswordQuery = "UPDATE users SET password = ?, salt = ? WHERE user_id = ?";
        
        try (Connection conn = databaseIO.getConnection(); 
         Statement lockStmt = conn.createStatement()) {

            // Lock the table before any operations
            lockStmt.execute(lockQuery);
        
            try (PreparedStatement stmt = conn.prepareStatement(updatePasswordQuery)) {
                stmt.setString(1, hashedPassword);
                stmt.setString(2, salt);
                stmt.setString(3, userId);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0; //If any rows are affected return true
            } catch (SQLException e) {
                System.out.println("Error changing the password: " + e.getMessage());
                return false;
            }finally{
                // Ensure the table is unlocked even if there's an exception
                try{
                    lockStmt.execute(unlockQuery);
                } catch(SQLException e){
                    System.out.println("Error unlocking table: " + e.getMessage());
                }
            }
        }catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        return false;
    }
}
    /**
     * This method allows changing a user's role in the database
     * @param userId the user id of the user to be updated
     * @param newRole the role to be updated
     * 
     * @return true if role was successfully changed and false if not
     */
    public boolean changeMyRole(String userId, Role newRole) {
        String lockQuery = "LOCK TABLES users WRITE";
        String unlockQuery = "UNLOCK TABLES";
        String updateRoleQuery = "UPDATE users SET role = ? WHERE user_id = ?";
        
        try (Connection conn = databaseIO.getConnection(); 
         Statement lockStmt = conn.createStatement()) {

            // Lock the table before any operations
            lockStmt.execute(lockQuery);
            try (PreparedStatement stmt = conn.prepareStatement(updateRoleQuery)) {
                stmt.setString(1, newRole.name());
                stmt.setString(2, userId);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0; //If any rows are affected return true
            } catch (SQLException e) {
                System.out.println("Error changing the role: " + e.getMessage());
                return false;
            }finally{
                // Ensure the table is unlocked even if there's an exception
                try{
                    lockStmt.execute(unlockQuery);
                } catch(SQLException e){
                    System.out.println("Error unlocking table: " + e.getMessage());
                }
            }
        }catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        return false;
    }
    }
    
    /**
     * This method generates a string to represent a unique user id
     * @return String uniqueID randomly generated string
     */
    public String generateUniqueUserID(){
        return UUID.randomUUID().toString();
    }
    
    /**
     * This method fetches a user in the database by their user id.
     * @param userID the user id of the desired user
     * 
     * @return user object
     */
    public User fetchUserById(String userID){
        String fetchUserQuery = "SELECT username, password, role, lecturer_id, salt FROM users WHERE user_id = ?";
        User user = null;
        
        try (Connection conn = databaseIO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(fetchUserQuery)) {
            
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
