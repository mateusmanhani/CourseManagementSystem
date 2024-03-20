package coursemanagementsystem;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author Mateus Manhani
 */
public class ReportOutputter {

    public void outputReport(String reportContent, OutputType outputType) {
        switch (outputType) {
            case CONSOLE:
                outputToConsole(reportContent);
                break;
            case TEXT_FILE:
                outputToTextFile(reportContent, "report.txt");
                break;
            case CSV_FILE:
                outputToCsvFile(reportContent, "report.csv");
                break;
            default:
                System.out.println("Unsupported report output type.");
                break;
        }
    }

    private void outputToConsole(String reportContent) {
        System.out.println(reportContent);
    }

    private void outputToTextFile(String reportContent, String fileName) {
        try ( PrintWriter out = new PrintWriter(fileName)) {
            out.println(reportContent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void outputToCsvFile(String reportContent, String fileName) {
        // CSV-specific formatting can be applied here if needed
        try ( PrintWriter out = new PrintWriter(fileName)) {
            out.println(reportContent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
