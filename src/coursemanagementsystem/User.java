
package coursemanagementsystem;

/**
 *
 * @author Mateus Manhani
 */
public class User {
    private String username;
    private String password;
    private String userID;
    private Role role;
    private ReportGenerator reportGenerator; // Use the interface

    public User(String username, String password, Role role, ReportGenerator reportGenerator) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.reportGenerator = reportGenerator;
    }
    
    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public String getUserID() {
        return userID;
    }
    
    

    // setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    
    // Hash the password...

}
