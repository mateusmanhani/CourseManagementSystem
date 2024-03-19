package coursemanagementsystem;

/**
 *
 * @author Mateus Manhani
 */
public class ReportGenerator {
    private User user;
    private DatabaseIO databaseIO;
    
    public ReportGenerator(User user, DatabaseIO databaseIO){
        this.user = user;
        this.databaseIO = databaseIO;
    }
    
    //Generate Course Report
    public void generateCourseReport(){
        if (user.getRole() != Role.OFFICE){
            System.out.println("Unauthorized access for course report by role: " + user.getRole());
            return;
        }
        
        
    }
    
    // Generate Lecturer Report
    public void generateLecturerReport(){
        if (user.getRole() != Role.OFFICE){
            System.out.println("Unauthorized access for lecturer report by role: " + user.getRole());
            return;
        }
        
        
    }
}
