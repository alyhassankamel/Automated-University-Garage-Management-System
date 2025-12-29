package test;

import service.GarageSystem;
import dao.VehicleDAO;
import dao.OccupancySensorDAO;
import dao.ParkingSpotDAO;
import entity.Vehicle;
import entity.ParkingSpot;
import entity.OccupancySensor;
import java.sql.Timestamp;

public class TestRegistrationAndOccupancy {
    public static void main(String[] args) {
        System.out.println("=== TEST: Registration & Occupancy Persistence ===");

        GarageSystem system = new GarageSystem();
        VehicleDAO vehicleDAO = new VehicleDAO();
        OccupancySensorDAO sensorDAO = new OccupancySensorDAO();
        ParkingSpotDAO spotDAO = new ParkingSpotDAO();

        String plate = "abc123";
        String spotName = "A-01";

        // 1. Cleanup
        System.out.println("Step 1: Cleanup (removing test vehicle if exists)...");
        // We assume manual cleanup or just checking.
        // Ideally we'd have a delete method, but for now let's just proceed.

        // 2. Register
        System.out.println("Step 2: Registering " + plate + "...");
        String regResult = system.registerVehicle(plate, "Visitor");
        System.out.println("Registration Result: " + regResult);

        // 3. Verify Registration in DB (New Connection)
        Vehicle v = vehicleDAO.findVehicleByLicensePlate(plate);
        if (v != null) {
            System.out.println("PASS: Vehicle " + plate + " found in DB. ID: " + v.getVehicleId());
        } else {
            System.out.println("FAIL: Vehicle " + plate + " NOT FOUND in DB after registration!");
        }

        // 4. Reset Sensor for A-01
        System.out.println("Step 4: Resetting A-01 Sensor to Empty...");
        ParkingSpot spot = spotDAO.findSpotByNumber(spotName);
        if (spot != null) {
            OccupancySensor sensor = sensorDAO.findSensorBySpot(spot.getSpotID());
            if (sensor != null) {
                sensorDAO.updateSensorStatus(sensor.getSensorId(), false); // FORCE EMPTY
            }
        }

        // 5. Enter Vehicle
        System.out.println("Step 5: Entering " + plate + " into " + spotName + "...");
        String entryResult = system.processEntry(plate, new Timestamp(System.currentTimeMillis()), 1, spotName);
        System.out.println("Entry Result: " + entryResult);

        // 6. Verify Occupancy in DB
        OccupancySensor sensorVerify = sensorDAO.findSensorBySpot(spot.getSpotID());
        if (sensorVerify != null && sensorVerify.getOccupancyState()) {
            System.out.println("PASS: Sensor for " + spotName + " is OCCUPIED in DB.");
        } else {
            System.out.println("FAIL: Sensor for " + spotName + " is NOT OCCUPIED in DB!");
        }
    }
}
