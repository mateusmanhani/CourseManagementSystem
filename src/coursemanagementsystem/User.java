package coursemanagementsystem;

/**
 *
 * @author Mateus Manhani
 */
public class User {
    
    private String userID;
    private String username;
    private String password;
    private Role role;
    private String lecturerId;
    private String salt;
    
    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    public User(String userID, String username, String password, Role role, String lecturerId, String salt) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.role = role;
        this.lecturerId = lecturerId;
        this.salt = salt;
    }
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getLecturerId() {
        return lecturerId;
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
}
