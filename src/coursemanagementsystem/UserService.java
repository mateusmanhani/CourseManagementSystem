package coursemanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    
    public boolean changeMyUsername (String username){
       String query 
    }
    
    public boolean changeMyPassword (String password){
        
    }
}
