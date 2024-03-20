package coursemanagementsystem;

/**
 *
 * @author Mateus Manhani
 */
public class MenuSystem {
    private AuthService authService;
    
    public MenuSystem(DatabaseIO databaseIO){
        this.authService = new AuthService(databaseIO);
    }
}
