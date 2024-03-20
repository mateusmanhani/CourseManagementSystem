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

    public ReportGenerator(User user, DatabaseIO databaseIO) {
        this.user = user;
        this.databaseIO = databaseIO;
        this.reportOutputter = new ReportOutputter(); // initialise reportOutputter
    }

    //Generate Course Report
    public String generateCourseReport(String courseId) {
        StringBuilder report = new StringBuilder();
        String courseQuery = "SELECT course_name FROM courses WHERE course_id = ?;";
        String courseName = "";

        // Initialize the Connection object outside of the try blocks to ensure it remains open
        Connection conn = null;

        try {
            conn = databaseIO.getConnection();
            // Fetch the course name
            try ( PreparedStatement courseStmt = conn.prepareStatement(courseQuery)) {
                courseStmt.setString(1, courseId);
                try ( ResultSet courseRs = courseStmt.executeQuery()) {
                    if (courseRs.next()) {
                        courseName = courseRs.getString("course_name");
                    } else {
                        return "Course not found.";
                    }
                }
            }

            // Generate report for modules
            report.append(String.format("Course Report for: %s (%s)%n", courseName, courseId));
            report.append("Modules:\n");
            String moduleQuery = "SELECT m.module_name, m.num_students, l.lecturer_name, m.room "
                    + "FROM modules m "
                    + "JOIN lecturers l ON m.lecturer_id = l.lecturer_id "
                    + "WHERE m.course_id = ?;";

            try ( PreparedStatement moduleStmt = conn.prepareStatement(moduleQuery)) {
                moduleStmt.setString(1, courseId);
                try ( ResultSet moduleRs = moduleStmt.executeQuery()) {
                    while (moduleRs.next()) {
                        String moduleName = moduleRs.getString("module_name");
                        int numStudents = moduleRs.getInt("num_students");
                        String lecturerName = moduleRs.getString("lecturer_name");
                        String room = moduleRs.getString("room");
                        report.append(String.format("    Module: %s, Students: %d, Lecturer: %s, Room: %s%n", moduleName, numStudents, lecturerName, room));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "An error occurred while generating the report.";
        } finally {
            // Close the connection if it was opened
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return report.toString();
    }

    // Output Rourse Report
    public void outputCourseReport(String courseId, OutputType outputType) {
        String reportContent = generateCourseReport(courseId);
        if (reportOutputter == null) {
            System.out.println("ReportOutputter is not initialized.");
        } else {
            reportOutputter.outputReport(reportContent, outputType);
        }
    }

    // Generate Student Report
    public void generateStudentReport(String studentID) {
        if (user.getRole() != Role.OFFICE) {
            System.out.println("Unauthorized access for course report by role: " + user.getRole());
        }

        StringBuilder report = new StringBuilder();
        String query = "";

    }

    // Generate Lecturer Report
    public void generateLecturerReport(String lecturerId) {
        // Office can generate any report
        if (user.getRole() == Role.OFFICE) {
            System.out.println("Generating lecturer report for: " + lecturerId);
            // Implementation to generate and display/return the lecturer report
        } // Lecturers can only generate reports for themselves
        else if (user.getRole() == Role.LECTURER && user.getUserID().equals(lecturerId)) {
            System.out.println("Generating lecturer report for self: " + lecturerId);
            // Implementation to generate and display/return the lecturer report for themselves
        } else {
            System.out.println("Unauthorized access attempt for lecturer report by: " + user.getUserID());
        }

    }
}
