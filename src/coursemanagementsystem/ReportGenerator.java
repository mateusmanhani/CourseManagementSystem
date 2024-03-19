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
    public void generateCourseReport(String CourseID){
        if (user.getRole() != Role.OFFICE){
            System.out.println("Unauthorized access for course report by role: " + user.getRole());
        }
        
        
    }
    
    // Generate Student Report
    public void generateStudentReport (String studentID){
        if (user.getRole() != Role.OFFICE){
            System.out.println("Unauthorized access for course report by role: " + user.getRole());
            
        }
    }
    
    
    // Generate Lecturer Report
    public void generateLecturerReport(String lecturerId){
        // Office can generate any report
        if (user.getRole() == Role.OFFICE){
            System.out.println("Generating lecturer report for: " + lecturerId);
            // Implementation to generate and display/return the lecturer report
        }
        // Lecturers can only generate reports for themselves
        else if (user.getRole() == Role.LECTURER && user.getUserID().equals(lecturerId)) {
            System.out.println("Generating lecturer report for self: " + lecturerId);
            // Implementation to generate and display/return the lecturer report for themselves
        } else {
            System.out.println("Unauthorized access attempt for lecturer report by: " + user.getUserID());
        }
        
        
    }
}
