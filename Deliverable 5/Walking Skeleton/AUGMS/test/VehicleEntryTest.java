package test;

import controller.VehicleEntryController;
import dao.*;
import entity.*;
import java.sql.Timestamp;
import service.GarageSystem;

/**
 * Test suite for UC-102: Process Vehicle Entry
 */
public class VehicleEntryTest {

    public static void main(String[] args) {
        System.out.println("=== Starting Vehicle Entry Test Suite ===");

        try {
            cleanupTestData(); // Ensure clean state
            testVehicleEntryFlow();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("=== Test Suite Completed ===");
    }

    private static void cleanupTestData() {
        System.out.println("\n[Setup] Cleaning up test data...");
        // Ensure STU-1234 has no active sessions
        VehicleDAO vehicleDAO = new VehicleDAO();
        Vehicle v = vehicleDAO.findVehicleByLicensePlate("STU-1234");
        if (v != null) {
            try (java.sql.Connection conn = Database.DatabaseConnectionFactory.getConnection();
                    java.sql.PreparedStatement stmt = conn.prepareStatement(
                            "UPDATE ParkingSession SET exitTime = GETDATE() WHERE vehicleId = ? AND exitTime IS NULL")) {
                stmt.setInt(1, v.getVehicleId());
                stmt.executeUpdate();
            } catch (Exception e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            }

            // Also ensure we have at least one free spot?
            // We'll trust the DB has space (reset sensors if needed, but that's complex
            // without hardcoding)
            try (java.sql.Connection conn = Database.DatabaseConnectionFactory.getConnection();
                    java.sql.PreparedStatement stmt = conn.prepareStatement(
                            "UPDATE OccupancySensor SET isCurrentlyOccupied = 0 WHERE spotId = 26")) {
                // Spot 26 (C-01) was used in exit test, let's free it specifically to be safe
                stmt.executeUpdate();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    private static void testVehicleEntryFlow() {
        System.out.println("\n[Test] Testing Vehicle Entry Flow...");

        String licensePlate = "STU-1234";
        int gateId = 1;

        // 1. Run Controller (First Attempt - Success)
        VehicleEntryController controller = new VehicleEntryController();
        System.out.println(">> Attempting Entry 1 (Should Succeed)...");
        controller.handleEntryRequest(licensePlate, gateId);

        // 2. Verify Session Created
        VehicleDAO vehicleDAO = new VehicleDAO();
        Vehicle v = vehicleDAO.findVehicleByLicensePlate(licensePlate);
        ParkingSessionDAO sessionDAO = new ParkingSessionDAO();
        ParkingSession session = sessionDAO.findActiveSession(v.getVehicleId());

        if (session != null) {
            System.out.println("PASS: Active session found for " + licensePlate + ". Spot ID: " + session.getSpotId());

            // Verify Sensor Occupied
            OccupancySensorDAO sensorDAO = new OccupancySensorDAO();
            OccupancySensor sensor = sensorDAO.findSensorBySpot(session.getSpotId());
            if (sensor != null && sensor.getOccupancyState()) {
                System.out.println("PASS: Sensor for Spot " + session.getSpotId() + " is OCCUPIED.");
            } else {
                System.out.println("FAIL: Sensor not occupied!");
            }

            // 3. Run Controller (Second Attempt - Duplicate)
            System.out.println("\n>> Attempting Entry 2 (Should Fail - Duplicate)...");
            controller.handleEntryRequest(licensePlate, gateId);

        } else {
            System.out.println("FAIL: No active session created!");
        }
    }
}
