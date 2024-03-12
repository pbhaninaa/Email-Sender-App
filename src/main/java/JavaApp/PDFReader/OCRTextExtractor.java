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
    public static void extractAndSaveEmails(Connection connection,String position) {
        boolean pos = position.toLowerCase().trim().contains("security officer");
        String security = "occusec@occusec.co.za " +
                "mail@khuselani.co.za " +
                "admin@vusapmb.co.za " +
                "tuffguardpmb@telkomsa.net " +
                "tigerforce@webmail.co.za " +
                "thabzosec@gmail.com " +
                "sfiso@sibongilesecurity.co.za " +
                "redalert@redalert.co.za " +
                "info@pmbsecurity.co.za " +
                "midltraincen@telkomsa.net " +
                "mi7@telkomsa.net " +
                "pmbnatal@mugnumshield.co.za " +
                "Ikhwezi@sai.co.za " +
                "admin@ilangasec.co.za " +
                "tigerforce@webmail.co.za " +
                "fssspmb@fidelity.co.za " +
                "alphinesecurity@telkomsa.net " +
                "admin@amlec.co.za " +
                "sancom.sa@gmail.com " +
                "hrrecuitment@fedelity.co.za";
        String recruiters = "Recruitment@workforce.co.za" +
                " coreenm@phakishahldg.co.za" +
                " Kempton@workforce.co.za" +
                " recruitment@macdon.co.za" +
                " grace.sithole@stratogoco.za" +
                " recruit@cre8work.co.za " +
                " Recruitmentjb@scribantelabour.co.za" +
                " Recruitmentjhb@masa.co.za" +
                " Vmapelane@pple.co.za" +
                " recruitgp@singamandla.co.za" +
                " recruiter@hestony.co.za" +
                " santie@labourflow.co.za" +
                " pinnacleoutsource@gmail.com" +
                " drivers@intercape.co.za " +
                " bongiwe@cargocarriers.co.za" +
                " hr@ilangaout.co.za " +
                " recruitment@onelogix.com" +
                " Sweetness@assign.co.za" +
                " Wayne@scribantelabour.co.za" +
                " drivers@assign.co.za" +
                " Midrand@workforce.co.za" +
                " Centurion@workforce.co.za" +
                " lebogangm@dynaniclabour.co.za";

        String[] emails = extractEmailAddresses(pos?security:recruiters);
        saveEmailsToDatabase(emails, connection,position);
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


    public static void createTableIfNotExists(Connection connection, String position) throws SQLException {

        String createTableSQL = position.toLowerCase().trim().contains("security officer")?"CREATE TABLE IF NOT EXISTS security_recruiter_emails (id INT AUTO_INCREMENT PRIMARY KEY, email TEXT)":"CREATE TABLE IF NOT EXISTS recruiter_emails (id INT AUTO_INCREMENT PRIMARY KEY, email TEXT)";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
        System.out.println("Table Created!!");
    }

    public static void saveEmailsToDatabase(String[] emails, Connection connection,String position) {
        boolean pos = position.toLowerCase().trim().contains("security officer");
        // Define the SQL query to check if an email exists in the table
        String checkExistenceQuery = pos?"SELECT email FROM security_recruiter_emails WHERE email = ?":"SELECT email FROM recruiter_emails WHERE email = ?";

        // Define the SQL query to insert email addresses into the table
        String insertQuery = pos?"INSERT INTO security_recruiter_emails (email) VALUES (?)":"INSERT INTO recruiter_emails (email) VALUES (?)";

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
