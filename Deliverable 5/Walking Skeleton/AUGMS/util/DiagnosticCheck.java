
import dao.VehicleDAO;
import dao.ParkingSpotDAO;
import dao.OccupancySensorDAO;
import entity.Vehicle;
import entity.ParkingSpot;
import entity.OccupancySensor;
import Database.DatabaseConnectionFactory;
import java.sql.Connection;

public class DiagnosticCheck {
    public static void main(String[] args) {
        System.out.println("=== Diagnostic Check ===");
        try {
            Connection conn = DatabaseConnectionFactory.getConnection();
            System.out.println("Connected to: " + conn.getMetaData().getURL());
            System.out.println("Database: " + conn.getCatalog());
            conn.close();
        } catch (Exception e) {
            System.err.println("Connection Failed: " + e.getMessage());
            return;
        }

        System.out.println("\n--- Checking Vehicle 'abc123' ---");
        VehicleDAO vehicleDAO = new VehicleDAO();
        Vehicle v = vehicleDAO.findVehicleByLicensePlate("abc123");
        if (v != null) {
            System.out.println("Found: " + v.getLicensePlate() + " (ID: " + v.getVehicleId() + ")");
        } else {
            System.out.println("NOT FOUND: abc123");
        }

        System.out.println("\n--- Checking Spot 'A-01' Sensor ---");
        ParkingSpotDAO spotDAO = new ParkingSpotDAO();
        ParkingSpot spot = spotDAO.findSpotByNumber("A-01");
        if (spot != null) {
            System.out.println("Spot A-01 Found (ID: " + spot.getSpotID() + ")");
            OccupancySensorDAO sensorDAO = new OccupancySensorDAO();
            OccupancySensor sensor = sensorDAO.findSensorBySpot(spot.getSpotID());
            if (sensor != null) {
                System.out.println(
                        "Sensor Found (ID: " + sensor.getSensorId() + ") - Occupied: " + sensor.getOccupancyState());
            } else {
                System.out.println("WARNING: No Sensor found for Spot A-01!");
            }
        } else {
            System.out.println("Spot A-01 not found.");
        }

    }
}
