package AUGMS.dao;

import AUGMS.entity.ParkingSpot;
import AUGMS.entity.SpotStatus;
import AUGMS.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for ParkingSpot operations
 * Works with Parking_spots table (spot_id INT, spot_number, spot_status)
 */
public class ParkingSpotDAO {

    /**
     * Find a parking spot by spot number (e.g., "SPOT001")
     */
    public ParkingSpot findSpot(String spotNumber) throws SQLException {
        String sql = "SELECT * FROM Parking_spots WHERE spot_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, spotNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ParkingSpot spot = new ParkingSpot(rs.getString("spot_number"));
                    String statusStr = rs.getString("spot_status");
                    if (statusStr != null) {
                        try {
                            spot.updateStatus(SpotStatus.valueOf(statusStr.toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            spot.updateStatus(SpotStatus.AVAILABLE);
                        }
                    }
                    return spot;
                }
            }
        }
        return null;
    }

    /**
     * Find a parking spot by spot_id (INT)
     */
    public ParkingSpot findSpotById(int spotId) throws SQLException {
        String sql = "SELECT * FROM Parking_spots WHERE spot_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, spotId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ParkingSpot spot = new ParkingSpot(rs.getString("spot_number"));
                    String statusStr = rs.getString("spot_status");
                    if (statusStr != null) {
                        try {
                            spot.updateStatus(SpotStatus.valueOf(statusStr.toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            spot.updateStatus(SpotStatus.AVAILABLE);
                        }
                    }
                    return spot;
                }
            }
        }
        return null;
    }

    /**
     * Get all parking spots
     */
    public List<ParkingSpot> getAllSpots() throws SQLException {
        List<ParkingSpot> spots = new ArrayList<>();
        String sql = "SELECT * FROM Parking_spots ORDER BY spot_number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                ParkingSpot spot = new ParkingSpot(rs.getString("spot_number"));
                String statusStr = rs.getString("spot_status");
                if (statusStr != null) {
                    try {
                        spot.updateStatus(SpotStatus.valueOf(statusStr.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        spot.updateStatus(SpotStatus.AVAILABLE);
                    }
                } else {
                    spot.updateStatus(SpotStatus.AVAILABLE);
                }
                spots.add(spot);
            }
        }
        return spots;
    }

    /**
     * Get all available spots
     */
    public List<ParkingSpot> getAvailableSpots() throws SQLException {
        List<ParkingSpot> spots = new ArrayList<>();
        String sql = "SELECT * FROM Parking_spots WHERE spot_status = 'AVAILABLE' ORDER BY spot_number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                ParkingSpot spot = new ParkingSpot(rs.getString("spot_number"));
                spot.updateStatus(SpotStatus.AVAILABLE);
                spots.add(spot);
            }
        }
        return spots;
    }

    /**
     * Get the first available spot
     */
    public ParkingSpot getFirstAvailableSpot() throws SQLException {
        String sql = "SELECT TOP 1 * FROM Parking_spots WHERE spot_status = 'AVAILABLE' ORDER BY spot_number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                ParkingSpot spot = new ParkingSpot(rs.getString("spot_number"));
                spot.updateStatus(SpotStatus.AVAILABLE);
                return spot;
            }
        }
        return null;
    }

    /**
     * Update spot status by spot number
     */
    public void updateSpotStatus(String spotNumber, SpotStatus status) throws SQLException {
        String sql = "UPDATE Parking_spots SET spot_status = ? WHERE spot_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.toString());
            pstmt.setString(2, spotNumber);
            pstmt.executeUpdate();
        }
    }

    /**
     * Update spot status by spot_id
     */
    public void updateSpotStatusById(int spotId, SpotStatus status) throws SQLException {
        String sql = "UPDATE Parking_spots SET spot_status = ? WHERE spot_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.toString());
            pstmt.setInt(2, spotId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Get current vehicle in a spot (if occupied) by spot number
     */
    public String getCurrentVehiclePlate(String spotNumber) throws SQLException {
        String sql = "SELECT TOP 1 license_plate FROM EntryExitLogs WHERE spot_number = ? AND type = 'ENTRY' " +
                     "AND timestamp = (SELECT MAX(timestamp) FROM EntryExitLogs WHERE spot_number = ? AND type = 'ENTRY') " +
                     "AND NOT EXISTS (SELECT 1 FROM EntryExitLogs e2 WHERE e2.license_plate = EntryExitLogs.license_plate " +
                     "AND e2.type = 'EXIT' AND e2.timestamp > EntryExitLogs.timestamp)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, spotNumber);
            pstmt.setString(2, spotNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("license_plate");
                }
            }
        }
        return null;
    }

    /**
     * Get current vehicle in a spot (if occupied) by spot_id
     */
    public String getCurrentVehiclePlateById(int spotId) throws SQLException {
        String sql = "SELECT TOP 1 license_plate FROM EntryExitLogs WHERE spot_number = ? AND type = 'ENTRY' " +
                     "AND timestamp = (SELECT MAX(timestamp) FROM EntryExitLogs WHERE spot_number = ? AND type = 'ENTRY') " +
                     "AND NOT EXISTS (SELECT 1 FROM EntryExitLogs e2 WHERE e2.license_plate = EntryExitLogs.license_plate " +
                     "AND e2.type = 'EXIT' AND e2.timestamp > EntryExitLogs.timestamp)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, getSpotNumberById(spotId));
            pstmt.setString(2, getSpotNumberById(spotId));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("license_plate");
                }
            }
        }
        return null;
    }

    /**
     * Get spot_id from spot_number
     */
    private int getSpotIdByNumber(String spotNumber) throws SQLException {
        String sql = "SELECT spot_id FROM Parking_spots WHERE spot_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, spotNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("spot_id");
                }
            }
        }
        return -1;
    }

    /**
     * Get spot_number from spot_id
     */
    public String getSpotNumberById(int spotId) throws SQLException {
        String sql = "SELECT spot_number FROM Parking_spots WHERE spot_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, spotId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("spot_number");
                }
            }
        }
        return null;
    }
}
