
package coursemanagementsystem;

/**
 *
 * @author Mateus Manhani
 */
public class User {
    private String username;
    private String password;
    private Role role;
    
    // STRATEGY iNTERFACES
    private ReportGenerationStrategy reportStrategy;
    private UserManagementStrategy userManagementStrategy;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
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

    public ReportGenerationStrategy getReportStrategy() {
        return reportStrategy;
    }

    public UserManagementStrategy getUserManagementStrategy() {
        return userManagementStrategy;
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

    public void setReportStrategy(ReportGenerationStrategy reportStrategy) {
        this.reportStrategy = reportStrategy;
    }

    public void setUserManagementStrategy(UserManagementStrategy userManagementStrategy) {
        this.userManagementStrategy = userManagementStrategy;
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
