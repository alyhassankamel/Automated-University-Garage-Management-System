package dao;

import Database.DatabaseConnectionFactory;
import entity.Vehicle;

import java.sql.*;
import java.util.*;

/**
 * Data Access Object for Vehicle entity.
 */
public class VehicleDAO {

    public VehicleDAO() {
    }

    /**
     * Finds a vehicle by license plate.
     * 
     * @param licensePlate The license plate to search for.
     * @return Vehicle object if found, null otherwise.
     */
    public Vehicle findVehicleByLicensePlate(String licensePlate) {
        String query = "SELECT vehicleId, licensePlate, vehicleType, registeredAt FROM Vehicle WHERE licensePlate = ?";

        try (Connection conn = DatabaseConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, licensePlate);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToVehicle(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding vehicle by license plate: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a new vehicle.
     * 
     * @param vehicle The vehicle to create.
     * @return true if successful, false otherwise.
     */
    public boolean createVehicle(Vehicle vehicle) {
        String query = "INSERT INTO Vehicle (licensePlate, vehicleType, registeredAt) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, vehicle.getLicensePlate());
            stmt.setString(2, vehicle.getVehicleType());
            stmt.setTimestamp(3, vehicle.getRegisteredAt());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        vehicle.setVehicleId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating vehicle: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Helper method to map ResultSet to Vehicle object
     */
    private Vehicle mapResultSetToVehicle(ResultSet rs) throws SQLException {
        int vehicleId = rs.getInt("vehicleId");
        String licensePlate = rs.getString("licensePlate");
        String vehicleType = rs.getString("vehicleType");
        Timestamp registeredAt = rs.getTimestamp("registeredAt");

        return new Vehicle(vehicleId, licensePlate, vehicleType, registeredAt);
    }
}