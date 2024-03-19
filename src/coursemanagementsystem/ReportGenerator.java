package coursemanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Mateus Manhani
 */
public class ReportGenerator {
    private User user;
    private DatabaseIO databaseIO;
    private ReportOutputter reportOutputter;
    
    public ReportGenerator(User user, DatabaseIO databaseIO){
        this.user = user;
        this.databaseIO = databaseIO;
    }
    
    //Generate Course Report
    public String generateCourseReport() {
    StringBuilder report = new StringBuilder();
    String query = "SELECT m.module_name, c.course_name, m.num_students, l.lecturer_name, m.room FROM modules m JOIN courses c ON m.course_id = c.course_id JOIN lecturers l ON m.lecturer_id = l.lecturer_id;";

    try (Connection conn = databaseIO.getConnection(); 
         PreparedStatement stmt = conn.prepareStatement(query); 
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String moduleName = rs.getString("module_name");
            String courseName = rs.getString("course_name");
            int numStudents = rs.getInt("num_students");
            String lecturerName = rs.getString("lecturer_name");
            String room = rs.getString("room").isEmpty() ? "Online" : rs.getString("room");

            report.append(String.format("Module: %s, Course: %s, Students: %d, Lecturer: %s, Room: %s%n", moduleName, courseName, numStudents, lecturerName, room));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return report.toString();
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
