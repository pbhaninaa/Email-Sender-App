package JavaApp;

import JavaApp.DbConfig.DbConfigurations;
import JavaApp.EmailService.EmailService;
import JavaApp.PDFReader.OCRTextExtractor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class JavaAppApplication {
private static final Connection connection =DbConfigurations.connectToDatabase();
static String name;
static String surname;
static String occupation;
static String position;
static String phone;
static String email;
    public static void fields() {
        JTextField nameField = new JTextField();
        JTextField surnameField = new JTextField();
        JTextField occupationField = new JTextField();
        JTextField positionField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Surname:"));
        panel.add(surnameField);
        panel.add(new JLabel("Occupation:"));
        panel.add(occupationField);
        panel.add(new JLabel("Desired Position:"));
        panel.add(positionField);
        panel.add(new JLabel("Contact Number:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email Address:"));
        panel.add(emailField);

        while (true) {
            int result = JOptionPane.showConfirmDialog(
                    null,
                    panel,
                    "Enter Your Details",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            String nameTxt = nameField.getText().trim();
            String surnameTxt = surnameField.getText().trim();
            String occupationTxt = occupationField.getText().trim();
            String positionTxt = positionField.getText().trim();
            String phoneTxt = phoneField.getText().trim();
            String emailTxt = emailField.getText().trim();

            if (nameTxt.isEmpty() || surnameTxt.isEmpty() || occupationTxt.isEmpty()
                    || positionTxt.isEmpty() || phoneTxt.isEmpty() || emailTxt.isEmpty()) {

                showError("All fields are required.");
                continue;
            }

            if (!nameTxt.matches("[a-zA-Z ]+")) {
                showError("Name must contain letters only.");
                nameField.requestFocus();
                continue;
            }

            if (!surnameTxt.matches("[a-zA-Z ]+")) {
                showError("Surname must contain letters only.");
                surnameField.requestFocus();
                continue;
            }

            if (!phoneTxt.matches("\\d{10,15}")) {
                showError("Contact number must be numeric (10–15 digits).");
                phoneField.requestFocus();
                continue;
            }

            if (!emailTxt.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                showError("Please enter a valid email address.");
                emailField.requestFocus();
                continue;
            }

            name = nameTxt;
            surname = surnameTxt;
            occupation = occupationTxt;
            position = positionTxt;
            phone = phoneTxt;
            email = emailTxt;
            break;
        }
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
        );
    }




    public static void main(String[] args) throws SQLException {
        fields();
        OCRTextExtractor.createTableIfNotExists(connection, position);
        OCRTextExtractor. extractAndSaveEmails(connection,position);
        UserData userData = new UserData(name, surname, occupation, position, phone, email);
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

                            List<RecipientList> emailList = EmailService.emails(position);
                            for (RecipientList recipient : emailList) {
                                String email = recipient.getEmail();
                                        EmailService.emails(position);
                                        EmailService.sendApplicationEmail(email, selectedFiles, userData);
                                        System.out.println("Sent to "+ email );

                            }
                            JOptionPane.showMessageDialog(null, "Done. Thank you!!", "Info", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null,"No files selected. Application failed.","Error",JOptionPane.ERROR_MESSAGE);
                            fields();
                        }

            }

        }, 0, 5, TimeUnit.DAYS);
        SpringApplication.run(JavaAppApplication.class, args);
    }
}
