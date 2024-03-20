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
        DatabaseIO dbIO = new MySQLDatabaseIO();
        User user = new User("admin","java", Role.OFFICE);
        ReportGenerator rg = new ReportGenerator(user,dbIO);
        
        rg.outputCourseReport("64c125f1", OutputType.CONSOLE);
    }
    
}
