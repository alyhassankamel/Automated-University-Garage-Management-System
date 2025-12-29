package Database;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionTest {

    public static void main(String[] args) {
        System.out.println("Testing database connectivity...");
        try (Connection conn = DatabaseConnectionFactory.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Connection successful: " + conn.getMetaData().getURL());
            } else {
                System.err.println("Connection returned null or closed connection.");
                System.exit(2);
            }
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Done.");
    }
}
