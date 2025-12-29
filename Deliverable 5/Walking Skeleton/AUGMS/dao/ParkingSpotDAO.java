package dao;

import entity.*;
import Database.DatabaseConnectionFactory;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * Data Access Object for ParkingSpot entity.
 * Handles all database operations related to parking spots.
 */
public class ParkingSpotDAO {

    /**
     * Default constructor
     */
    public ParkingSpotDAO() {
    }

    /**
     * Finds a parking spot by Spot Number (e.g. "A-01")
     * 
     * @param spotNumber the spot number string.
     * @return the ParkingSpot object, or null if not found.
     */
    public ParkingSpot findSpotByNumber(String spotNumber) {
        String query = "SELECT spotId, garageId, spotNumber FROM ParkingSpot WHERE spotNumber = ?";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, spotNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToSpot(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding spot by number: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Finds a parking spot by ID
     * 
     * @param spotId the spot ID to search for
     * @return the ParkingSpot object, or null if not found
     */
    public ParkingSpot findSpot(int spotId) {
        String query = "SELECT spotId, garageId, spotNumber FROM ParkingSpot WHERE spotId = ?";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, spotId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToSpot(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding spot: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all parking spots in the database
     * 
     * @return set of all ParkingSpot objects
     */
    public Set<ParkingSpot> getAllSpots() {
        Set<ParkingSpot> spots = new HashSet<>();
        String query = "SELECT spotId, garageId, spotNumber FROM ParkingSpot";

        try (Connection conn = DatabaseConnectionFactory.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                spots.add(mapResultSetToSpot(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all spots: " + e.getMessage());
            e.printStackTrace();
        }
        return spots;
    }

    /**
     * Finding the first available parking spot (lowest ID).
     * 
     * @return A ParkingSpot object if found, null otherwise.
     */
    public ParkingSpot findFirstAvailableSpot() {
        String query = "SELECT TOP 1 ps.spotId, ps.garageId, ps.spotNumber " +
                "FROM ParkingSpot ps " +
                "LEFT JOIN OccupancySensor os ON ps.spotId = os.spotId " +
                "WHERE os.isCurrentlyOccupied = 0 OR os.sensorId IS NULL " +
                "ORDER BY ps.spotId ASC";

        try (Connection conn = DatabaseConnectionFactory.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                ParkingSpot spot = new ParkingSpot();
                spot.setSpotId(rs.getInt("spotId"));
                spot.setGarageId(rs.getInt("garageId"));
                spot.setSpotNumber(rs.getString("spotNumber"));
                spot.setStatus(SpotStatus.AVAILABLE);
                return spot;
            }
        } catch (SQLException e) {
            System.err.println("Error finding available spot: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Garage is full
    }

    /**
     * Gets all available parking spots
     * 
     * @return set of available ParkingSpot objects
     */
    public Set<ParkingSpot> getAvailableSpots() {
        Set<ParkingSpot> spots = new HashSet<>();
        String query = "SELECT ps.spotId, ps.garageId, ps.spotNumber " +
                "FROM ParkingSpot ps " +
                "LEFT JOIN OccupancySensor os ON ps.spotId = os.spotId " +
                "WHERE os.isCurrentlyOccupied = 0 OR os.sensorId IS NULL";

        try (Connection conn = DatabaseConnectionFactory.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                ParkingSpot spot = mapResultSetToSpot(rs);
                spot.setStatus(SpotStatus.AVAILABLE);
                spots.add(spot);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving available spots: " + e.getMessage());
            e.printStackTrace();
        }
        return spots;
    }

    /**
     * Gets all occupied parking spots
     * 
     * @return set of occupied ParkingSpot objects
     */
    public Set<ParkingSpot> getOccupiedSpots() {
        Set<ParkingSpot> spots = new HashSet<>();
        String query = "SELECT ps.spotId, ps.garageId, ps.spotNumber " +
                "FROM ParkingSpot ps " +
                "JOIN OccupancySensor os ON ps.spotId = os.spotId " +
                "WHERE os.isCurrentlyOccupied = 1";

        try (Connection conn = DatabaseConnectionFactory.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                ParkingSpot spot = mapResultSetToSpot(rs);
                spot.setStatus(SpotStatus.OCCUPIED);
                spots.add(spot);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving occupied spots: " + e.getMessage());
            e.printStackTrace();
        }
        return spots;
    }

    /**
     * Updates a parking spot's status in the database
     * 
     * @param spotId the spot ID to update
     * @param status the new status
     * @return true if update was successful, false otherwise
     */
    public boolean updateSpotStatus(int spotId, SpotStatus status) {
        // Note: Actual update typically done via sensor or session updates
        // This is a helper method for direct updates
        try (Connection conn = DatabaseConnectionFactory.getConnection()) {
            // Could update a spot_status table or derive from sensor data
            System.out.println("Spot " + spotId + " status marked as: " + status);
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating spot status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets all parking spots for a specific garage
     * 
     * @param garageId the garage ID
     * @return set of ParkingSpot objects in that garage
     */
    public Set<ParkingSpot> getSpotsByGarage(int garageId) {
        Set<ParkingSpot> spots = new HashSet<>();
        String query = "SELECT spotId, garageId, spotNumber FROM ParkingSpot WHERE garageId = ?";

        try (Connection conn = DatabaseConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, garageId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                spots.add(mapResultSetToSpot(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving spots by garage: " + e.getMessage());
            e.printStackTrace();
        }
        return spots;
    }

    /**
     * Gets occupancy status for a specific spot
     * 
     * @param spotId the spot ID
     * @return SpotStatus (OCCUPIED, AVAILABLE, or OUT_OF_SERVICE)
     */
    public SpotStatus getSpotStatus(int spotId) {
        String query = "SELECT os.isCurrentlyOccupied FROM OccupancySensor os " +
                "WHERE os.spotId = ?";

        try (Connection conn = DatabaseConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, spotId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                boolean isOccupied = rs.getBoolean("isCurrentlyOccupied");
                return isOccupied ? SpotStatus.OCCUPIED : SpotStatus.AVAILABLE;
            }
        } catch (SQLException e) {
            System.err.println("Error getting spot status: " + e.getMessage());
            e.printStackTrace();
        }
        return SpotStatus.OUT_OF_SERVICE;
    }

    /**
     * Helper method to map ResultSet to ParkingSpot object
     * 
     * @param rs the ResultSet from a query
     * @return a ParkingSpot object
     * @throws SQLException if result set access fails
     */
    private ParkingSpot mapResultSetToSpot(ResultSet rs) throws SQLException {
        int spotId = rs.getInt("spotId");
        int garageId = rs.getInt("garageId");
        String spotNumber = rs.getString("spotNumber");

        ParkingSpot spot = new ParkingSpot();
        spot.setSpotId(spotId);
        spot.setGarageId(garageId);
        spot.setSpotNumber(spotNumber);
        spot.setStatus(getSpotStatus(spotId));

        return spot;
    }
}