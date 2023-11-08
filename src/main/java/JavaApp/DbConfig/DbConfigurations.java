package JavaApp.DbConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfigurations {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/recruitors";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Philas@12";
    public static Connection connectToDatabase() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            if (connection != null) {
                System.out.println("Connected to the database.");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
