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
  static String name;
static String surname;
static String occupation;
  static String position;
 static String phone;
public static void fields(){
    name = JOptionPane.showInputDialog("Enter your name:");
    surname = JOptionPane.showInputDialog("Enter your surname:");
    occupation = JOptionPane.showInputDialog("Enter your occupation:");
    position = JOptionPane.showInputDialog("Enter your desired position:");
    phone = JOptionPane.showInputDialog("Enter your contact number:");
}

    public static void main(String[] args) throws SQLException {
        fields();
        OCRTextExtractor.createTableIfNotExists(connection);
        OCRTextExtractor. extractAndSaveEmails(connection);
        UserData userData = new UserData(name, surname, occupation, position, phone);
        File[] selectedFiles = EmailService.selectFiles();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            if (name == null || name.trim().isEmpty() ||
                    surname == null || surname.trim().isEmpty() ||
                    occupation == null || occupation.trim().isEmpty() ||
                    position == null || position.trim().isEmpty() ||
                    phone == null || phone.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields. Application failed", "Error", JOptionPane.ERROR_MESSAGE);
                fields();
            } else {
                if (phone.matches("\\d{10}")){
                    if (selectedFiles.length > 0) {
                        List<RecipientList> emailList = EmailService.emails();

//                for (RecipientList recipient : emailList) {
//                    String email = recipient.getEmail();
                        String email = "thembatwane@gmail.com";
                        EmailService.sendApplicationEmail(email, selectedFiles, userData);
                        System.out.println("Sent to "+ email );
//                }

                    } else {
                        JOptionPane.showMessageDialog(null,"No files selected. Application failed.","Error",JOptionPane.ERROR_MESSAGE);
                    }
                    fields();
                }
                JOptionPane.showMessageDialog(null,"Phone Number must be in Numerics and should be 10 numbers!. Application failed.","Error",JOptionPane.ERROR_MESSAGE);
                fields();
            }

        }, 0, 5, TimeUnit.DAYS);
        SpringApplication.run(JavaAppApplication.class, args);
    }
}
