package AUGMS.dao;

import AUGMS.entity.Vehicle;
import AUGMS.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Vehicle operations
 * Works with the Vehicle table (vehicle_id, license_plate, etc.)
 */
public class VehicleDAO {

    /**
     * Create a new vehicle record
     * Note: vehicleType and ownerName are stored as model and color fields for simplicity
     * In a full implementation, you might want to add these as separate columns
     */
    public void createVehicle(Vehicle vehicle) throws SQLException {
        String sql = "INSERT INTO Vehicle (license_plate, model, color) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, vehicle.getLicensePlate());
            // Store vehicleType in model field, ownerName in color field for now
            // In production, you'd want proper columns for these
            pstmt.setString(2, vehicle.getVehicleType());
            pstmt.setString(3, vehicle.getOwnerName());
            
            pstmt.executeUpdate();
            
            // Get generated vehicle_id
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    vehicle.setVehicleID(String.valueOf(rs.getInt(1)));
                }
            }
        }
    }

    /**
     * Find vehicle by license plate
     */
    public Vehicle findVehicleByLicensePlate(String licensePlate) throws SQLException {
        String sql = "SELECT * FROM Vehicle WHERE license_plate = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, licensePlate);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setVehicleID(String.valueOf(rs.getInt("vehicle_id")));
                    vehicle.setLicensePlate(rs.getString("license_plate"));
                    // Map model to vehicleType, color to ownerName for compatibility
                    vehicle.setVehicleType(rs.getString("model"));
                    vehicle.setOwnerName(rs.getString("color"));
                    vehicle.setModel(rs.getString("model"));
                    vehicle.setColor(rs.getString("color"));
                    return vehicle;
                }
            }
        }
        return null;
    }

    /**
     * Check if vehicle is currently parked (has an active entry log without exit)
     */
    public boolean isVehicleParked(String licensePlate) throws SQLException {
        // Check if there's an entry without a corresponding exit
        String sql = "SELECT COUNT(*) FROM EntryExitLogs e1 " +
                     "WHERE e1.license_plate = ? AND e1.type = 'ENTRY' " +
                     "AND NOT EXISTS (" +
                     "    SELECT 1 FROM EntryExitLogs e2 " +
                     "    WHERE e2.license_plate = e1.license_plate " +
                     "    AND e2.type = 'EXIT' " +
                     "    AND e2.timestamp > e1.timestamp" +
                     ")";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, licensePlate);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
