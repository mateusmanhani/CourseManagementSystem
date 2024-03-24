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
//        DatabaseIO dbIO = new MySQLDatabaseIO();
//        User user = new User("admin","java", Role.OFFICE);
//        ReportGenerator rg = new ReportGenerator(user,dbIO);
//        
//        
//        
//        rg.outputCourseReport("64c125f1", OutputType.CONSOLE);
//        
//        rg.outputStudentReport("s2",OutputType.CONSOLE);

        // Initialize DatabaseIO with your actual database connection details
        DatabaseIO databaseIO = new MySQLDatabaseIO();

//        // Call the method to update all user passwords
//        userService.updateAllUserPasswords();
//
//        System.out.println("All user passwords have been updated.");
    
    
        // Initialise services
        AuthService authService = new AuthService(databaseIO);
        UserService userService = new UserService(databaseIO);
        
        // Initialise ReportGenerator
        ReportGenerator reportGenerator = new ReportGenerator(databaseIO);
        
    
   
         // Call menuSytem
        MenuSystem menu = new MenuSystem(authService,userService,reportGenerator);
    }
    
}
