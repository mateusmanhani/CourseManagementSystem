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
    public String generateStudentReport(String studentId) {
        StringBuilder report = new StringBuilder();
        Connection conn = null;

        try {
            conn = databaseIO.getConnection();

            // Fetch student information
            String studentInfoQuery = "SELECT s.student_name, s.student_surname, c.course_name "
                    + "FROM students s "
                    + "JOIN courses c ON s.course_id = c.course_id "
                    + "WHERE s.student_id = ?;";

            // Fetch enrolled modules
            String enrolledModulesQuery = "SELECT m.module_name "
                    + "FROM modules m "
                    + "JOIN course_enrollments ce ON m.course_id = ce.course_id "
                    + "WHERE ce.student_id = ?;";

            // Fetch completed modules and grades
            String completedModulesQuery = "SELECT m.module_name, g.grade "
                    + "FROM grades g "
                    + "JOIN modules m ON g.module_id = m.module_id "
                    + "WHERE g.student_id = ? AND g.status = 'completed';";

            // Fetch modules to repeat (assuming a grade below 40 is a fail)
            String modulesToRepeatQuery = "SELECT m.module_name "
                    + "FROM grades g "
                    + "JOIN modules m ON g.module_id = m.module_id "
                    + "WHERE g.student_id = ? AND g.grade < 50;";

            // Execute student information query
            try ( PreparedStatement studentStmt = conn.prepareStatement(studentInfoQuery)) {
                studentStmt.setString(1, studentId);
                try ( ResultSet studentRs = studentStmt.executeQuery()) {
                    if (studentRs.next()) {
                        String studentName = studentRs.getString("student_name");
                        String studentSurname = studentRs.getString("student_surname");
                        String courseName = studentRs.getString("course_name");
                        report.append(String.format("Student Report for %s %s (%s)%nCourse: %s%n%n",
                                studentName, studentSurname, studentId, courseName));
                    } else {
                        return "Student not found.";
                    }
                }
            }

            // Execute enrolled modules query
            report.append("Modules Currently Enrolled In:\n");
            try ( PreparedStatement enrolledModulesStmt = conn.prepareStatement(enrolledModulesQuery)) {
                enrolledModulesStmt.setString(1, studentId);
                try ( ResultSet enrolledModulesRs = enrolledModulesStmt.executeQuery()) {
                    while (enrolledModulesRs.next()) {
                        report.append(String.format("    %s%n", enrolledModulesRs.getString("module_name")));
                    }
                }
            }

            // Execute completed modules and grades query
            report.append("\nCompleted Modules and Grades:\n");
            try ( PreparedStatement completedModulesStmt = conn.prepareStatement(completedModulesQuery)) {
                completedModulesStmt.setString(1, studentId);
                try ( ResultSet completedModulesRs = completedModulesStmt.executeQuery()) {
                    while (completedModulesRs.next()) {
                        report.append(String.format("    %s: Grade %s%n",
                                completedModulesRs.getString("module_name"), completedModulesRs.getString("grade")));
                    }
                }
            }

            // Execute modules to repeat query
            report.append("\nModules to Repeat:\n");
            try ( PreparedStatement modulesToRepeatStmt = conn.prepareStatement(modulesToRepeatQuery)) {
                modulesToRepeatStmt.setString(1, studentId);
                try ( ResultSet modulesToRepeatRs = modulesToRepeatStmt.executeQuery()) {
                    while (modulesToRepeatRs.next()) {
                        report.append(String.format("    %s%n", modulesToRepeatRs.getString("module_name")));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "An error occurred while generating the student report.";
        } finally {
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

    // Output Student Report
    public void outputStudentReport(String studentId, OutputType outputType) {
        String reportContent = generateStudentReport(studentId);
        if (reportOutputter == null) {
            System.out.println("ReportOutputter is not initialized.");
        } else {
            reportOutputter.outputReport(reportContent, outputType);
        }
    }

    // Generate Lecturer Report
    public String generateLecturerReport(String lecturerId) {
        
        StringBuilder report = new StringBuilder();
                
        // Office can generate any report
        if (user.getRole() == Role.OFFICE) {
            System.out.println("Generating lecturer report for: " + lecturerId);
            // Implementation to generate and display/return the lecturer report
        } // Lecturers can only generate reports for themselves
        else if (user.getRole() == Role.LECTURER && user.getUserID().equals(lecturerId)) {
            System.out.println("Generating lecturer report for self: " + lecturerId);
            
            // Implementation to generate and display/return the lecturer report for themselves
            Connection conn = null;
            try {
                conn = databaseIO.getConnection();

                // Fetch lecturer information
                String lecturerQuery = "SELECT l.lecturer_name, l.role "
                        + "FROM lecturers l "
                        + "WHERE l.lecturer_id = ?;";

                // Fetch modules taught by the lecturer
                String modulesQuery = "SELECT m.module_name, m.num_students "
                        + "FROM modules m "
                        + "WHERE m.lecturer_id = ?;";

                try ( PreparedStatement lecturerStmt = conn.prepareStatement(lecturerQuery)) {
                    lecturerStmt.setString(1, lecturerId);
                    try ( ResultSet lecturerRs = lecturerStmt.executeQuery()) {
                        if (lecturerRs.next()) {
                            String lecturerName = lecturerRs.getString("lecturer_name");
                            String lecturerRole = lecturerRs.getString("role");
                            report.append(String.format("Lecturer Report for: %s (%s)%nRole: %s%n%n",
                                    lecturerName, lecturerId, lecturerRole));
                        } else {
                            return "Lecturer not found.";
                        }
                    }
                }

                // Append the modules and the number of students
                report.append("Modules Teaching This Semester:\n");
                try ( PreparedStatement modulesStmt = conn.prepareStatement(modulesQuery)) {
                    modulesStmt.setString(1, lecturerId);
                    try ( ResultSet modulesRs = modulesStmt.executeQuery()) {
                        while (modulesRs.next()) {
                            String moduleName = modulesRs.getString("module_name");
                            int numStudents = modulesRs.getInt("num_students");
                            report.append(String.format("    Module: %s, Students: %d%n", moduleName, numStudents));
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return "An error occurred while generating the lecturer report.";
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return "Unauthorized access attempt for lecturer report by: " + user.getUserID();
        }

        return report.toString();

    }
}
