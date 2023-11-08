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

import JavaApp.DbConfig.DbConfigurations;
import JavaApp.RecipientList;
import JavaApp.UserData;
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

    public static List<RecipientList> emails() {
        List<RecipientList> users = new ArrayList<>();
        Connection connection = DbConfigurations.connectToDatabase();
        if (connection != null) {
            try {
                String query = "SELECT email FROM recruitor_emails ";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String email = resultSet.getString("email");
                    RecipientList recipient = new RecipientList(email, "Name","Surname");
                    users.add(recipient);
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
        return users;
    }


    public static void sendApplicationEmail(String recipientEmail, File[] selectedFiles, UserData userData) {
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
            String name = userData.getName();
            String surname = userData.getSurname();
            String occupation = userData.getOccupation();
            String position = userData.getPosition();
            String phone = userData.getPhone();


            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            String emailSubject = "General Inquiry about Job Opportunities";
            message.setSubject(emailSubject);

            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart textPart = new MimeBodyPart();
            String applicationMessage = "Dear Hiring Team,\n\n" +
                    "I am writing to express my strong interest in potential opportunities within your organization.\n" +
                    "As a highly skilled " + occupation + " with a strong background in the field, I believe that my experience\n" +
                    "and expertise make me a valuable candidate for roles within your company.\n\n" +
                    "My dedication, problem-solving skills, and passion for contributing to the success of the company\n" +
                    "align perfectly with your organizational goals. I am excited about the possibility of joining your team \n" +
                    "and contributing to the continued success of your organization.\n\n" +
                    "Please find attached my resume and other relevant documents for your consideration.\n\n" +
                    "Thank you for your time and consideration. I look forward to the opportunity to discuss how I can \n" +
                    "contribute to your organization's success in any suitable role.\n\n" +
                    "Sincerely,\n" + name + " " + surname + "\n" + phone;

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
