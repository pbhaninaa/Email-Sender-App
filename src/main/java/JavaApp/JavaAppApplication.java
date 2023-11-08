package JavaApp;

import JavaApp.DbConfig.DbConfigurations;
import JavaApp.EmailService.EmailService;
import JavaApp.PDFReader.OCRTextExtractor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class JavaAppApplication {
private static Connection connection =DbConfigurations.connectToDatabase();
    public static void main(String[] args) throws SQLException {


        OCRTextExtractor.createTableIfNotExists(connection);
        OCRTextExtractor. extractAndSaveEmails(connection);

        String name = JOptionPane.showInputDialog("Enter your name:");
        String surname = JOptionPane.showInputDialog("Enter your surname:");
        String occupation = JOptionPane.showInputDialog("Enter your occupation:");
        String position = JOptionPane.showInputDialog("Enter your desired position:");
        String phone = JOptionPane.showInputDialog("Enter your contact number:");

        UserData userData = new UserData(name, surname, occupation, position, phone);

        File[] selectedFiles = EmailService.selectFiles();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            if (selectedFiles.length > 0) {
                List<RecipientList> emailList = EmailService.emails();

                for (RecipientList recipient : emailList) {
                    String email = recipient.getEmail();

                    EmailService.sendApplicationEmail(email, selectedFiles, userData);
                    System.out.println("Sent to "+ email );
                }

            } else {
                System.out.println("No files selected. Emails not sent.");
            }
        }, 0, 5, TimeUnit.DAYS);
        SpringApplication.run(JavaAppApplication.class, args);
    }
}
