package coursemanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Mateus Manhani
 */
public class UserService {

    private DatabaseIO databaseIO;

    public UserService(DatabaseIO databaseIO) {
        this.databaseIO = databaseIO;
    }

    public boolean addUSer(User user) {
        String query = " INSERT INTO users (user_id, username, password, role, lecturer_id) VALUES ( ?, ?, ?, ?);";
        try ( Connection conn = databaseIO.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUserID());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole().name());
            stmt.setString(5, user.getRole() == Role.LECTURER ? user.getLecturerId() : null); // if user role is LECTURER also insert lecturerId else insert null

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

    public boolean updateUser(User user) {
        String query = "UPDATE users SET username = ?, password = ?, role = ?, lecture_id = ? WHERE user_id = ?;";
        try ( Connection conn = databaseIO.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().name());
            stmt.setString(4, user.getRole() == Role.LECTURER ? user.getLecturerId() : null); // if user role is LECTURER also insert lecturerId else insert null
            stmt.setString(5, user.getUserID());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; //return true if any rows are affected
            
        }catch (SQLException e) {
            System.out.println("Error adding user.");
            e.getSQLState();
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

            String salt = IterativeHasher.generateSalt();
            String hashedPassword = IterativeHasher.hashPassword("defaultPassword", salt, 1000); // Example use

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
    
//    public boolean changeMyUsername (String username){
//       String query 
//    }
//    
//    public boolean changeMyPassword (String password){
//        
//    }
}
