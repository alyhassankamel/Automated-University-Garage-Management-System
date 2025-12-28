package AUGMS.dao;

import AUGMS.entity.EntryExitLog;
import AUGMS.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for EntryExitLogs table.
 */
public class EntryExitRepository {

    public void createEntryLog(EntryExitLog log) throws SQLException {
        String sql = "INSERT INTO EntryExitLogs (license_plate, timestamp, type, spot_number, fee) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, log.getLicensePlate());
            pstmt.setTimestamp(2, log.getTimestamp());
            pstmt.setString(3, log.getType());
            pstmt.setString(4, log.getSpotID());
            if (log.getFee() != null) {
                pstmt.setDouble(5, log.getFee());
            } else {
                pstmt.setNull(5, Types.DECIMAL);
            }

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    log.setLogID(rs.getInt(1));
                }
            }
        }
    }

    public void createExitLog(EntryExitLog log) throws SQLException {
        createEntryLog(log); // same insert structure, type should be "EXIT"
    }

    public EntryExitLog getLatestEntryLog(String licensePlate) throws SQLException {
        String sql = "SELECT TOP 1 * FROM EntryExitLogs " +
                "WHERE license_plate = ? AND type = 'ENTRY' " +
                "ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, licensePlate);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    EntryExitLog log = new EntryExitLog();
                    log.setLogID(rs.getInt("log_id"));
                    log.setLicensePlate(rs.getString("license_plate"));
                    log.setTimestamp(rs.getTimestamp("timestamp"));
                    log.setType(rs.getString("type"));
                    log.setSpotID(rs.getString("spot_number"));
                    log.setFee(rs.getDouble("fee"));
                    return log;
                }
            }
        }
        return null;
    }

    public List<EntryExitLog> getEntryExitLogs() throws SQLException {
        List<EntryExitLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM EntryExitLogs ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                EntryExitLog log = new EntryExitLog();
                log.setLogID(rs.getInt("log_id"));
                log.setLicensePlate(rs.getString("license_plate"));
                log.setTimestamp(rs.getTimestamp("timestamp"));
                log.setType(rs.getString("type"));
                log.setSpotID(rs.getString("spot_id"));
                log.setFee(rs.getDouble("fee"));
                logs.add(log);
            }
        }
        return logs;
    }
}

