
import dao.ParkingSessionDAO;
import dao.OccupancySensorDAO;
import Database.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ActiveSessionCleaner {
    public static void main(String[] args) {
        System.out.println("Cleaning active sessions...");
        String query = "UPDATE ParkingSession SET exitTime = CURRENT_TIMESTAMP WHERE exitTime IS NULL";
        String sensorQuery = "UPDATE OccupancySensor SET isCurrentlyOccupied = 0";

        try (Connection conn = DatabaseConnectionFactory.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                int rows = stmt.executeUpdate();
                System.out.println("Closed " + rows + " active sessions.");
            }
            try (PreparedStatement stmt = conn.prepareStatement(sensorQuery)) {
                int rows = stmt.executeUpdate();
                System.out.println("Reset " + rows + " sensors to empty.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
