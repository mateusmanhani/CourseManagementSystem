
package coursemanagementsystem;

/**
 *
 * @author Mateus Manhani
 */
public class User {
    private String username;
    private String password;
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
    
    //update Username
    public void updateUsername (String newUsername){
        this.username = newUsername;
    }
    
    // update password
    public void updatePassword (String newPassword){
            this.password = newPassword;
    }
    
    // Hash the password...

}
