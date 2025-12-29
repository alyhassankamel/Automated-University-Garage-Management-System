package dao;

import Database.DatabaseConnectionFactory;
import entity.EntryExitLog;

import java.sql.*;

/**
 * Data Access Object for EntryExitLog entity.
 */
public class EntryExitLogDAO {

    public EntryExitLogDAO() {
    }

    /**
     * Logs an entry or exit event.
     * 
     * @param log The EntryExitLog object to save.
     * @return true if successful, false otherwise.
     */
    public boolean logEvent(EntryExitLog log) {
        String query = "INSERT INTO EntryExitLog (licensePlate, direction, garageId, spotId, eventTime) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, log.getLicensePlate());
            stmt.setString(2, log.getDirection());
            stmt.setInt(3, log.getGarageId());

            if (log.getSpotId() != null) {
                stmt.setInt(4, log.getSpotId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }

            stmt.setTimestamp(5, log.getEventTime());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error logging event: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
