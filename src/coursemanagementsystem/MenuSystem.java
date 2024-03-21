package coursemanagementsystem;

import java.sql.SQLException;
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

                System.out.println("Username: ");
                String username = sc.nextLine();

                System.out.println("Password: ");
                String password = sc.nextLine();

                User user = authService.authenticateUser(username, password);

                if(user != null){
                    System.out.println("Login Successful.");
                    // continue with operations...
                }else{
                    System.out.println("Login failed. Please check your username and password.");
                    // do a loop to keep going
                }
            } catch (SQLException ex) {
                Logger.getLogger(MenuSystem.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
    public void showUserMenu(User user){
        Role userRole = user.getRole();
        
        switch(userRole){
            case OFFICE:
                // SHOW OFFICE MENU
            case LECTURER:
                //SHOW LECTURER MENU
            case ADMIN:
                //SHOW ADMIN MENU
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
                
            }catch(Exception e){
                
            }
        }
    }
    
    public void lecturerMenu(User user, Scanner sc){
        boolean validInput = false;
        while (!validInput){
            try{
                System.out.println("Welcome to the ADMIN menu.");
                
                System.out.println("1. Generate my report.");
                System.out.println("2. Change my username.");
                System.out.println("3. Change my password.");
                System.out.println("4. Logout.");
                System.out.println("Enter the number of your choice: ");
                
                int choice = sc.nextInt();
                
                switch(choice){
                    case 1:
                        // generate self report
                        System.out.println("Please select an output type:");
                        System.out.println("1. CSV");
                        System.out.println("2. TEXT");
                        System.out.println("3. CONSOLE.");
                        System.out.println("Enter the number of your choice: ");
                        int outputChoice = sc.nextInt();
                        OutputType outputType = getOutputTypeFromChoice(outputChoice);
                        if (outputType != null){
                            reportGenerator.outputLecturerReport(user.getLecturerId(),outputType);
                        }else{
                            System.out.println("Invalid output option, please try again.");
                        }
                        break;
                    case 2:
                        //change my username
                        
                    case 3:
                        //change my password
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
