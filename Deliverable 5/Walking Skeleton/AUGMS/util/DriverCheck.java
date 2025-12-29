import java.sql.Driver;
import java.sql.DriverManager;

public class DriverCheck {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=AUGMS_UNI_PARKING;encrypt=true;trustServerCertificate=true";
        try {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                System.out.println("Loaded driver class successfully.");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver class not found: " + e.getMessage());
            }
            System.out.println("Registered drivers via DriverManager:");
            java.util.Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver d = drivers.nextElement();
                System.out.println(" - " + d.getClass().getName());
            }
            try {
                Driver drv = DriverManager.getDriver(url);
                System.out.println("DriverManager.getDriver succeeded: " + drv.getClass().getName());
            } catch (Exception ex) {
                System.out.println("DriverManager.getDriver failed: " + ex.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
