
package coursemanagementsystem;

/**
 *
 * @author Mateus Manhani
 */
public interface UserManagementStrategy {
    
    public void addUser(User user);
    
    public void modifyUser(User user);
    
    public void deleteUser(User user);
}
