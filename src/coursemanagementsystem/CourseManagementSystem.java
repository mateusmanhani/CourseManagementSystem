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

        // Initialize UserService with the DatabaseIO instance
        UserService userService = new UserService(databaseIO);

        // Call the method to update all user passwords
        userService.updateAllUserPasswords();

        System.out.println("All user passwords have been updated.");
    }
    
}
