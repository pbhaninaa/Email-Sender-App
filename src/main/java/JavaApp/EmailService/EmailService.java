package JavaApp.EmailService;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DbConfig.DbConfigurations;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailService {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static List<String> emails() {
        List<String> emailList = new ArrayList<>();
        Connection connection = DbConfigurations.connectToDatabase();
        if (connection != null) {
            try {
                String query = "SELECT email FROM users";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String email = resultSet.getString("email");
                    emailList.add(email);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return emailList;
    }
    public static void sendApplicationEmail(String recipientEmail, File[] selectedFiles) {
        String senderEmail = "pbhanina@gmail.com";
        String senderPassword = "thzw pays edyz pomo";
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        try {
            String name = JOptionPane.showInputDialog("Enter your name:");
            String surname = JOptionPane.showInputDialog("Enter your surname:");
            String occupation = JOptionPane.showInputDialog("Enter your occupation:");
            String position = JOptionPane.showInputDialog("Enter your desired position:");
            String phone = JOptionPane.showInputDialog("Enter your contact number:");


            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            String emailSubject = "Application for " + position + " position. ";
            message.setSubject(emailSubject);


            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart textPart = new MimeBodyPart();

            String applicationMessage = "Dear Hiring Manager,\n\n" + "I am writing to express my strong interest in the " + position + " position at your company.\nAs a highly skilled " + occupation + " with a strong background in the field, I believe that my experience and expertise\nmake me a perfect fit " + "for this role.\n\n" + "My dedication, problem-solving skills, and passion for contributing to the success of the company " + "align perfectly with the job requirements. \nI am excited about the opportunity to join your team and contribute " + "to the continued success of your organization.\n\n" + "Please find attached my resume and other relevant documents for your review.\n\n" + "Thank you for considering my application. I look forward to the opportunity for an interview to discuss " + "how I can contribute to your company's success.\n\n" + "Sincerely,\n" + name + " " + surname + "\n" + phone;
            textPart.setText(applicationMessage);
            multipart.addBodyPart(textPart);
            for (File file : selectedFiles) {
                MimeBodyPart attachment = new MimeBodyPart();
                attachment.attachFile(file);
                attachment.setFileName(file.getName());
                multipart.addBodyPart(attachment);
            }
            message.setContent(multipart);
            Transport.send(message);
        } catch (AuthenticationFailedException e) {
            JOptionPane.showMessageDialog(null, "Authentication failed. Please check your email and password.");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    public static File[] selectFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFiles();
        } else {
            return new File[0];
        }
    }
}
