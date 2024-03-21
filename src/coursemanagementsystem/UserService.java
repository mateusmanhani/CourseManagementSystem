
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
    
    public UserService(DatabaseIO databaseIO){
        this.databaseIO = databaseIO;
    }
    
    public boolean addUSer(User user){
        String query = " INSERT INTO users (username, password, role, lecturer_id) VALUES ( ?, ?, ?, ?);";
        try(Connection conn = databaseIO.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().toString());
            stmt.setString(4,user.getLecturerId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // If any rows were actually updated it ill return true;
            
        }catch(SQLException e){
            System.out.println("Error adding user.");
            e.getSQLState();
            return false;   
        }
    }
}
