package coursemanagementsystem;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mateus Manhani
 */
public class MenuSystem {
    private AuthService authService;
    private UserService userService;
    private ReportGenerator reportGenerator;

    public MenuSystem(AuthService authService, UserService userService, ReportGenerator reportGenerator) {
        this.authService = authService;
        this.userService = userService;
        this.reportGenerator = reportGenerator;
    }
    
    public void showLoginScreen(){
        boolean validInput = false;
        while(!validInput){
            try {
                Scanner sc = new Scanner(System.in);
                System.out.println("Welcome to the Course Management System (CMS)");
                System.out.println("If you would like to exit type 'exit'.");

                System.out.println("Username: ");
                String username = sc.nextLine();
                
                if (username.equalsIgnoreCase("exit"))
                    break;

                System.out.println("Password: ");
                String password = sc.nextLine();

                User user = authService.authenticateUser(username, password);

                if(user != null){
                    System.out.println("Login Successful.");
                    showUserMenu(user, sc);
                }else{
                    System.out.println("Login failed. Please check your username and password.");
                    // do a loop to keep going
                }
            } catch (SQLException ex) {
                Logger.getLogger(MenuSystem.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
    public void showUserMenu(User user, Scanner sc){
        Role userRole = user.getRole();
        
        switch(userRole){
            case OFFICE:
                // SHOW OFFICE MENU
                officeMenu(user,sc);
            case LECTURER:
                //SHOW LECTURER MENU
                lecturerMenu(user,sc);
            case ADMIN:
                //SHOW ADMIN MENU
                adminMenu(user, sc);
        }
        
    }
    
    public void adminMenu(User user, Scanner sc){
//        UserService userService = new UserService(databaseIO);
        boolean validInput = false;
        while (!validInput){
            try{
                System.out.println("Welcome to the ADMIN menu.");
                System.out.println("1. Add User.");
                System.out.println("2. Update User.");
                System.out.println("3. Delete User.");
                System.out.println("4. Change my username");
                System.out.println("5. Change my password");
                System.out.println("6. Change my role");
                System.out.println("7. Logout");
                System.out.println("Enter the number of your choice: ");
                
                int choice = sc.nextInt();
                
                switch(choice){
                    case 1:{
                        //AddUser
                        sc.nextLine();
                        System.out.println("Enter username: ");
                        String username = sc.nextLine();
                        
                        System.out.println("Enter password: ");
                        String password = sc.nextLine();
                        
                        System.out.println("Enter role (ADMIN, OFFICE, LECTURER): ");
                        String roleStr = sc.nextLine();
                        Role role = Role.valueOf(roleStr.toUpperCase());
                        
                        String lecturerId = null;
                        if(role == Role.LECTURER){
                            System.out.println("Enter lecturer ID: ");
                            lecturerId = sc.nextLine();
                        }
                        
                        String salt = Hasher.generateSalt();
                        String userId = userService.generateUniqueUserID();
                        User newUser = new User(userId, username, role, lecturerId, salt); // instanciate new User
                        boolean userAdded = userService.addUSer(newUser, password, salt);
                        
                        if(userAdded){
                            System.out.println("User added successfully.");
                        }else{
                            System.out.println("Failed to add User.");
                        }
                        break;
                    }
                    case 2:{
                        //update user
                        sc.nextLine(); // Consume newline
                        System.out.println("Enter the user ID of the user you wish to update: ");
                        String updateUserId = sc.nextLine();

                        // Fetch user to ensure they exist 
                        User userToUpdate = userService.fetchUserById(updateUserId);
                        if (userToUpdate == null) {
                            System.out.println("User not found.");
                            break;
                        }

                        System.out.println("Enter new username (press Enter to keep current): ");
                        String newUsername = sc.nextLine();
                        if (!newUsername.isEmpty()) userToUpdate.setUsername(newUsername);

                        System.out.println("Enter new role (ADMIN, OFFICE, LECTURER) or press Enter to keep current: ");
                        String newRoleStr = sc.nextLine();
                        if (!newRoleStr.isEmpty()) userToUpdate.setRole(Role.valueOf(newRoleStr.toUpperCase()));

                        Optional<String> newPassword = Optional.empty();
                        System.out.println("Do you want to change the password? (yes/no): ");
                        String changePassword = sc.nextLine();
                        if ("yes".equalsIgnoreCase(changePassword)) {
                            System.out.println("Enter new password:");
                            newPassword = Optional.of(sc.nextLine());
                        }

                        // Call update User method 
                        boolean updated = userService.updateUser(userToUpdate, newPassword);
                        if (updated) {
                            System.out.println("User updated successfully.");
                        } else {
                            System.out.println("Failed to update user.");
                        }
                        break;
                    }
                    case 3:
                        //deleteUser
                        sc.nextLine();
                        System.out.println("Enter the user ID of the user you wish to delete: ");
                        String deleteUserId = sc.next();
                        
                        if(deleteUserId != null){
                            boolean deleted = userService.deleteUser(deleteUserId);
                            if(deleted){
                                System.out.println("User deleteded successfully.");
                            }else{
                                System.out.println("Error deleting user.Please try again.");
                            }
                        }
                        break;
                    case 4:{
                        // changeMyUsername
                        sc.nextLine();
                        System.out.println("Enter your new username: ");
                        String newUsername = sc.next();
                        
                        //call changeMyUsername method passing the new username
                        boolean usernameChanged = userService.changeMyUsername(user.getUserID(), newUsername);
                        if(usernameChanged){
                            System.out.println("Username Changed successfully.");
                        }else{
                            System.out.println("Failed to change username. please try again.");
                        }
                        break;
                    }
                    case 5:{
                        // changeMyPassword
                        sc.nextLine();
                        System.out.println("Enter your new password");
                        String newPassword = sc.next();
                        // Hash the password
                        String salt = Hasher.generateSalt();
                        String hashedPassword = Hasher.hashPassword(newPassword, salt, 1000);
                        // Call the changeMyPassword method passing the hashedPassword and salt
                        boolean passwordChanged = userService.changeMyPassword(user.getUserID(),hashedPassword,salt);
                        if(passwordChanged){
                            System.out.println("Password changed successfully.");
                        }else{
                            System.out.println("Error updating password. Please try again.");
                        }
                        break;
                    }
                    case 6:
                        // changeMyRole
                        sc.nextLine();
                        System.out.println("Select the role you want to change to:");
                        System.out.println("1. Office");
                        System.out.println("2. Lecturer");
                        System.out.println("3. Admin");
                        int roleChoice = sc.nextInt();
                        Role newRole = null;
                        switch(roleChoice){
                            case 1:
                                newRole = Role.OFFICE;
                                break;
                            case 2:
                                newRole = Role.LECTURER;
                                break;
                            case 3:
                                newRole = Role.ADMIN;
                                break;
                            default:
                                System.out.println("Invalid role choice.");
                                break;
                        }
                        if(newRole != null){
                            boolean roleChanged = userService.changeMyRole(user.getUserID(), newRole);
                            if (roleChanged){
                                System.out.println("Role changed successfully.");
                            }else{
                                System.out.println("Failed to change role. Please try again.");
                            }
                        }
                        break;
                    case 7:
                        validInput = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                    
                }
                
                
                
            }catch(Exception e){
                
            }
        }
    }
    
    public void lecturerMenu(User user, Scanner sc){
        boolean validInput = false;
        while (!validInput){
            try{
                System.out.println("Welcome to the LECTURER menu.");
                
                System.out.println("1. Generate my report.");
                System.out.println("2. Change my username.");
                System.out.println("3. Change my password.");
                System.out.println("4. Logout.");
                System.out.println("Enter the number of your choice: ");
                
                int choice = sc.nextInt();
                
                switch(choice){
                    case 1:
                        sc.nextLine();
                        // generate self report
                        System.out.println("Please select an output type:");
                        System.out.println("1. CSV");
                        System.out.println("2. TEXT");
                        System.out.println("3. CONSOLE.");
                        System.out.println("Enter the number of your choice: ");
                        int outputChoice = sc.nextInt();
                        OutputType outputType = getOutputTypeFromChoice(outputChoice);
                        if (outputType != null){
                            reportGenerator.outputLecturerReport(user, user.getLecturerId(),outputType);
                        }else{
                            System.out.println("Invalid output option, please try again.");
                        }
                        break;
                    case 2:
                        sc.nextLine();
                        //change my username
                        System.out.println("Enter your new username: ");
                        String newUsername = sc.next();
                        
                        //call changeMyUsername method passing the new username
                        boolean usernameChanged = userService.changeMyUsername(user.getUserID(), newUsername);
                        if(usernameChanged){
                            System.out.println("Username Changed successfully.");
                        }else{
                            System.out.println("Failed to change username. please try again.");
                        }
                        break;
                        
                    case 3:
                        sc.nextLine();
                        //change my password
                        System.out.println("Enter your new password");
                        String newPassword = sc.next();
                        // Hash the password
                        String salt = Hasher.generateSalt();
                        String hashedPassword = Hasher.hashPassword(newPassword, salt, 1000);
                        // Call the changeMyPassword method passing the hashedPassword and salt
                        boolean passwordChanged = userService.changeMyPassword(user.getUserID(),hashedPassword,salt);
                        if(passwordChanged){
                            System.out.println("Password changed successfully.");
                        }else{
                            System.out.println("Error updating password. Please try again.");
                        }
                        break;
                    case 4:
                        validInput = true;
                        break;
                    default:
                        System.out.println("Invalid option please try again.");
                }
            }catch(Exception e){
                sc.nextLine(); // Clear scanner buffer
                System.out.println("An error occured please try again.");
            }
        }

    }
    
    public void officeMenu(User user, Scanner sc){
        boolean validInput = false;
        while (!validInput){
            try{
                System.out.println("Welcome to the OFFICE menu.");
                System.out.println("1. Generate student report.");
                System.out.println("2. Generate lecturer report");
                System.out.println("3. Generate course report");
                System.out.println("4. Change my username");
                System.out.println("5. Change my password");
                System.out.println("6. Logout");
                System.out.println("Enter the number of your choice: ");
                
                int choice = sc.nextInt();
                
                switch(choice){
                    case 1:{
                        // generate Student Report
                        System.out.println("Enter the student ID for the student report: ");
                        String studentId = sc.next();
                        
                        sc.nextLine();
                        System.out.println("Please select an output type:");
                        System.out.println("1. CSV");
                        System.out.println("2. TEXT");
                        System.out.println("3. CONSOLE.");
                        System.out.println("Enter the number of your choice: ");
                        int outputChoice = sc.nextInt();
                        OutputType outputType = getOutputTypeFromChoice(outputChoice);
                        if (outputType != null){
                            reportGenerator.outputStudentReport(studentId,outputType);
                        }else{
                            System.out.println("Invalid output option, please try again.");
                        }
                        break;
                    }
                    case 2:{
                        // Generate Lecturer report
                        System.out.println("Enter the lecturer ID for the lecturer report: ");
                        String lecturerId = sc.next();
                        
                        sc.nextLine();
                        System.out.println("Please select an output type:");
                        System.out.println("1. CSV");
                        System.out.println("2. TEXT");
                        System.out.println("3. CONSOLE.");
                        System.out.println("Enter the number of your choice: ");
                        int outputChoice = sc.nextInt();
                        OutputType outputType = getOutputTypeFromChoice(outputChoice);
                        if (outputType != null){
                            reportGenerator.outputLecturerReport(user,lecturerId,outputType);
                        }else{
                            System.out.println("Invalid output option, please try again.");
                        }
                        break;
                    }
                    case 3:{
                        //Generate course Report
                        System.out.println("Enter the course ID for the course report: ");
                        String courseId = sc.next();
                        
                        sc.nextLine();
                        System.out.println("Please select an output type:");
                        System.out.println("1. CSV");
                        System.out.println("2. TEXT");
                        System.out.println("3. CONSOLE.");
                        System.out.println("Enter the number of your choice: ");
                        int outputChoice = sc.nextInt();
                        OutputType outputType = getOutputTypeFromChoice(outputChoice);
                        if (outputType != null){
                            reportGenerator.outputCourseReport(courseId,outputType);
                        }else{
                            System.out.println("Invalid output option, please try again.");
                        }
                        break;
                    }
                    case 4:
                        //change my username
                        sc.nextLine();
                        System.out.println("Enter your new username: ");
                        String newUsername = sc.next();
                        
                        //call changeMyUsername method passing the new username
                        boolean usernameChanged = userService.changeMyUsername(user.getUserID(), newUsername);
                        if(usernameChanged){
                            System.out.println("Username Changed successfully.");
                        }else{
                            System.out.println("Failed to change username. please try again.");
                        }
                        break;
                        
                    case 5:
                        //change my password
                        sc.nextLine();
                        System.out.println("Enter your new password");
                        String newPassword = sc.next();
                        // Hash the password
                        String salt = Hasher.generateSalt();
                        String hashedPassword = Hasher.hashPassword(newPassword, salt, 1000);
                        // Call the changeMyPassword method passing the hashedPassword and salt
                        boolean passwordChanged = userService.changeMyPassword(user.getUserID(),hashedPassword,salt);
                        if(passwordChanged){
                            System.out.println("Password changed successfully.");
                        }else{
                            System.out.println("Error updating password. Please try again.");
                        }
                        break;
                    
                    case 6:
                        //logout
                        validInput = true;
                        break;
                }
            }catch(Exception e){
                
            }
        }
    }
    
    
    // Method to return enum output type for the output of the reports
    private OutputType getOutputTypeFromChoice(int choice) {
    switch (choice) {
        case 1:
            return OutputType.CSV_FILE;
        case 2:
            return OutputType.TEXT_FILE;
        case 3:
            return OutputType.CONSOLE;
        default:
            return null; // Invalid choice
    }
}
}
