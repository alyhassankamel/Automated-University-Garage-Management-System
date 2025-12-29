package dao;

import Database.DatabaseConnectionFactory;
import entity.ParkingSession;

import java.sql.*;

/**
 * Data Access Object for ParkingSession entity.
 */
public class ParkingSessionDAO {

    public ParkingSessionDAO() {
    }

    /**
     * Finds the active session for a vehicle (where exitTime is NULL).
     * 
     * @param vehicleId The vehicle ID.
     * @return ParkingSession object if found, null otherwise.
     */
    public ParkingSession findActiveSession(int vehicleId) {
        String query = "SELECT sessionId, vehicleId, spotId, entryTime, exitTime, processedAt " +
                "FROM ParkingSession WHERE vehicleId = ? AND exitTime IS NULL";

        try (Connection conn = DatabaseConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToSession(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding active session: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a new parking session.
     * 
     * @param session The session to create.
     * @return true if successful, false otherwise.
     */
    public boolean createSession(ParkingSession session) {
        String query = "INSERT INTO ParkingSession (vehicleId, spotId, entryTime) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, session.getVehicleId());
            stmt.setInt(2, session.getSpotId());
            stmt.setTimestamp(3, session.getEntryTime());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        session.setSessionId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating session: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Ends a session by setting the exit time.
     * 
     * @param sessionId The session ID.
     * @param exitTime  The time of exit.
     * @return true if successful, false otherwise.
     */
    public boolean endSession(int sessionId, Timestamp exitTime) {
        String query = "UPDATE ParkingSession SET exitTime = ?, processedAt = ? WHERE sessionId = ?";

        try (Connection conn = DatabaseConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setTimestamp(1, exitTime);
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // processedAt = now
            stmt.setInt(3, sessionId);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error ending session: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private ParkingSession mapResultSetToSession(ResultSet rs) throws SQLException {
        return new ParkingSession(
                rs.getInt("sessionId"),
                rs.getInt("vehicleId"),
                rs.getInt("spotId"),
                rs.getTimestamp("entryTime"),
                rs.getTimestamp("exitTime"),
                rs.getTimestamp("processedAt"));
    }
}
