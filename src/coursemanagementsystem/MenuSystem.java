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
        boolean validInput = false;
        while(!validInput){
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
    
    public void showUserMenu(User user){
        Role userRole = user.getRole();
        
        switch(userRole){
            case OFFICE:
                // SHOW OFFICE MENU
            case LECTURER:
                //SHOW LECTURER MENU
            case ADMIN:
                //SHOW ADMIN MENU
        }
        
    }
    
    public void adminMenu(){
        //addUser
        
        //updateUser
            //role
            //username
            //password
        
        //deleteUser
        
        // change my username
        
        //change my password
    }
    
    public void lecturerMenu(){
        //generate my report
        
        // change my password
        
        //change my username
    }
    
    public void officeMenu(){
        //generate student report
        
        //generate lecturer report
        
        //generate course report
        
        //change my password
        
        //change my username
    }
}
