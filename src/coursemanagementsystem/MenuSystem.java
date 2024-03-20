package coursemanagementsystem;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mateus Manhani
 */
public class MenuSystem {
    private AuthService authService;
    
    public MenuSystem(DatabaseIO databaseIO){
        this.authService = new AuthService(databaseIO);
    }
    
    public void showLoginScreen(){
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Welcome to the Course Management System (CMS)");
            
            System.out.println("Username: ");
            String username = sc.nextLine();
            
            System.out.println("Password: ");
            String password = sc.nextLine();
            
            User user = authService.authenticateUser(username, password);
            
            if(user != null){
                System.out.println("Login Successful.");
                // continue with operations...
            }else{
                System.out.println("Login failed. Please check your username and password.");
                // do a loop to keep going
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
