package entity;

import service.GarageSystem;

/**
 * Represents a sensor at the garage entry.
 */
public class EntrySensor {

    private int sensorId;
    private GarageSystem garageSystem;

    public EntrySensor(GarageSystem garageSystem) {
        this.garageSystem = garageSystem;
    }

    /**
     * Simulates detecting a vehicle arrival.
     * 
     * @param licensePlate The license plate of the detected vehicle.
     * @param gateId       The ID of the entry gate.
     */
    public String detectVehicle(String licensePlate, int gateId) {
        System.out.println("EntrySensor: Vehicle detected: " + licensePlate);
        // Delegate to system
        return garageSystem.processEntry(licensePlate, new java.sql.Timestamp(System.currentTimeMillis()), gateId);
    }
}
