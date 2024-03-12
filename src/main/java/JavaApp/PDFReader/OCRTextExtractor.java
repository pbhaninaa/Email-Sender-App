package JavaApp.PDFReader;

import java.io.File;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.swing.*;

public class OCRTextExtractor {
    public static void extractAndSaveEmails(Connection connection) {
        String text = "pbhanina@gmail.com dikinyekatulisa@gmail.com";

        String[] emails = extractEmailAddresses(text);
        saveEmailsToDatabase(emails, connection);

    }

    public static String[] extractEmailAddresses(String text) {
        // Define a regular expression to match email addresses
        String emailRegex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,7}\\b";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(text);

        // Create a StringBuilder to store the extracted email addresses
        StringBuilder emailAddresses = new StringBuilder();

        while (matcher.find()) {
            emailAddresses.append(matcher.group()).append(" ");
        }

        // Split the concatenated email addresses into an array
        String[] emailArray = emailAddresses.toString().split(" ");

        return emailArray;
    }


    public static void createTableIfNotExists(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS recruitor_emails (id INT AUTO_INCREMENT PRIMARY KEY, email TEXT)";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
        System.out.println("Table Created!!");
    }

    public static void saveEmailsToDatabase(String[] emails, Connection connection) {
        // Define the SQL query to check if an email exists in the table
        String checkExistenceQuery = "SELECT email FROM recruitor_emails WHERE email = ?";

        // Define the SQL query to insert email addresses into the table
        String insertQuery = "INSERT INTO recruitor_emails (email) VALUES (?)";

        try (PreparedStatement checkExistenceStatement = connection.prepareStatement(checkExistenceQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {

            Set<String> uniqueEmails = new HashSet<>();

            for (String email : emails) {
                // Check if the email already exists in the table
                checkExistenceStatement.setString(1, email);
                ResultSet resultSet = checkExistenceStatement.executeQuery();

                if (!resultSet.next()) {
                    // Email does not exist, save it
                    uniqueEmails.add(email);
                }
            }

            for (String uniqueEmail : uniqueEmails) {
                // Insert the unique email addresses into the table
                insertStatement.setString(1, uniqueEmail);
                insertStatement.executeUpdate();
            }

            System.out.println("Emails saved to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
