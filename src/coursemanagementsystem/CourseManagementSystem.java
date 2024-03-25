package coursemanagementsystem;

import coursemanagementsystem.databaseImplementation.MySQLDatabaseIO;
import coursemanagementsystem.databaseImplementation.DatabaseIO;
import coursemanagementsystem.userManagement.UserService;
import coursemanagementsystem.userManagement.AuthService;
import coursemanagementsystem.reports.ReportGenerator;

/**
 *
 * @author Mateus Manhani
 */
public class CourseManagementSystem {

    /**
     * 
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
//        userService.updateAllUserPasswords();
//
//        System.out.println("All user passwords have been updated.");
        
        MenuSystem menu = new MenuSystem(authService,userService,reportGenerator);
        menu.showLoginScreen();
        
//        // Instanciate Test admin user/ Please uncomment this section to test or use the admin user in the users table or the office user
          // user: admin/ password: java
          //user: office/ password office123
        
//        Role role = Role.ADMIN;
//        String adminUserId = "123";
//        User adminUser = new User(adminUserId,"admin",role);
//        
//        String adminPassword = "java";
//        String adminSalt = Hasher.generateSalt();
//        
//        boolean addedAdmin = userService.addUSer(adminUser, adminPassword, adminSalt);
//        
//        if (addedAdmin){
//            // Call menuSytem
//            MenuSystem menu = new MenuSystem(authService,userService,reportGenerator);
//            menu.showLoginScreen();
//        }
    }
    
}
