package dao;

import Database.DatabaseConnectionFactory;
import entity.*;
import java.sql.*;
import java.util.*;

/**
 * Data Access Object for ParkingGarage entity.
 * Handles all database operations related to parking garages.
 */
public class ParkingGarageDAO {

    /**
     * Default constructor
     */
    public ParkingGarageDAO() {
    }

    /**
     * Finds a parking garage by ID
     * @param garageId the garage ID
     * @return the ParkingGarage object with all spots, or null if not found
     */
    public ParkingGarage findGarage(int garageId) {
        String query = "SELECT garageId, garageName, totalSpots FROM ParkingGarage WHERE garageId = ?";
        
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, garageId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                ParkingGarage garage = mapResultSetToGarage(rs);
                // Load all spots for this garage
                ParkingSpotDAO spotDAO = new ParkingSpotDAO();
                garage.setParkingSpots(new ArrayList<>(spotDAO.getSpotsByGarage(garageId)));
                return garage;
            }
        } catch (SQLException e) {
            System.err.println("Error finding garage: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all parking garages in the database
     * @return set of all ParkingGarage objects
     */
    public Set<ParkingGarage> getAllGarages() {
        Set<ParkingGarage> garages = new HashSet<>();
        String query = "SELECT garageId, garageName, totalSpots FROM ParkingGarage";
        
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                ParkingGarage garage = mapResultSetToGarage(rs);
                // Load spots for each garage
                ParkingSpotDAO spotDAO = new ParkingSpotDAO();
                garage.setParkingSpots(new ArrayList<>(spotDAO.getSpotsByGarage(garage.getGarageID())));
                garages.add(garage);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all garages: " + e.getMessage());
            e.printStackTrace();
        }
        return garages;
    }

    /**
     * Updates garage information
     * @param garage the garage with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateGarage(ParkingGarage garage) {
        String query = "UPDATE ParkingGarage SET garageName = ?, totalSpots = ? WHERE garageId = ?";
        
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, garage.getGarageName());
            stmt.setInt(2, garage.getTotalSpots());
            stmt.setInt(3, garage.getGarageID());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating garage: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets the count of available spots in a garage
     * @param garageId the garage ID
     * @return number of available spots
     */
    public int getAvailableSpotCount(int garageId) {
        String query = "SELECT COUNT(*) as count FROM ParkingSpot ps " +
                      "LEFT JOIN OccupancySensor os ON ps.spotId = os.spotId " +
                      "WHERE ps.garageId = ? AND (os.isCurrentlyOccupied = 0 OR os.sensorId IS NULL)";
        
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, garageId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Error getting available spot count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Gets the count of occupied spots in a garage
     * @param garageId the garage ID
     * @return number of occupied spots
     */
    public int getOccupiedSpotCount(int garageId) {
        String query = "SELECT COUNT(*) as count FROM ParkingSpot ps " +
                      "JOIN OccupancySensor os ON ps.spotId = os.spotId " +
                      "WHERE ps.garageId = ? AND os.isCurrentlyOccupied = 1";
        
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, garageId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Error getting occupied spot count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Gets parking status for a specific garage
     * @param garageId the garage ID
     * @return ParkingStatus object with aggregated status data
     */
    public ParkingStatus getParkingStatus(int garageId) {
        ParkingGarage garage = findGarage(garageId);
        if (garage != null) {
            return garage.getParkingStatus();
        }
        return null;
    }

    /**
     * Helper method to map ResultSet to ParkingGarage object
     * @param rs the ResultSet from a query
     * @return a ParkingGarage object
     * @throws SQLException if result set access fails
     */
    private ParkingGarage mapResultSetToGarage(ResultSet rs) throws SQLException {
        int garageId = rs.getInt("garageId");
        String garageName = rs.getString("garageName");
        int totalSpots = rs.getInt("totalSpots");
        
        ParkingGarage garage = new ParkingGarage();
        garage.setGarageId(garageId);
        garage.setGarageName(garageName);
        garage.setTotalSpots(totalSpots);
        garage.setGarageAccessible(true); // Default to accessible; could be stored in DB
        
        return garage;
    }
}