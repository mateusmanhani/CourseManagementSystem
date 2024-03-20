
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
    
    public User authenticateUser(String username, String password) throws SQLException{
        String query = "SELECT user_id, username, rolw, lecturer_id FROM users WHERE username = ? AND password = ?";
        
        try(Connection conn = databaseIO.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    String userId = rs.getString("user_id");
                    String roleStr = rs.getString("role");
                    Role role = Role.valueOf(roleStr.toUpperCase());
                    String lecturerId = rs.getString("lecture_id");
                    return new User(userId, username, role, lecturerId);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
