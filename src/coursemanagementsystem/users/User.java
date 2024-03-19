
package coursemanagementsystem.users;

import coursemanagementsystem.DatabaseIO;

/**
 *
 * @author Mateus Manhani
 */
public abstract class User {
    protected String username;
    protected String password;
    protected DatabaseIO databaseIO;

    public User(String username, String password, DatabaseIO databaseIO) {
        this.username = username;
        this.password = password;
        this.databaseIO = databaseIO;
    }
    
    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    // Hash the password...

}
