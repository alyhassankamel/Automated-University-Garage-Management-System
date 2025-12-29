package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionFactory {

    private static final String USER = "sa";
    private static final String PASSWORD = "123";

    // Allow trusting the server certificate for local/dev environments.
    private static final String INSTANCE_URL = "jdbc:sqlserver://localhost;instanceName=SQLEXPRESS;databaseName=AUGMS_UNI_PARKING;encrypt=true;trustServerCertificate=true";
    private static final String PORT_URL = "jdbc:sqlserver://localhost:1433;databaseName=AUGMS_UNI_PARKING;encrypt=true;trustServerCertificate=true";

    /**
     * Attempts to obtain a connection using a named-instance URL first,
     * then falls back to a host:port URL if that fails.
     */
    public static Connection getConnection() throws SQLException {
        SQLException firstEx = null;
        try {
            // Ensure JDBC driver is loaded (helps with some runtime classloader setups)
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } catch (ClassNotFoundException cnf) {
                // Driver not on classpath yet; DriverManager may still find it if present.
                System.err.println("JDBC driver class not found on classpath: " + cnf.getMessage());
            }

            // Allow environment overrides for easy local testing
            String instanceUrl = System.getenv().getOrDefault("AUP_DB_URL", INSTANCE_URL);
            String user = System.getenv().getOrDefault("AUP_DB_USER", USER);
            String password = System.getenv().getOrDefault("AUP_DB_PASSWORD", PASSWORD);

            return DriverManager.getConnection(instanceUrl, user, password);
        } catch (SQLException e) {
            firstEx = e;
            System.err.println("Instance URL failed: " + e.getMessage());
        }

        try {
            String portUrl = System.getenv().getOrDefault("AUP_DB_PORT_URL", PORT_URL);
            String user = System.getenv().getOrDefault("AUP_DB_USER", USER);
            String password = System.getenv().getOrDefault("AUP_DB_PASSWORD", PASSWORD);
            return DriverManager.getConnection(portUrl, user, password);
        } catch (SQLException e) {
            System.err.println("Port URL failed: " + e.getMessage());
            if (firstEx != null) {
                e.addSuppressed(firstEx);
            }
            throw e;
        }
    }
}