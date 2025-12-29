package dao;

import Database.DatabaseConnectionFactory;
import entity.*;
import java.sql.*;
import java.util.*;

/**
 * Data Access Object for OccupancySensor entity.
 * Handles all database operations related to occupancy sensors.
 */
public class OccupancySensorDAO {

    /**
     * Default constructor
     */
    public OccupancySensorDAO() {
    }

    /**
     * Finds an occupancy sensor by sensor ID
     * @param sensorId the sensor ID
     * @return the OccupancySensor object, or null if not found
     */
    public OccupancySensor findSensor(int sensorId) {
        String query = "SELECT sensorId, spotId, isCurrentlyOccupied FROM OccupancySensor WHERE sensorId = ?";
        
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, sensorId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSensor(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding sensor: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Finds the occupancy sensor for a specific parking spot
     * @param spotId the spot ID
     * @return the OccupancySensor for that spot, or null if not found
     */
    public OccupancySensor findSensorBySpot(int spotId) {
        String query = "SELECT sensorId, spotId, isCurrentlyOccupied FROM OccupancySensor WHERE spotId = ?";
        
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, spotId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSensor(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding sensor by spot: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all occupancy sensors in the database
     * @return set of all OccupancySensor objects
     */
    public Set<OccupancySensor> getAllSensors() {
        Set<OccupancySensor> sensors = new HashSet<>();
        String query = "SELECT sensorId, spotId, isCurrentlyOccupied FROM OccupancySensor";
        
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                sensors.add(mapResultSetToSensor(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all sensors: " + e.getMessage());
            e.printStackTrace();
        }
        return sensors;
    }

    /**
     * Gets sensors for a specific garage
     * @param garageId the garage ID
     * @return set of OccupancySensor objects in that garage
     */
    public Set<OccupancySensor> getSensorsByGarage(int garageId) {
        Set<OccupancySensor> sensors = new HashSet<>();
        String query = "SELECT os.sensorId, os.spotId, os.isCurrentlyOccupied " +
                      "FROM OccupancySensor os " +
                      "JOIN ParkingSpot ps ON os.spotId = ps.spotId " +
                      "WHERE ps.garageId = ?";
        
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, garageId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                sensors.add(mapResultSetToSensor(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving sensors by garage: " + e.getMessage());
            e.printStackTrace();
        }
        return sensors;
    }

    /**
     * Updates the occupancy status of a sensor
     * @param sensorId the sensor ID
     * @param isOccupied the new occupancy state
     * @return true if update was successful, false otherwise
     */
    public boolean updateSensorStatus(int sensorId, boolean isOccupied) {
        String query = "UPDATE OccupancySensor SET isCurrentlyOccupied = ? WHERE sensorId = ?";
        
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setBoolean(1, isOccupied);
            stmt.setInt(2, sensorId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating sensor status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Detects current occupancy by reading sensor data
     * @param sensorId the sensor ID to read
     * @return true if the spot is currently occupied, false if available
     */
    public boolean detectOccupancy(int sensorId) {
        String query = "SELECT isCurrentlyOccupied FROM OccupancySensor WHERE sensorId = ?";
        
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, sensorId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBoolean("isCurrentlyOccupied");
            }
        } catch (SQLException e) {
            System.err.println("Error detecting occupancy: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets the occupancy state for a specific sensor
     * @param sensorId the sensor ID
     * @return true if occupied, false if available
     */
    public boolean getOccupancyState(int sensorId) {
        return detectOccupancy(sensorId);
    }

    /**
     * Gets all occupied sensors
     * @return set of OccupancySensor objects that detect occupancy
     */
    public Set<OccupancySensor> getOccupiedSensors() {
        Set<OccupancySensor> sensors = new HashSet<>();
        String query = "SELECT sensorId, spotId, isCurrentlyOccupied FROM OccupancySensor WHERE isCurrentlyOccupied = 1";
        
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                sensors.add(mapResultSetToSensor(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving occupied sensors: " + e.getMessage());
            e.printStackTrace();
        }
        return sensors;
    }

    /**
     * Helper method to map ResultSet to OccupancySensor object
     * @param rs the ResultSet from a query
     * @return an OccupancySensor object
     * @throws SQLException if result set access fails
     */
    private OccupancySensor mapResultSetToSensor(ResultSet rs) throws SQLException {
        int sensorId = rs.getInt("sensorId");
        int spotId = rs.getInt("spotId");
        boolean isOccupied = rs.getBoolean("isCurrentlyOccupied");
        
        return new OccupancySensor(sensorId, spotId, isOccupied);
    }
}