package coursemanagementsystem;

/**
 *
 * @author Mateus Manhani
 */
public class CourseManagementSystem {

    /**
     * 
     */
    public static void main(String[] args) {

//        // Call the method to update all user passwords to hashed passwords in order to test
//        userService.updateAllUserPasswords();
//
//        System.out.println("All user passwords have been updated.");
    

    
        // Initialize DatabaseIO with your actual database connection details
        DatabaseIO databaseIO = new MySQLDatabaseIO();
    
        // Initialise services
        AuthService authService = new AuthService(databaseIO);
        UserService userService = new UserService(databaseIO);
        
        // Initialise ReportGenerator
        ReportGenerator reportGenerator = new ReportGenerator(databaseIO);
        
        MenuSystem menu = new MenuSystem(authService,userService,reportGenerator);
            menu.showLoginScreen();
        
//        // Instanciate Test admin user/ Please uncomment this section to test
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
