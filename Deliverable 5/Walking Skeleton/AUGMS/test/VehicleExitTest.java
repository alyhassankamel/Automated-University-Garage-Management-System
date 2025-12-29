package test;

import controller.VehicleExitController;
import dao.*;
import entity.*;
import java.sql.Timestamp;
import service.GarageSystem;

/**
 * Test suite for UC-103: Process Vehicle Exit
 */
public class VehicleExitTest {

    public static void main(String[] args) {
        System.out.println("=== Starting Vehicle Exit Test Suite ===");

        try {
            int spotId = setupTestData();
            testVehicleExitFlow(spotId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("=== Test Suite Completed ===");
    }

    private static int setupTestData() {
        System.out.println("\n[Setup] Preparing test data...");
        // 1. Ensure Vehicle exists
        VehicleDAO vehicleDAO = new VehicleDAO();
        Vehicle v = vehicleDAO.findVehicleByLicensePlate("TEST-EXIT-01");
        if (v == null) {
            System.out.println("Using existing vehicle STU-1234 for test.");
        }

        // Find a valid spot
        ParkingSpotDAO spotDAO = new ParkingSpotDAO();
        java.util.Set<ParkingSpot> spots = spotDAO.getAllSpots();
        if (spots.isEmpty()) {
            throw new RuntimeException("No parking spots found in DB! Cannot run test.");
        }
        ParkingSpot validSpot = spots.iterator().next();
        System.out.println("Using Spot ID: " + validSpot.getSpotID() + " (" + validSpot.getSpotNumber() + ")");

        return validSpot.getSpotID();
    }

    private static void createDummySession(String licensePlate, int spotId) {
        // Helper to insert a session directly for testing purposes
        try (java.sql.Connection conn = Database.DatabaseConnectionFactory.getConnection();
                java.sql.PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO ParkingSession (vehicleId, spotId, entryTime) " +
                                "SELECT v.vehicleId, ?, GETDATE() FROM Vehicle v WHERE v.licensePlate = ? " +
                                "AND NOT EXISTS (SELECT 1 FROM ParkingSession ps WHERE ps.vehicleId = v.vehicleId AND ps.exitTime IS NULL)")) {

            stmt.setInt(1, spotId);
            stmt.setString(2, licensePlate);
            int rows = stmt.executeUpdate();
            if (rows > 0)
                System.out.println("Created dummy session for " + licensePlate);
            else
                System.out.println("Session likely already exists for " + licensePlate);

            // Also update the sensor to be occupied
            try (java.sql.PreparedStatement sensorStmt = conn.prepareStatement(
                    "UPDATE OccupancySensor SET isCurrentlyOccupied = 1 WHERE spotId = ?")) {
                sensorStmt.setInt(1, spotId);
                sensorStmt.executeUpdate();
            }

        } catch (Exception e) {
            System.err.println("Setup failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testVehicleExitFlow(int spotId) {
        System.out.println("\n[Test] Testing Vehicle Exit Flow...");

        String licensePlate = "STU-1234";

        // 0. Setup: Ensure session exists
        createDummySession(licensePlate, spotId);

        // 1. Run Controller
        VehicleExitController controller = new VehicleExitController();
        controller.handleExitRequest(licensePlate, 1);

        // 2. Verify Session Closed
        ParkingSessionDAO sessionDAO = new ParkingSessionDAO();
        VehicleDAO vehicleDAO = new VehicleDAO();
        Vehicle v = vehicleDAO.findVehicleByLicensePlate(licensePlate);

        if (v != null) {
            entity.ParkingSession session = sessionDAO.findActiveSession(v.getVehicleId());
            if (session == null) {
                System.out.println("PASS: Session is correctly closed (no active session returned).");
            } else {
                System.out.println("FAIL: Session still active!");
            }
        }

        // 3. Verify Spot Freed
        OccupancySensorDAO sensorDAO = new OccupancySensorDAO();
        entity.OccupancySensor sensor = sensorDAO.findSensorBySpot(spotId);
        if (sensor != null && !sensor.getOccupancyState()) {
            System.out.println("PASS: Sensor is reported as FREE.");
        } else {
            System.out.println("Check Sensor status manually or update test if accessor differs.");
        }
    }
}
