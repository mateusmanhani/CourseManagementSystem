package coursemanagementsystem.userManagement;

/**
 *
 * @author Mateus Manhani
 */
public class User {
    
    private String userID;
    private String username;
    private Role role;
    private String lecturerId;
    private String salt;
    
    public User(String userID, String username, Role role) {
        this.userID = userID;
        this.username = username;
        this.role = role;
    }
    
    public User(String userID, String username, Role role, String lecturerId, String salt) {
        this.userID = userID;
        this.username = username;
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

    public void setRole(Role role) {
        this.role = role;
    }
}
