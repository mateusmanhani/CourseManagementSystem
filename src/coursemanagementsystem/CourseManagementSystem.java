package coursemanagementsystem;

import coursemanagementsystem.databaseImplementation.MySQLDatabaseIO;
import coursemanagementsystem.databaseImplementation.DatabaseIO;
import coursemanagementsystem.userManagement.UserService;
import coursemanagementsystem.userManagement.AuthService;
import coursemanagementsystem.reports.ReportGenerator;
import coursemanagementsystem.userManagement.Hasher;

/**
 *
 * @author Mateus Manhani
 */
public class CourseManagementSystem {

    /**
     * GITHUB: https://github.com/mateusmanhani/CourseManagementSystem
     */
    public static void main(String[] args) {
        // Initialize DatabaseIO with your actual database connection details
        DatabaseIO databaseIO = new MySQLDatabaseIO();
    
        // Initialise services
        AuthService authService = new AuthService(databaseIO);
        UserService userService = new UserService(databaseIO);
        
        // Initialise ReportGenerator
        ReportGenerator reportGenerator = new ReportGenerator(databaseIO);
        
        // Call the method to update all user passwords to hashed passwords in order to test
        // In the csv given for the users all passwords are unhashed so you can run this code to hash them 
        // you can check the actual password on the csv in order to test
        // comment after first run so passwords are not hashed twice
        
//        userService.updateAllUserPasswords();
//
//        System.out.println("All user passwords have been updated.");
        
        MenuSystem menu = new MenuSystem(authService,userService,reportGenerator);
        menu.showLoginScreen();
        
          // User admin in the users table of the cms database login information below
          // username: admin/ password: java


    }
    
}
